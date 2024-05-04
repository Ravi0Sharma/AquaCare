package ui.aquacare;

import com.fazecast.jSerialComm.SerialPort;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.util.Scanner;

/**
 * second, as a backup in case the first code won't work
 * without testing you never
 */

public class SceneController2 {

    @FXML
    private ComboBox<String> portList;
    @FXML
    private Button connectButton;
    @FXML
    private Label phLabel;
    @FXML
    private Label tempLabel;
    @FXML
    private Label lightLabel;
    @FXML
    private Label feedLabel;
    @FXML
    private VBox roots;

    private static SerialPort chosenPort;

    public void initialize() {
        // populate the box with available serial ports
        SerialPort[] portNames = SerialPort.getCommPorts();
        for (SerialPort port : portNames) {
            portList.getItems().add(port.getSystemPortName());
        }

        connectButton.setOnAction(event -> {
            if (connectButton.getText().equals("Connect")) {

                chosenPort = SerialPort.getCommPort(portList.getValue());
                chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);

                if (chosenPort.openPort()) {
                    connectButton.setText("Disconnect");
                    portList.setDisable(true);
                }

                // thread to listen to incoming data from the serial port
                Thread thread = new Thread(() -> {
                    Scanner scanner = new Scanner(chosenPort.getInputStream());
                    while (scanner.hasNextLine()) {
                        try {
                            String line = scanner.nextLine();

                            // we will use prefixes as "'temperature': 21" and "'pH': 8". So to display the data at the right we split by :
                            String[] parts = line.split(":");

                            if (parts.length == 2) {
                                String sensorType = parts[0].trim();
                                String sensorValue = (parts[1].trim());
                                // Update the appropriate label with the received sensor data
                                switch (sensorType) {
                                    case "pH":
                                        phLabel.setText("pH: "+ sensorValue);
                                        break;
                                    case "Temperature":
                                        tempLabel.setText("Temperature: "+ sensorValue);
                                        break;
                                    case "Light":
                                        lightLabel.setText("Light: "+sensorValue);
                                        break;
//                                    case "Dispenser":
//                                        feedLabel.setText("Last Fed: "+sensorValue);
//                                        break;
                                    default:
                                        System.out.println("Unknown sensor!");
                                        break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    scanner.close();
                });
                thread.start();
            } else {
                chosenPort.closePort();
                portList.setDisable(false);
                connectButton.setText("Connect");

                phLabel.setText("");
                tempLabel.setText("");
                lightLabel.setText("");
//                feedLabel.setText("");
            }
        });
    }
}

