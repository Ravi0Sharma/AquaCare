package ui.utilities;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.FileReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.influxdb.query.FluxRecord;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.influxdb.query.FluxTable;

public class NotificationController {

    private ApplicationInterface appInterface;

    //Expected Structure: deviceID, <sensor, (lower threshold, upper threshold)>
    HashMap<String, HashMap<String, Threshold>> deviceMap;

    //Scheduled executor service to run the checkTresholds method every interval
    private ScheduledExecutorService executorService;

    // Sensor name to be used for the query
    private String sensorName;
    // Duration to be used for the query
    private String duration;
    // Device ID to be used for the query
    private String deviceID;

    //notificationClient.displayTray("Aquarium " + deviceID, "Treshold Breach on " + sensorName);


    private void updateTresholds() {
        deviceMap = new HashMap<String, HashMap<String, Threshold>>();
        //Get the latest data from the json - or maybe influxDB after the recent developements
        //Write it off to a java object
    }

    private void checkTresholds() {

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

                    //If may be redundant since tables is not empty
                    if (!records.isEmpty()) {

                        //Get the first entry value
                        double value = (double) records.get(0).getValue();

                        //Check if it is within the threshold and send a notification in case of breach
                        if (value < threshold.getLowerThreshold() || value > threshold.getUpperThreshold()) {
                            try {
                                NotificationClient.displayTray("Aquarium " + deviceID, "Treshold Breach on " + sensor + " with value " + value);
                            } catch (AWTException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}