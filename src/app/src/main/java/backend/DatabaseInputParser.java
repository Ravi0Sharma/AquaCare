package backend;

public class DatabaseInputParser{

    InfluxDBJavaClient db;
    
    public DatabaseInputParser() {
        db = InfluxDBJavaClient.getInstance();
    }

    public void parseMqttData(String topic, String message) {
        
        //!Change parse logic 
        //*******************************
        //!Change parse logic

        // Parse the MQTT message
        String[] topicLayers = topic.split("/");            //Split the topic into layers
        if (topicLayers.length != 3) {
            System.out.println("Invalid message format");
            return;
        }
        
        String deviceID = topicLayers[1];                   //Get the device ID
        String measurement = topicLayers[2];                //Get the measurement or sensor type 
        
        String[] messageParts = message.split(",");         //Split the message into parts
        String value = messageParts[0].trim();              //Get the value
        
        if (messageParts.length != 2) {

            if (messageParts.length == 1) {
                //If there is no timestamp, write the data with the current time
                db.WriteData(measurement, deviceID, Double.parseDouble(value));
                return;
            }


            System.out.println("Invalid message format");
            return;
        }
        String timestamp = messageParts[1].trim();          //Get the unixnano timestamp
        
        System.out.println("Writing data to InfluxDB");
        // Forward the parsed data to dbHandler
        db.WriteData(measurement, "deviceID", deviceID, "value", Double.parseDouble(value), Long.parseLong(timestamp));
        System.out.println("Have written data to InfluxDB");
    }

    
}