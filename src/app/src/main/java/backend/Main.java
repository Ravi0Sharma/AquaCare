package backend;

//import java.backend.ApplicationInterface;

public class Main {
 
    public static void main(String[] args) {
        ApplicationInterface app = new ApplicationInterface();
        app.Publish("AquaCare/topic", "Ayo", 1);

        
        
        for (int i = 0; i < 10; i++) {
            app.Publish("AquaCare/topic", "Ayo", 1);
        }

        for (int i = 0; i < 10; i++) {
            //app.WriteSensorData("Temperature", "deviceID", "2", "value", 25.0 + i * 5);
        }

        app.QueryOfDuration("Temparature", "15m", "2");


    }


}
