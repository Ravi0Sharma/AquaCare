package ui.aquacare;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import ui.utilities.ApplicationInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ReadingsController extends NavigationController implements Initializable {

    //  Real-time Readings
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
    @FXML
    private Button feedFishButton;

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

    private volatile boolean running = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDate();
        realTimeData();

        //Set active fish monitor to 1 since owning multiple monitors is not supported yet
        activeFishMonitor = "1";

        Map<LineChart, String> linecharts = new HashMap<>();
        linecharts.put(linechartTemp, "Temperature");
        linecharts.put(linechartDisp, "Dispenser");
        linecharts.put(linechartLight, "Light");
        linecharts.put(linechartPh, "Ph");

        //***********************************************
        //Check which line chart is not null and update it
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
        updateChart(linechartPh, "Ph");
        updateChart(linechartDisp, "Dispenser");
    }

    private void updateChart(LineChart chart, String sensorName) {

        //Create data series for the line chart
        XYChart.Series series = new XYChart.Series();

        //Affects legend which, at the moment, does not exist
        series.setName(sensorName);

        //Query data based on active fish monitor and sensor name
        List<FluxTable> tables = applicationInterface.QueryOfDuration(sensorName, "1d", activeFishMonitor);

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

    /*
     * ------- REAL-TIME DATA VISUALIZATION -------
     */

    public void realTimeData() {
        // create a new thread that listens for incoming text and populates the graph
        Thread thread = new Thread(() -> {

            while (running) {

                // Query data based on active fish monitor and sensor name
                List<FluxTable> tablesPh = applicationInterface.MeanOfDuration("Ph", "30s", activeFishMonitor);
                List<FluxTable> tablesTemp = applicationInterface.MeanOfDuration("Temperature", "30s", activeFishMonitor);
                List<FluxTable> tablesLight = applicationInterface.MeanOfDuration("Light", "30s", activeFishMonitor);
                List<FluxTable> tablesDisp = applicationInterface.LastOfDuration("Dispenser", "25d", activeFishMonitor);

                // Extract the mean values from the tables
                double meanPh = ApplicationInterface.extractMeanValue(tablesPh);
                double meanTemp = ApplicationInterface.extractMeanValue(tablesTemp);
                double meanLight = ApplicationInterface.extractMeanValue(tablesLight);
                double lastFedHour = ApplicationInterface.extractLastRowTimeDifferenceWithNow(tablesDisp);

                Platform.runLater(() -> {
                    // update labels with the mean values
                    phLabel.setText("pH: " + meanPh);
                    tempLabel.setText("Temperature: " + meanTemp + "°C");
                    lightLabel.setText("Light: " + meanLight);
                    feedLabel.setText("Fed " + lastFedHour + " hours ago");


                });

                // sleep to avoid reduce app's CPU usage
                try {
                    Thread.sleep(15000); // 1000 = 1 sec
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    //To turn off the real-time data visualization
    private void stopRealTimeData() {
        running = false;
    }

    private double extractMeanValue(List<FluxTable> tables) {
        double meanValue = 0.0;
        for (FluxTable table : tables) {
            List<FluxRecord> records = table.getRecords();
            for (FluxRecord fluxRecord : records) {
                meanValue = (double) fluxRecord.getValue();
            }
        }
        return meanValue;
    }

    public void feedFish() {
        applicationInterface.ActivateFeeder(activeFishMonitor);
    }
}
