package backend;

//import java.backend.ApplicationInterface;

public class Main {
 
    public static void main(String[] args) {
        //Initialize the database and mqtt clients
        InfluxDBJavaClient db = InfluxDBJavaClient.getInstance();
        
        //MqttJavaClient.getInstance();  //Will be used instead(I assume)
        MqttJavaClient mqttJavaClient = MqttJavaClient.getInstance();
        


/*         for (int i = 0; i < 2; i++) {
            mqttJavaClient.Publish("AquaCare/topic", "Ayo", 1);
        }

        for (int i = 0; i < 10; i++) {
            //app.WriteSensorData("Temperature", "deviceID", "2", "value", 25.0 + i * 5);
        }
        for (int i = 0; i < 1; i++) {
            mqttJavaClient.Publish("AquaCare/topic", "Ayo", 1);
        }
 */

        System.out.println(db.QueryDatabase("Temperature", "1h", "value", "3"));






/*         app.Publish("AquaCare/topic", "Ayo", 1);

        
        
        for (int i = 0; i < 10; i++) {
            app.Publish("AquaCare/topic", "Ayo", 1);
        }

        for (int i = 0; i < 10; i++) {
            //app.WriteSensorData("Temperature", "deviceID", "2", "value", 25.0 + i * 5);
        }


        app.QueryOfDuration("Temparature", "15m", "2");
 

        for (int i = 0; i < 3; i++) {
            app.Publish("AquaCare/topic", "Ayo", 1);
        }
 */

    }


}
