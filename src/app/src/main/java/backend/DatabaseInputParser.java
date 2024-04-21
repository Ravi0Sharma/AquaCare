package backend;

public class DatabaseInputParser{

    public DatabaseInputParser(InfluxdbClient dbHandler) {
        
    }

    public void parseAndForward(String topic, String message) {

        // Parse the MQTT message
        String[] topicLayers = topic.split("/");
        String deviceID = topicLayers[1];
        String measurement = topicLayers[2];
        
        String[] messageParts = message.split(",");
        String value = messageParts[0].trim();
        String timestamp = messageParts[1].trim();
        

        // Forward the parsed data to dbHandler
        //InfluxdbClient.WriteData(measurement, "deviceID", deviceID, "value", Double.parseDouble(value), Long.parseLong(timestamp));
    }

    
}