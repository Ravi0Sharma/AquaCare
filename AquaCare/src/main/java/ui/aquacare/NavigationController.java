package ui.aquacare;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NavigationController {

    // TODO: close influxdb client for real-time readings when scenes switched
    //          public void close() {
    //        if (databaseClient != null) {
    //            databaseClient.close();
    //        }
    //        ***


    //  General
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Button exitButton;
    @FXML
    private Text dateHP;

    public void setDate(){
        // TODO: The date should be able to change while the app is still running.
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter theFormat = DateTimeFormatter.ofPattern("d MMMM YYYY");
        String formattedDate = localDate.format(theFormat);
        dateHP.setText("" + formattedDate);
    }

    @FXML
    public void quit(){
        exitButton.setOnAction(event -> {
            Platform.exit();

        });
        // TODO: add new exit function to close sensor readings too.
        //  + should be able to quit with single click.
    }

    private void goToPage(String fxmlFileName, ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlFileName));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void goHomePage(ActionEvent event) throws IOException {
        goToPage("homepage.fxml", event);
    }
    @FXML
    public void goSearchPage(ActionEvent event) throws IOException {
        goToPage("fish-search2.fxml", event);
    }
    @FXML
    public void goPhPage(ActionEvent event) throws IOException {
        goToPage("ph-scene.fxml", event);
    }
    @FXML
    public void goFoodPage(ActionEvent event) throws IOException {
        goToPage("food-scene.fxml", event);
    }
    @FXML
    public void goTempPage(ActionEvent event) throws IOException {
        goToPage("temperature-scene.fxml", event);
    }
    @FXML
    public void goConfigPage(ActionEvent event) throws IOException {
        goToPage("configRanges.fxml", event);
    }
    @FXML
    public void goSettings(ActionEvent event) throws IOException {
        goToPage("settings.fxml", event);
    }
}
