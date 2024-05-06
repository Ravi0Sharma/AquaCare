package ui.aquacare;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.Scanner;


public class SceneController implements Initializable {

//  General
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Button exitButton;
    @FXML
    private Text dateHP;

//  Real time data
    static SerialPort chosenPort;
    static int x = 0;   //
    @FXML
    private static ComboBox<String> portList;
    @FXML
    private static Button connectButton;
    @FXML
    private VBox roots;
    //  Dashboard Cards
    @FXML
    private Label phLabel;
    @FXML
    private Label lightLabel;
    @FXML
    private Label tempLabel;
    @FXML
    private Label feedLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        setDate();
        realTimeData();
    }

    // This method is useless if you start to use MQTT for RT display. because data will be processed in that class.
    private String[] processData(String rawData) {
        // we will use prefixes as "'temperature' : 21" and "'pH':8". to get the data at the right we split by :
            String[] parts = rawData.split(":");
            if (parts.length != 2) {    // the data should only have the type and the value (for RT)
                return new String[]{"", ""};
            }
            // Extract sensor type and value
            String sensorType = parts[0].trim();
            String sensorValue = parts[1].trim();
            return new String[]{sensorType, sensorValue};
        }

    private String readDataFromSerialPort() {
        byte[] buffer = new byte[1024]; // holds the data received from the serial port
        int numBytes = chosenPort.readBytes(buffer, buffer.length);
        return new String(buffer, 0, numBytes); //convert byte array buffer to string starting from index 0
    }

    public void realTimeData(){
        portList = new ComboBox<>();
        connectButton = new Button("Connect");
        roots = new VBox(portList, connectButton);

        // populate the box with available port names
        SerialPort[] portNames = SerialPort.getCommPorts();
        for (SerialPort port : portNames) {
            portList.getItems().add(chosenPort.getSystemPortName());
        }

        connectButton.setOnAction(event -> {
            if (connectButton.getText().equals("Connect")) {
                // attempt to connect to the serial port
                chosenPort = SerialPort.getCommPort(portList.getValue().toString());
                chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);


                if (chosenPort.openPort()) {
                    connectButton.setText("Disconnect");
                    portList.setDisable(true);
                }

                // create a new thread that listens for incoming text and populates the graph
                Thread thread = new Thread(() -> {
                    while (true) {

                        String data = readDataFromSerialPort();
                        String[] sensorData = processData(data);

                        Platform.runLater(() -> {
                            // update labels based on sensor type
                            if (sensorData[0].equals("pH")) {
                                phLabel.setText("pH: " + sensorData[1]);
                            } else if (sensorData[0].equals("Temperature")) {
                                tempLabel.setText("Temperature: " + sensorData[1]);
                            } else if (sensorData[0].equals("Light")) {
                                lightLabel.setText("Light: " + sensorData[1]);
                            }
                        });

                        // sleep to avoid reduce app's CPU usage
                        try {
                            Thread.sleep(1000); // 1000 = 1 sec
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();


            } else {
                // disconnect from the serial port
                chosenPort.closePort();
                portList.setDisable(false);
                connectButton.setText("Connect");
                x = 0;
            }
        });
    }

    public void setDate(){
        // TODO: The date should be able to change while the app is still running.
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter theFormat = DateTimeFormatter.ofPattern("d MMMM YYYY");
        String formattedDate = localDate.format(theFormat);
        dateHP.setText("" + formattedDate);
    }

    @FXML
    private void quit(){
        exitButton.setOnAction(event -> {
            Platform.exit();
        });
        // TODO: add new exit function to close sensor readings too.
    }

    private void goToPage(String fxmlFileName, ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlFileName));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goHomePage(ActionEvent event) throws IOException {
        goToPage("homepage.fxml", event);
    }
    @FXML
    private void goSearchPage(ActionEvent event) throws IOException {
        goToPage("fish-search.fxml", event);
    }
    @FXML
    private void goPhPage(ActionEvent event) throws IOException {
        goToPage("ph-scene.fxml", event);
    }
    @FXML
    private void goFoodPage(ActionEvent event) throws IOException {
        goToPage("food-scene.fxml", event);
    }
    @FXML
    private void goTempPage(ActionEvent event) throws IOException {
        goToPage("temperature-scene.fxml", event);
    }
    @FXML
    private void goConfigPage(ActionEvent event) throws IOException {
        goToPage("configRanges.fxml", event);
    }
    @FXML
    private void goSettings(ActionEvent event) throws IOException {
        goToPage("settings.fxml", event);
    }

}
