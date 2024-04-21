package backend;

import java.util.List;
import com.influxdb.query.FluxTable;

public class ApplicationInterface {
    
    MqttJavaClient mqttJavaClient;
    InfluxdbClient influxdbClient;

    public ApplicationInterface() {
        mqttJavaClient = new MqttJavaClient();
        influxdbClient = new InfluxdbClient();
    }

    public void Publish(String topic, String content, int qos) {
       mqttJavaClient.Publish(topic, content ,qos);
    }

    public void Publish(String topic, String content) {
        // Default qos settings are used
        //Excpected topic format is "AquaCareApp/deviceID/actuator"
        Publish(topic, content, 1);
    }
    
    public void ActivateFeeder(String content) {
        // Default topic, qos and (content?) settings are used
        Publish("AquaCareApp/deviceID/actuator", content, 1);
        //We may implement duplicate command control system on the terminal side based on the sent content? -Just maybe
        //Other QoS settings may be more appropriate
    }
   
    public void WriteSensorData(String measurement, String tagKey, String tagValue, String fieldKey, double fieldValue) {
        influxdbClient.WriteData(measurement, tagKey, tagValue, fieldKey, fieldValue);
    }


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
        
        //Example parameters (Temparature, 1d, 123123)
        //dbQueryClient.QueryDatabase(sensorName, duration, deviceID, true);
        //Null values are handled on db side

        return influxdbClient.QueryDatabase(sensorName, duration, "value", deviceID, true);
    }

    public List<FluxTable> QueryOfDuration(String sensorName, String duration, String deviceID) {
        return influxdbClient.QueryDatabase(sensorName, duration, "value", deviceID, false);
    }


}