package backend;

//import java.backend.ApplicationInterface;

public class Main {
 
    public static void main(String[] args) {
        //Initialize the database and mqtt clients
        InfluxDBJavaClient.getInstance();
        MqttJavaClient.getInstance();
    }
}
