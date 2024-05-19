package ui.aquacare;

import com.fazecast.jSerialComm.SerialPort;
import javafx.animation.KeyFrame;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
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
import ui.utilities.NotificationController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private int realTimeLabelDataAge = 60;

    // Units in milliseconds - 1 seconds = 1000 milliseconds
    private int realTimeDataUpdateInterval = 3000;


    //  Units in seconds - 1 day = 24 hours = 1440 minutes = 86400 seconds
    private int chartDataAge = 20 * 60; // 20 mins

    ApplicationInterface applicationInterface = new ApplicationInterface();

    // Still not exactly sure how static fixed multiple thread problem
    private static ScheduledExecutorService executorService;

    private static String tempLevelText;
    private static String phLevelText;
    private static String lightLevelText;
    private static String feedLevelText;

    Map<LineChart, String> linecharts = new HashMap<>();
    ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Update the labels with the latest values so changing scenes do not show default values, may be considered redundant
        updateLabels();

        //Set active fish monitor to 1 since owning multiple monitors is not supported yet
        activeFishMonitor = "1";

        populateSensorMap();

        setDate();

        realTimeData();

        //***********************************************
        //Check which line chart is not null and update it
        //***********************************************
        for (Map.Entry<LineChart, String> chartEntry : linecharts.entrySet()) {
            if (chartEntry.getKey() != null) {
                initializeChartContents(chartEntry.getKey(), chartEntry.getValue());

                if (timeline == null) {
                    // Create a Timeline that updates the chart every few seconds
                    timeline = new Timeline(new KeyFrame(Duration.seconds(dynamicChartUpdateInterval), event -> {
                        //Wonder if we could put realTimeData() here as well instead of creating a thread
                        updateChart(chartEntry.getKey(), chartEntry.getValue()); //Will enter new values dynamically to the chart
                    }));
                    timeline.setCycleCount(Timeline.INDEFINITE);
                    timeline.play();
                }

            } 
        }

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
        List<FluxTable> tables = applicationInterface.QueryOfDuration(sensorName, String.valueOf(chartDataAge) + "s", activeFishMonitor);

        //Divide tables into individual tables
        for (FluxTable table : tables) {

            //Get the records from the table, which correspond to rows in a table
            List<FluxRecord> records = table.getRecords();

            for (FluxRecord fluxRecord : records) {

                XYChart.Data<String, Number> dataPoint = new XYChart.Data(fluxRecord.getTime().toString(), fluxRecord.getValue());

                //Save date and value to the data series
                series.getData().add(addTooltip(dataPoint, fluxRecord));
            }
        }

        // Initialize chart settings
        initializeChartSettings(chart);

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

                    //Logic for checking if the data is already in the chart

                    if (seriesIsDataDuplicate(series, fluxRecord) == false) {

                        // Create a new data point
                        XYChart.Data<String, Number> dataPoint = new XYChart.Data(fluxRecord.getTime().toString(), fluxRecord.getValue());

                        //Save date and value to the data series, if the data is not already in the chart
                        series.getData().add(addTooltip(dataPoint, fluxRecord));
                    }

                }
            }
            removeOldEntries(series); //Will remove old entries dynamically from the chart
        }
    }

    /*
     * ------- REAL-TIME DATA VISUALIZATION -------
     */

    public void realTimeData() {
        // If there's already a running task, cancel it
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
        }

        // Create a new executor service
        executorService = Executors.newSingleThreadScheduledExecutor();

        // Schedule the task to run every interval
        executorService.scheduleAtFixedRate(() -> {
            // Query data based on active fish monitor and sensor name
            List<FluxTable> tablesPh = applicationInterface.MeanOfDuration("Ph", String.valueOf(realTimeLabelDataAge) + "s", activeFishMonitor);
            List<FluxTable> tablesTemp = applicationInterface.MeanOfDuration("Temperature", String.valueOf(realTimeLabelDataAge) + "s", activeFishMonitor);
            List<FluxTable> tablesLight = applicationInterface.MeanOfDuration("Light", String.valueOf(realTimeLabelDataAge) + "s", activeFishMonitor);
            List<FluxTable> tablesDisp = applicationInterface.LastOfDuration("Dispenser", "25d", activeFishMonitor);

            // Extract the mean values from the tables
            double meanPh = ApplicationInterface.extractMeanValue(tablesPh);
            double meanTemp = ApplicationInterface.extractMeanValue(tablesTemp);
            double meanLight = ApplicationInterface.extractMeanValue(tablesLight);
            double lastFedHour = ApplicationInterface.extractLastRowHourlyTimeDifference(tablesDisp);

            //Check for threshold breaches
            if (!(meanPh == -1 || meanTemp == -1)) {
                NotificationController.checkThresholds(meanTemp, meanPh);
            }

            Platform.runLater(() -> {
                // update labels with the mean values

                //Truncate the values to a certain length
                phLevelText = Double.toString(meanPh).substring(0, Math.min(Double.toString(meanPh).length(), 4));
                tempLevelText = Double.toString(meanTemp).substring(0, Math.min(Double.toString(meanPh).length(), 5));
                lightLevelText = Double.toString(meanLight).substring(0, Math.min(Double.toString(meanLight).length(), 6));
                feedLevelText = Double.toString(lastFedHour).substring(0, Math.min(Double.toString(lastFedHour).length(), 5));

                updateLabels();
            });
        }, 0, 5, TimeUnit.SECONDS);
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

    private void removeOldEntries(XYChart.Series<String, Number> series){
        // Get the current time
        Instant currentTime = Instant.now();

        // Iterate through the data points in the series
        for (int i = 0; i < series.getData().size(); i++) {
            // Get the data point
            XYChart.Data<String, Number> data = series.getData().get(i);

            // Parse the timestamp of the data point to Instant
            Instant dataTime = Instant.parse(data.getXValue());

            // Calculate the difference in seconds between the current time and the timestamp of the data point
            long diffInSeconds = currentTime.getEpochSecond() - dataTime.getEpochSecond();

            // If the difference is greater than the threshold, remove the data point from the series
            if (diffInSeconds > chartDataAge) {
                series.getData().remove(i);
                // Decrement the counter as we have removed an element
                i--;
            }
            else {
                return;
            }
        }
    }

    private XYChart.Data<String, Number> addTooltip(XYChart.Data<String, Number> dataPoint, FluxRecord fluxRecord) {

        // Create a StackPane to use as the node for the data point
        StackPane stackPane = new StackPane();

        // Create a Tooltip with the timestamp
        Tooltip tooltip = new Tooltip( fluxRecord.getValue().toString() + "\n" +  fluxRecord.getTime().toString());

        // Add the Tooltip to the StackPane
        Tooltip.install(stackPane, tooltip);

        // Set the node for the data point to the StackPane
        dataPoint.setNode(stackPane);

        return dataPoint;
    }

    private void initializeChartSettings(LineChart chart) {
        //Turn off the legend for the line chart, since it takes precious space
        chart.setLegendVisible(false);

        //Turn off the symbols or dots on the line chart, since they over-crowd the chart
        chart.setCreateSymbols(false);

        //Turn off the animation of the line chart, since it is not needed
        chart.setAnimated(false);

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
    }
    private void updateLabels() {
        phLabel.setText("pH: " + phLevelText);
        tempLabel.setText("Temperature: " + tempLevelText  + "°C");
        lightLabel.setText("Light: " + lightLevelText);
        feedLabel.setText("Fed " + feedLevelText + " hours ago");
    }

}
