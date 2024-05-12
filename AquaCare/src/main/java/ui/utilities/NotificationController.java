package ui.utilities;

import java.awt.*;
import java.util.List;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.influxdb.query.FluxRecord;

import com.influxdb.query.FluxTable;

public class NotificationController {

    private final ApplicationInterface appInterface;

    //Expected Structure: deviceID, <sensor, (lower threshold, upper threshold)>
    HashMap<String, HashMap<String, Threshold>> deviceMap;

    private int counter;

    public NotificationController() {

        appInterface = new ApplicationInterface();

        // Initialize the executor service with a single thread
        executorService = Executors.newSingleThreadScheduledExecutor();

        // Schedule the checkThresholds method to run every 30 seconds
        // with no initial delay
        executorService.scheduleAtFixedRate(this::updateAndCheck, 30, 30, TimeUnit.SECONDS);
    }

    private void updateAndCheck() {
        if (counter == 10) {
            updateThresholds();
            counter = 0;
        }
            counter++;
            checkThresholds();
    }



    //Scheduled executor service to run the checkThresholds method every interval
    private final ScheduledExecutorService executorService;

    private void updateThresholds() {
        deviceMap = new HashMap<String, HashMap<String, Threshold>>();
        //This function depends on selected fish which is not currently implemented
        //Get the latest data from the json - or maybe influxDB after the recent developments
        //Write it off to the Hashmap object
    }

    private void checkThresholds() {

        //For each aquarium monitor
        for (String deviceID : deviceMap.keySet()) {
            HashMap<String, Threshold> sensorMap = deviceMap.get(deviceID);

            //For each sensor in an aquarium monitor
            for (String sensor : sensorMap.keySet()) {
                Threshold threshold = sensorMap.get(sensor);

                //Get the latest data from the database
                //Rather than real-realtime data, we are using the mean of 30s of data from the database
                List<FluxTable> tables = appInterface.MeanOfDuration(sensor, "30s", deviceID);
                if (!tables.isEmpty()) {
                    FluxTable fluxTable = tables.get(0);
                    List<FluxRecord> records = fluxTable.getRecords();

                    //It may be redundant since tables is not empty
                    if (!records.isEmpty()) {

                        //Get the first entry value
                        double value = (double) records.get(0).getValue();

                        //Check if it is within the threshold and send a notification in case of breach
                        if (value < threshold.getLowerThreshold() || value > threshold.getUpperThreshold()) {
                            NotificationClient.displayTray("Aquarium " + deviceID, "Threshold Breach on " + sensor + " with value " + value);
                        }
                    }
                }
            }
        }
    }
    public void stop() {
        // Shut down the executor service when it's no longer needed
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }



}