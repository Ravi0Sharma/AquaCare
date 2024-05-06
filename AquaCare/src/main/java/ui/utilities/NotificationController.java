package ui.utilities;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.FileReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.influxdb.query.FluxTable;

public class NotificationController {
    
    //private NotificationManager notificationManager;   }
    private ApplicationInterface appInterface;
    //List of all fishes
    //List all their sensors
    //List all their thresholds

    //Get real time sensor data and compare with thresholds for each sensor one by one
    //Change the fish into the next one

    //When loop is done, start again from the first fish after the interval




    private Map<String, JSONObject> fishThresholds;

    private ScheduledExecutorService executorService;

    // Sensor name to be used for the query
    private String sensorName;
    // Duration to be used for the query
    private String duration;
    // Device ID to be used for the query
    private String deviceID;


    //Library to be used for notifications
}