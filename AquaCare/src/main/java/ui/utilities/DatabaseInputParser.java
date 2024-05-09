package ui.utilities;

import java.time.Instant;

public class DatabaseInputParser {

    InfluxDBJavaClient dataBaseHandler;

    public DatabaseInputParser() {
        dataBaseHandler = InfluxDBJavaClient.getInstance();
    }

    public void parseMqttData(String topic, String message) {

        // Parse the MQTT message
        String[] topicLayers = topic.split("/");      //Split the topic into layers
        if (topicLayers.length != 3) {                      //Ensure the topic is in the correct format
            System.out.println("Invalid topic format");
            return;
        }

        String deviceID = topicLayers[1];                   //Get the device ID
        String measurement = topicLayers[2];                //Get the measurement or sensor type 

        String[] messageParts = message.split(",");   //Split the message into parts
        String value = messageParts[0].trim();              //Get the value

        if (isParsableToDouble(value) == false) {           //Ensure the value is in the correct format
            System.out.println("Value not in correct format");
            return;
        }

        if (messageParts.length >= 2) {
            String timestamp = messageParts[1].trim();      //Get the timestamp

            if (isParsableToLong(timestamp) == false) {
                System.out.println("Timestamp not in correct format");

            } else if (messageSentInLastWeekNanoseconds(Long.parseLong(timestamp)) == false) {
                System.out.println("Timestamp is not from the last week");
            } else {

                System.out.println("Writing data to InfluxDB");
                // If there is a timestamp in the correct format, write the data with the timestamp
                // Since the database only holds data of the last month, giving an older data may cause disconnection
                // Excpect time in nanoseconds
                dataBaseHandler.WriteData(measurement, "deviceID", deviceID, "value", Double.parseDouble(value), Long.parseLong(timestamp));
                System.out.println("Have written data to InfluxDB");
                return;
            }
        }


        // This section saves the data even though there is no timestamp, but that may not be required for us
        // Since it was stated that timestamps are in fact a requirement

        System.out.println("Writing data without timestamp to InfluxDB");
        //If there is no timestamp, write the data with the current time
        dataBaseHandler.WriteData(measurement, "deviceID", deviceID, "value", Double.parseDouble(value));
        System.out.println("Have written data to InfluxDB");
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

    //Check if the message was sent in the last week
    private boolean messageSentInLastWeekNanoseconds(Long timestamp) {
        Long currentTime = Instant.now().getEpochSecond() * 1000000000L;
        Long weekInSeconds = 604800L;
        Long weekInNanoseconds = weekInSeconds * 1000000000L;
        Long weekAgo = currentTime - weekInNanoseconds;
        if (timestamp < weekAgo || timestamp > currentTime) {
            return false;
        }
        return true;
    }

}