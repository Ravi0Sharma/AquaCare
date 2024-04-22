package backend;

//import java.backend.ApplicationInterface;

public class Main {
 
    public static void main(String[] args) {
        //Initialize the database and mqtt clients
        InfluxDBJavaClient db = InfluxDBJavaClient.getInstance();
        
        //MqttJavaClient.getInstance();  //Will be used instead(I assume)
        MqttJavaClient mqttJavaClient = MqttJavaClient.getInstance();
    
        for (int i = 0; i < 2; i++) {
            mqttJavaClient.Publish("AquaCare/topic", "Ayo", 1);
        }
        
        for (int i = 0; i < 1; i++) {
            mqttJavaClient.Publish("AquaCare/topic", "Ayo", 1);
        }

        System.out.println(db.QueryDatabase("5d", "Temperature", "value",
        "3"));


    }
}
