package ui.aquacare;

import javafx.application.Application;
import ui.utilities.MqttJavaClient;
import ui.utilities.InfluxDBJavaClient;


public class Main {

    public static void main(String[] args) {
        InfluxDBJavaClient.getInstance();
        MqttJavaClient.getInstance();
        Application.launch(GraphicalInterface.class, args);
    }
}
