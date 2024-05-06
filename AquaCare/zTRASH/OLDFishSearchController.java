


package ui.aquacare;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FishSearchController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TableView<FishSearchModel> fishTableView;
    @FXML
    private TableColumn<FishSearchModel, String> fNameTableColumn;
    @FXML
    private TableColumn<FishSearchModel, String> fSpeciesTableColumn;
    @FXML
    private TableColumn<FishSearchModel, String> fFavFoodTableColumn;
    @FXML
    private TableColumn<FishSearchModel, String> fURLTableColumn;
    @FXML
    private TableColumn<FishSearchModel, String> fPhTableColumn;
    @FXML
    private TableColumn<FishSearchModel, String> fTempTableColumn;
    @FXML
    private TableColumn<FishSearchModel, String> fLightTableColumn;
    @FXML
    private TextField searchBarSP;

ObservableList<FishSearchModel> fishSearchModelObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        FishDBManager connectNow = new FishDBManager();
        Connection connectDB = connectNow.getDBConnection();

        String fishViewQuery = "SELECT fishName, fishSpecies, favoriteFood, fishURL, fishPh, fishTemp, fishLight FROM fish";

        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(fishViewQuery);

            while (queryOutput.next()){
                String queryfName = queryOutput.getString("fishName");
                String queryfSpecies = queryOutput.getString("fishSpecies");
                String queryfFavFood = queryOutput.getString("favoriteFood");
                String queryfURL = queryOutput.getString("fishURL");
                String queryfPh = queryOutput.getString("fishPh");
                String queryfTemp = queryOutput.getString("fishTemp");
                String queryfLight = queryOutput.getString("fishLight");

                // popluate observable list
                fishSearchModelObservableList.add(new FishSearchModel(queryfName,queryfSpecies, queryfFavFood, queryfURL, queryfPh, queryfTemp, queryfLight));
            }

            fNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("fishName"));
            fSpeciesTableColumn.setCellValueFactory(new PropertyValueFactory<>("fishSpecies"));
            fFavFoodTableColumn.setCellValueFactory(new PropertyValueFactory<>("favoriteFood"));
            fURLTableColumn.setCellValueFactory(new PropertyValueFactory<>("fishURL"));
            fPhTableColumn.setCellValueFactory(new PropertyValueFactory<>("fishPh"));
            fTempTableColumn.setCellValueFactory(new PropertyValueFactory<>("fishTemp"));
            fLightTableColumn.setCellValueFactory(new PropertyValueFactory<>("fishLight"));

            fishTableView.setItems(fishSearchModelObservableList);


            FilteredList<FishSearchModel> filteredFList = new FilteredList<>(fishSearchModelObservableList, x -> true);
            searchBarSP.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredFList.setPredicate(fishSearchModel -> {

                    //  if no input is entered, no changes in the list
                    if(newValue.isEmpty() || newValue.isBlank() || newValue == null) {return true;}

                    //  to simplify search turn input to lowercase
                    String searchKey = newValue.toLowerCase();

                    //  return true if fish name or species matches the search
                    if(fishSearchModel.getFishName().toLowerCase().indexOf(searchKey)>-1){
                        return true;
                    } else if(fishSearchModel.getFishSpecies().toLowerCase().indexOf(searchKey)>-1) {
                        return true;
                    } else
                        return false;
                });
            });


            SortedList<FishSearchModel> sortedFList = new SortedList<>(filteredFList);

            //  bind sorted list to the table view
            sortedFList.comparatorProperty().bind(fishTableView.comparatorProperty());

            //  with this filtered/sorted data visible in the table view
            fishTableView.setItems(sortedFList);


        }catch (SQLException e){
            Logger.getLogger(FishSearchController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }


    }
}