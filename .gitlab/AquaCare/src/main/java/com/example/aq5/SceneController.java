package com.example.aq5;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;

public class SceneController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    private void goToPage(String fxmlFileName, ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlFileName));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goHomePage(ActionEvent event) throws IOException {
        goToPage("home-page3.fxml", event);
    }

    @FXML
    private void goSearchPage(ActionEvent event) throws IOException {
        goToPage("fishsearch.fxml", event);
    }

    @FXML
    public void goSearchPage2(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("fishsearch.fxml"));
        stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goPhPage(ActionEvent event) throws IOException {
        goToPage("phScene.fxml", event);
    }


    @FXML
    private void goLightPage(ActionEvent event) throws IOException {
        goToPage("lightScene.fxml", event);
    }

    @FXML
    private void goFoodPage(ActionEvent event) throws IOException {
        goToPage("foodScene.fxml", event);
    }

    @FXML
    private void goTempPage(ActionEvent event) throws IOException {
        goToPage("temperatureScene.fxml", event);
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
