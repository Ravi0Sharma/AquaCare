package ui.aquacare;

import com.fazecast.jSerialComm.SerialPort;
import javafx.animation.KeyFrame;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.fxml.Initializable;
import javafx.animation.Timeline;

import java.net.URL;
import java.time.Instant;
import java.util.ResourceBundle;

import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;
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

    private Timeline timeline;

    //  Units in seconds
    private int dynamicChartUpdateInterval = 10;


    //  Units in seconds
    private int realTimeLabelUpdateInterval = 5;


    //  Units in days
    private int chartDataAge = 7;

    ApplicationInterface applicationInterface = new ApplicationInterface();

    private volatile double threadCordinator = 0;

    Map<LineChart, String> linecharts = new HashMap<>();
    ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Set active fish monitor to 1 since owning multiple monitors is not supported yet
        activeFishMonitor = "1";

        populateSensorMap();

        setDate();

        threadCordinator = Math.random();
        realTimeData(threadCordinator);

        //***********************************************
        //Check which line chart is not null and update it
        //***********************************************
        for (Map.Entry<LineChart, String> chartEntry : linecharts.entrySet()) {
            if (chartEntry.getKey() != null) {
                System.out.println(chartEntry.getKey() + "      " + chartEntry.getValue());
                initializeChartContents(chartEntry.getKey(), chartEntry.getValue());

                if (timeline == null) {
                    System.out.println("Timeline is null");
                    // Create a Timeline that updates the chart every few seconds
                    timeline = new Timeline(new KeyFrame(Duration.seconds(dynamicChartUpdateInterval), event -> {
                        //Wonder if we could put realTimeData() here as well instead of creating a thread
                        System.out.println("Initiated a timeline");
                        updateChart(chartEntry.getKey(), chartEntry.getValue());
                    }));
                    timeline.setCycleCount(Timeline.INDEFINITE);
                    timeline.play();
                }

            } else {
                System.out.println(chartEntry.getValue() + " is null");
            }
        }
        //updateChartData();

    }

    /**
     * ------- HISTORICAL READINGS VISUALIZATION -------
     */

    private void initializeChartContents(LineChart chart, String sensorName) {

        //Create data series for the line chart
        XYChart.Series series = new XYChart.Series();

        //Affects legend which, at the moment, does not exist
        series.setName(sensorName);

        //Query data based on active fish monitor and sensor name
        List<FluxTable> tables = applicationInterface.QueryOfDuration(sensorName, String.valueOf(chartDataAge) + "d", activeFishMonitor);

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

        //XYChart.Data.setNode(hoverPane);

        //*************************************************************************************

        //Update the line chart with milked values
        chart.getData().add(series);
    }

    private void updateChart(LineChart chart, String sensorName) {

        //Query data based on active fish monitor and sensor name
        List<FluxTable> tables = applicationInterface.QueryOfDuration(sensorName, String.valueOf(dynamicChartUpdateInterval) + "s", activeFishMonitor);

        // Check if the chart already has data
        if (!chart.getData().isEmpty()) {

            // Get the first (and in this case, only) series from the chart

            XYChart.Series<String, Number> series = (XYChart.Series<String, Number>) chart.getData().get(0);

            //Divide tables into individual tables
            for (FluxTable table : tables) {

                //Get the records from the table, which correspond to rows in a table
                List<FluxRecord> records = table.getRecords();

                for (FluxRecord fluxRecord : records) {

                    System.out.println("value: " + fluxRecord.getValue() + "    stamp:" + fluxRecord.getTime().toString());

                    //Logic for checking if the data is already in the chart

                    if (seriesIsDataDuplicate(series, fluxRecord) == false) {
                        //Save date and value to the data series, if the data is not already in the chart
                        series.getData().add(new XYChart.Data(fluxRecord.getTime().toString(), fluxRecord.getValue()));
                    }

                }
            }
        }
    }

    /*
     * ------- REAL-TIME DATA VISUALIZATION -------
     */

    public void realTimeData(double threadIndex) {
        // create a new thread that listens for incoming text and populates the graph
        Thread thread = new Thread(() -> {

            while (threadIndex == threadCordinator) {

                // Query data based on active fish monitor and sensor name
                List<FluxTable> tablesPh = applicationInterface.MeanOfDuration("Ph", String.valueOf(realTimeLabelUpdateInterval) + "s", activeFishMonitor);
                List<FluxTable> tablesTemp = applicationInterface.MeanOfDuration("Temperature", String.valueOf(realTimeLabelUpdateInterval) + "s", activeFishMonitor);
                List<FluxTable> tablesLight = applicationInterface.MeanOfDuration("Light", String.valueOf(realTimeLabelUpdateInterval) + "s", activeFishMonitor);
                List<FluxTable> tablesDisp = applicationInterface.LastOfDuration("Dispenser", "25d", activeFishMonitor);

                // Extract the mean values from the tables
                double meanPh = ApplicationInterface.extractMeanValue(tablesPh);
                double meanTemp = ApplicationInterface.extractMeanValue(tablesTemp);
                double meanLight = ApplicationInterface.extractMeanValue(tablesLight);
                double lastFedHour = ApplicationInterface.extractLastRowHourlyTimeDifference(tablesDisp);

                Platform.runLater(() -> {
                    // update labels with the mean values
                    phLabel.setText("pH: " + meanPh);
                    tempLabel.setText("Temperature: " + meanTemp + "°C");
                    lightLabel.setText("Light: " + meanLight);
                    feedLabel.setText("Fed " + lastFedHour + " hours ago");


                });

                // sleep to avoid reduce app's CPU usage
                try {
                    Thread.sleep(3000); // 1000 = 1 sec
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return;
        });
        thread.start();
    }

    public void feedFish() {
        applicationInterface.ActivateFeeder(activeFishMonitor);
    }

    private void populateSensorMap() {
        if (linecharts.isEmpty()) {
            linecharts.put(linechartTemp, "Temperature");
            linecharts.put(linechartDisp, "Dispenser");
            linecharts.put(linechartLight, "Light");
            linecharts.put(linechartPh, "Ph");
        }
    }

    private boolean seriesIsDataDuplicate(XYChart.Series<String, Number> series, FluxRecord newRecord) {

        System.out.println("Checking if the data is duplicate");

        // Check if the series is empty
        if (!series.getData().isEmpty()) {
            // Get the last data point in the series
            XYChart.Data<String, Number> lastData = series.getData().get(series.getData().size() - 1);

            // Parse the timestamps to Instant for comparison
            Instant lastDataTime = Instant.parse(lastData.getXValue());
            Instant newRecordTime = newRecord.getTime();

            // Compare the timestamp of the new data with the timestamp of the last data
            if (newRecordTime.isBefore(lastDataTime) || newRecordTime.equals(lastDataTime)) {
                // If the timestamp of the new data is smaller or equal to the timestamp of the last data, return true
                return true;
            }
        }
        // If the series is empty or the timestamp of the new data is greater than the timestamp of the last data, return false
        return false;
    }
}
