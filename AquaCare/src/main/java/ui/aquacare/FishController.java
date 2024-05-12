package ui.aquacare;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.influxdb.client.InfluxDBClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import ui.utilities.NotificationClient;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class FishController extends NavigationController implements Initializable {

    @FXML
    private TableView<FishModel> fishTableView;
    @FXML
    private TableColumn<FishModel, String> fNameTableColumn;
    @FXML
    private TableColumn<FishModel, String> fSpeciesTableColumn;
    @FXML
    private TableColumn<FishModel, String> fFavFoodTableColumn;
    @FXML
    private TableColumn<FishModel, String> fURLTableColumn;
    @FXML
    private TableColumn<FishModel, String> fPhTableColumn;
    @FXML
    private TableColumn<FishModel, String> fTempTableColumn;
    @FXML
    private TableColumn<FishModel, String> fLightTableColumn;
    @FXML
    private TextField searchBarSP;

    private final ObservableList<FishModel> fishList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try (InputStream inputStream = getClass().getResourceAsStream("/ui/aquacare/fish.json");
             InputStreamReader streamReader = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(streamReader)) {

            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            Gson gson = new Gson();
            FishModel[] fishArray = gson.fromJson(jsonContent.toString(), FishModel[].class);
            fishList.addAll(fishArray);

            fNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("fishName"));
            fSpeciesTableColumn.setCellValueFactory(new PropertyValueFactory<>("fishSpecies"));
            fFavFoodTableColumn.setCellValueFactory(new PropertyValueFactory<>("favoriteFood"));
            fURLTableColumn.setCellValueFactory(new PropertyValueFactory<>("fishURL"));
            fPhTableColumn.setCellValueFactory(new PropertyValueFactory<>("fishPh"));
            fTempTableColumn.setCellValueFactory(new PropertyValueFactory<>("fishTemp"));
            fLightTableColumn.setCellValueFactory(new PropertyValueFactory<>("fishLight"));

            fishTableView.setItems(fishList);

            // Search Bar
            FilteredList<FishModel> filteredData = new FilteredList<>(fishList, p -> true);
            searchBarSP.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(fish -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    return fish.getFishName().toLowerCase().contains(lowerCaseFilter) ||
                            fish.getFishSpecies().toLowerCase().contains(lowerCaseFilter);
                });
            });

            fishTableView.setOnMouseClicked( event -> {
                if( event.getClickCount() == 2 ) {
                    NotificationClient.displayTray("New fish selected", "Fish: " + fishTableView.getSelectionModel().getSelectedItem().getFishName() + " selected.");
                }});


            SortedList<FishModel> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(fishTableView.comparatorProperty());
            fishTableView.setItems(sortedData);

        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

}
