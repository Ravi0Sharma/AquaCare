package backend;

//import java.backend.ApplicationInterface;

public class Main {
 
    public static void main(String[] args) {
        //Initialize the database and mqtt clients
        InfluxDBJavaClient.getInstance();
        MqttJavaClient.getInstance();


        ApplicationInterface applicationInterface = new ApplicationInterface();

        for (int i = 0; i < 10; i++) {
            applicationInterface.Publish("AquaCare/test/test",String.valueOf(i+ 120), 0);
        }



        applicationInterface.Publish("AquaCare/test/test", "123", 0);
        applicationInterface.MeanOfDuration("test", "30d", "test");
        applicationInterface.QueryOfDuration("test", "30d", "test");
    }
}
