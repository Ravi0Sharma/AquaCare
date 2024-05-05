package ui.aquacare;

import ui.utilities.ApplicationInterface;
import ui.utilities.MqttJavaClient;
import ui.utilities.InfluxDBJavaClient;


public class Main {

    public static void main(String[] args) {
        InfluxDBJavaClient.getInstance();
        MqttJavaClient.getInstance();

        GraphicalInterface.main(args);
    }
}
