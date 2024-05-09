package ui.utilities;

import java.util.List;
import java.time.Instant;

import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

public class ApplicationInterface {

    MqttJavaClient mqttJavaClient;
    InfluxDBJavaClient influxDBJavaClient;

    public ApplicationInterface() {
        // Getting the instances of related classes
        mqttJavaClient = MqttJavaClient.getInstance();
        influxDBJavaClient = InfluxDBJavaClient.getInstance();

    }

    // Publishing a message to the MQTT broker
    // Excpected topic format is "AquaCare/001/Temperature" - "AquaCare/deviceID/sensorType"
    public void Publish(String topic, String content, int qos) {
        mqttJavaClient.Publish(topic, content, qos);
    }

    //Default qos is 1
    public void Publish(String topic, String content) {
        // Default qos settings are used
        Publish(topic, content, 1);
    }

    public void ActivateFeeder(String deviceID) {
        // Default topic, qos and (content?) settings are used
        Publish(String.format("AquaCare/%s/Feed", deviceID), "1", 1);
        //We may implement duplicate command control system on the terminal side based on the sent content? -Just maybe
        //Other QoS settings may be more appropriate
    }

    /*****************************/

    //Write data on database
    //!for whatever reason
    public void WriteSensorData(String measurement, String tagKey, String tagValue, String fieldKey, double fieldValue) {
        // Time is saved in apoch nano rather than mili so a conversion is needed
        influxDBJavaClient.WriteData(measurement, tagKey, tagValue, fieldKey, fieldValue, Instant.now().toEpochMilli() * 1000000);
    }

    public void WriteSensorData(String measurement, String tagKey, String tagValue, String fieldKey, double fieldValue,
                                Long timestamp) {
        influxDBJavaClient.WriteData(measurement, tagKey, tagValue, fieldKey, fieldValue, timestamp);
    }

    public void WriteSensorData(String measurement, String tagValue, double fieldValue) {
        influxDBJavaClient.WriteData(measurement, tagValue, fieldValue);
    }


    /**********************************************************/
    //Query database

    /**********************************************************/

    /*  1ns // 1 nanosecond
        1us // 1 microsecond
        1ms // 1 millisecond
        1s  // 1 second
        1m  // 1 minute
        1h  // 1 hour
        1d  // 1 day
        1w  // 1 week
        1mo // 1 calendar month
        1y  // 1 calendar year

        3d12h4m25s // 3 days, 12 hours, 4 minutes, and 25 seconds
     */
    public List<FluxTable> MeanOfDuration(String sensorName, String duration, String deviceID) {
        //Example parameters ("Temperature", "1d", "001")
        return influxDBJavaClient.QueryDatabase(duration, sensorName, "value", deviceID, true);
    }

    public List<FluxTable> QueryOfDuration(String sensorName, String duration, String deviceID) {
        //Example parameters ("Temperature", "1d", "001")
        return influxDBJavaClient.QueryDatabase(duration, sensorName, "value", deviceID, false);
    }

    public static double extractMeanValue(List<FluxTable> tables) {
        //Extracts the mean value from the query result
        //Returns the mean value

        //Code copied from another branch >:)
        //Get the latest data from the database
        //Rather than real-realtime data, we are using the mean of 30s of data from the database
        if (!tables.isEmpty()) {
            FluxTable fluxTable = tables.get(0);
            List<FluxRecord> records = fluxTable.getRecords();
            if (!records.isEmpty()) {
                //Get the first entry value
                double value = (double) records.get(0).getValue();
                System.out.println("Returning: "+ value);
                return value;
            }
        }

        System.out.println("Returning: -1");
        return -1;
    }
}