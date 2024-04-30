package ui.aquacare;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

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


    /*//! Lets set active fish manually for now
    //! We def need to add a way to switch the fishes
    //! At least based on how the dashboard is designed

    private String activeFishMonitor;

    //! Two choices:
    //! 1. Update all charts at once after selecting a fish
    //! 2. Update each chart separately after selecting and selecting a chart( one could even say they update after selecting a chart :-> )

    private void updateAllCharts() {
        //! Should be triggered after a fish change

        //Query data based om fish

        //Create data series for the line chart

        //Add data to the data series


        //Update the line chart

        //Do this 4 times???
        updateChart(linechartTemp, "Temperature");
        updateChart(linechartLight, "Light");
        updateChart(linechartPh, "PhLevel");
        updateChart(linechartDisp, "Dispenser");


        //Then create a method for it and call it 4 times for each chart


    }
    private void updateChart(LineChart chart, String sensorName) {
        //! A week of data is hardcoded for now
        //! Changing the duration or exporting the full duration would be good.
        //! Online db can only hold 30 days of data

        //Create data series for the line chart
        XYChart.Series series = new XYChart.Series();

        //Query data based on fish
        List<FluxTable> tables = applicationInterface.QueryOfDuration(activeFishMonitor, "1w", sensorName);

        //Divide tables into individual tables
        for (FluxTable table : tables) {
            //Get the records from the table
            List<FluxRecord> records = table.getRecords();

            for (FluxRecord fluxRecord : records) {
                //Save date and value to the data series
                series.getData().add(new XYChart.Data(fluxRecord.getTime(), fluxRecord.getValue()));
            }
        }

        //Update the line chart with milked values
        chart.getData().add(series);
    }

*/

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
