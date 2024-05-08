package ui.utilities;

public class DatabaseInputParser {

    InfluxDBJavaClient dataBaseHandler;

    public DatabaseInputParser() {
        dataBaseHandler = InfluxDBJavaClient.getInstance();
    }

    public void parseMqttData(String topic, String message) {

        //!Change parse logic 
        //*******************************
        //!Change parse logic

        // Parse the MQTT message
        String[] topicLayers = topic.split("/");            //Split the topic into layers
        if (topicLayers.length != 3) {                      //Ensure the topic is in the correct format
            System.out.println("Invalid message format");
            return;
        }

        String deviceID = topicLayers[1];                   //Get the device ID
        String measurement = topicLayers[2];                //Get the measurement or sensor type 

        String[] messageParts = message.split(",");         //Split the message into parts
        String value = messageParts[0].trim();              //Get the value

        //Ensure the message is in the correct format
        if (isParsableToDouble(value) == false) {
            System.out.println("Value not in correct format");

        } else {
            System.out.println("Writing data to InfluxDB");
            //If there is no timestamp, write the data with the current time
            dataBaseHandler.WriteData(measurement, "deviceID", deviceID, "value", Double.parseDouble(value));
            System.out.println("Have written data to InfluxDB");
            return;

        }

        if (messageParts.length >= 2) {

            String timestamp = messageParts[1].trim();          //Get the unixnano timestamp

            if (isParsableToLong(timestamp) == false) {         //Ensure the timestamp is in the correct format
                System.out.println("Timestamp not in correct format");
                return;
            }
            System.out.println("Writing data to InfluxDB");
            // Forward the parsed timestamped data to database
            dataBaseHandler.WriteData(measurement, "deviceID", deviceID, "value", Double.parseDouble(value), Long.parseLong(timestamp));
            System.out.println("Have written data to InfluxDB");
            return;
        }


        /*if (messageParts.length <= 2) {                     //Ensure the message is in the correct format
            if (messageParts.length == 1) {
                System.out.println("Writing data to InfluxDB");
                //If there is no timestamp, write the data with the current time
                dataBaseHandler.WriteData(measurement, "deviceID", deviceID, "value", Double.parseDouble(value));
                System.out.println("Have written data to InfluxDB");
                return;
        }

        }*/

        return;

    }

    private boolean isParsableToDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isParsableToLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}