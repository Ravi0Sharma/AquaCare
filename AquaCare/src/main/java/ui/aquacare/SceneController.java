package ui.aquacare;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;

public class SceneController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private LineChart<String, Number> linechartPh;
    @FXML
    private LineChart<String, Number> linechartTemp;
    @FXML
    private LineChart<String, Number> linechartLight;
    @FXML
    private LineChart<String, Number> linechartDisp;

    @FXML
    private Button homeButton;
    @FXML
    private Button searchFishButton;
    @FXML
    private Button buttonLeft3;
    @FXML
    private Button buttonLeft4;
    @FXML
    private Button exitButton;

    @FXML
    private Button phButton;
    @FXML
    private Button feedButton;
    @FXML
    private Button lightButton;
    @FXML
    private Button tempButton;

    @FXML
    private Button buttonRight;
    @FXML
    private Button buttonRight1;
    @FXML
    private Button buttonRight2;

    @FXML
    private TextField textFieldLight;
    @FXML
    private Text lcTextPh;
    @FXML
    private Text lcTextLight;
    @FXML
    private Text lcTextTemp;
    @FXML
    private Text lcTextFood;


    //  will be added when some solution is found for switching pages (not scenes)
//    @FXML
//    private void clickPh() {
//        linechartPh.setVisible(true);
//        linechartDisp.setVisible(false);
//    }
//    @FXML
//    private void bringFront(ActionEvent event) {
//        anchorPane.toFront(true);
//    }

    @FXML
    private void quit(){
        exitButton.setOnAction(event -> {
            Platform.exit();
        });
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
    public void goSearchPage2(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("fish-search.fxml"));
        stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goPhPage(ActionEvent event) throws IOException {
        goToPage("ph-scene.fxml", event);
    }

    //this is now the default homepage
//    @FXML
//    private void goLightPage(ActionEvent event) throws IOException {
//        goToPage("lightScene.fxml", event);
//    }

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
