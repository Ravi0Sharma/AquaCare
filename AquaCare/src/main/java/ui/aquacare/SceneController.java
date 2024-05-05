package ui.aquacare;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import ui.utilities.ApplicationInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SceneController implements Initializable {

//  General
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Button exitButton;
    @FXML
    private Text dateHP;

//  Real-time Data
    static SerialPort chosenPort;
    static int x = 0;   //
    @FXML
    private static ComboBox<String> portList;
    @FXML
    private static Button connectButton;
    @FXML
    private VBox roots;
    //  Dashboard Cards
    @FXML
    private Label phLabel;
    @FXML
    private Label lightLabel;
    @FXML
    private Label tempLabel;
    @FXML
    private Label feedLabel;

//  Historical Readings
    @FXML
    private LineChart<String, Number> linechartPh;
    @FXML
    private LineChart<String, Number> linechartTemp;
    @FXML
    private LineChart<String, Number> linechartLight;
    @FXML
    private LineChart<String, Number> linechartDisp;

    private String activeFishMonitor;
    ApplicationInterface applicationInterface = new ApplicationInterface();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        setDate();
        realTimeData();

        //Set active fish monitor to 1 since owning multiple monitors is not supported yet
        activeFishMonitor = "1";

        Map<LineChart, String> linecharts = new HashMap<>();
        linecharts.put(linechartTemp, "Temperature");
        linecharts.put(linechartDisp, "Feed");
        linecharts.put(linechartLight, "Light");
        linecharts.put(linechartPh, "Ph");

        //***********************************************
        //Check which linechart is not null and update it
        //***********************************************

        for (Map.Entry<LineChart, String> chartEntry : linecharts.entrySet()) {
            if (chartEntry.getKey() != null) {
                updateChart(chartEntry.getKey(), chartEntry.getValue());
            } else {
                System.out.println(chartEntry.getValue() + " is null");
            }
        }

    }

    /**
     * ------- HISTORICAL READINGS VISUALIZATION -------
     */

    private void updateAllCharts() {
        //! Should be triggered after a fish change
        //The current structure of the UI makes this function obsolete since the charts are updated in the initialize function
        updateChart(linechartTemp, "Temperature");
        updateChart(linechartLight, "Light");
        updateChart(linechartPh, "PhLevel");
        updateChart(linechartDisp, "Dispenser");
    }

    private void updateChart(LineChart chart, String sensorName) {

        //Create data series for the line chart
        XYChart.Series series = new XYChart.Series();

        //Affects legend which, at the moment, does not exist
        series.setName(sensorName);

        //Query data based on active fish monitor and sensor name
        List<FluxTable> tables = applicationInterface.QueryOfDuration(activeFishMonitor, "1w", sensorName);

        //Divide tables into individual tables
        for (FluxTable table : tables) {

            //Get the records from the table, which correspond to rows in a table
            List<FluxRecord> records = table.getRecords();

            for (FluxRecord fluxRecord : records) {

                //This actually works as intended
                System.out.println("value: " + fluxRecord.getValue() + "    stamp:" + fluxRecord.getTime().toString());

                //Save date and value to the data series
                series.getData().add(new XYChart.Data(fluxRecord.getTime().toString(), fluxRecord.getValue()));
            }
        }

        //*************************************************************************************
        //These can be set up within fxml files themselves.

        //Turn off the legend for the line chart, since it takes precious space
        chart.setLegendVisible(false);

        //Turn off the symbols or dots on the line chart, since they over-crowd the chart
        chart.setCreateSymbols(false);

        //Turn off the animation of the line chart, since it is not needed
        chart.setAnimated(false);

        //Instead of turning them off, try shortening time stamps
        //Giving user about when data is collected is important
        //Another choice would be to somehow downsample the labels(not data as a whole), to make it less crowded

        //Turn off the horizontal grid labels of the line chart, since they make the chart much smaller
        chart.getXAxis().setTickLabelsVisible(false);
        chart.getXAxis().setOpacity(0);

        //Turns off effects on the line chart, since they are not needed
        chart.setEffect(null);

        //Turn auto-scaling off for the line chart, realistically value will be between two values
        chart.getYAxis().setAutoRanging(false);

        //These values should be based on sensor type and tresholds set in json file
        ((NumberAxis) chart.getYAxis()).setLowerBound(0);
        ((NumberAxis) chart.getYAxis()).setUpperBound(100);

        //*************************************************************************************

        //Update the line chart with milked values
        chart.getData().add(series);
    }


    /**
     * ------- REAL-TIME DATA VISUALIZATION -------
     */
    // This method is useless if you start to use MQTT for RT display. because data will be processed in that class.
    private String[] processData(String rawData) {
        // we will use prefixes as "'temperature' : 21" and "'pH':8". to get the data at the right we split by :
            String[] parts = rawData.split(":");
            if (parts.length != 2) {    // the data should only have the type and the value (for RT)
                return new String[]{"", ""};
            }
            // Extract sensor type and value
            String sensorType = parts[0].trim();
            String sensorValue = parts[1].trim();
            return new String[]{sensorType, sensorValue};
        }

    private String readDataFromSerialPort() {
        byte[] buffer = new byte[1024]; // holds the data received from the serial port
        int numBytes = chosenPort.readBytes(buffer, buffer.length);
        return new String(buffer, 0, numBytes); //convert byte array buffer to string starting from index 0
    }

    public void realTimeData(){
        portList = new ComboBox<>();
        connectButton = new Button("Connect");
        roots = new VBox(portList, connectButton);

        // populate the box with available port names
        SerialPort[] portNames = SerialPort.getCommPorts();
        for (SerialPort port : portNames) {
            portList.getItems().add(chosenPort.getSystemPortName());
        }

        connectButton.setOnAction(event -> {
            if (connectButton.getText().equals("Connect")) {
                // attempt to connect to the serial port
                chosenPort = SerialPort.getCommPort(portList.getValue().toString());
                chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);


                if (chosenPort.openPort()) {
                    connectButton.setText("Disconnect");
                    portList.setDisable(true);
                }

                // create a new thread that listens for incoming text and populates the graph
                Thread thread = new Thread(() -> {
                    while (true) {

                        String data = readDataFromSerialPort();
                        String[] sensorData = processData(data);

                        Platform.runLater(() -> {
                            // update labels based on sensor type
                            if (sensorData[0].equals("pH")) {
                                phLabel.setText("pH: " + sensorData[1]);
                            } else if (sensorData[0].equals("Temperature")) {
                                tempLabel.setText("Temperature: " + sensorData[1] + "°C");
                            } else if (sensorData[0].equals("Light")) {
                                lightLabel.setText("Light: " + sensorData[1]);
                            }
//                            else if (sensorData[0].equals("Dispenser")) {
//                                lightLabel.setText("Last Fed: " + sensorData[1]);
//                            }
                        });

                        // sleep to avoid reduce app's CPU usage
                        try {
                            Thread.sleep(1000); // 1000 = 1 sec
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            } else {
                // disconnect from the serial port
                chosenPort.closePort();
                portList.setDisable(false);
                connectButton.setText("Connect");
                x = 0;
            }
        });
    }

    /**
     * ------- HOME PAGE "BASE" -------
      */
    public void setDate(){
        // TODO: The date should be able to change while the app is still running.
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter theFormat = DateTimeFormatter.ofPattern("d MMMM YYYY");
        String formattedDate = localDate.format(theFormat);
        dateHP.setText("" + formattedDate);
    }

    @FXML
    private void quit(){
        exitButton.setOnAction(event -> {
            Platform.exit();
        });
        // TODO: add new exit function to close sensor readings too.
    }

    private void goToPage(String fxmlFileName, ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlFileName));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goHomePage(ActionEvent event) throws IOException {
        goToPage("homepage.fxml", event);
    }
    @FXML
    private void goSearchPage(ActionEvent event) throws IOException {
        goToPage("fish-search.fxml", event);
    }
    @FXML
    private void goPhPage(ActionEvent event) throws IOException {
        goToPage("ph-scene.fxml", event);
    }
    @FXML
    private void goFoodPage(ActionEvent event) throws IOException {
        goToPage("food-scene.fxml", event);
    }
    @FXML
    private void goTempPage(ActionEvent event) throws IOException {
        goToPage("temperature-scene.fxml", event);
    }
    @FXML
    private void goConfigPage(ActionEvent event) throws IOException {
        goToPage("configRanges.fxml", event);
    }
    @FXML
    private void goSettings(ActionEvent event) throws IOException {
        goToPage("settings.fxml", event);
    }

}
