//package com.example.aq5;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.collections.transformation.FilteredList;
//import javafx.collections.transformation.SortedList;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.scene.Node;
//import javafx.scene.Scene;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.TextField;
//import javafx.scene.control.cell.PropertyValueFactory;
//
//import java.io.FileReader;
//import java.io.IOException;
//import java.net.URL;
//import java.util.ResourceBundle;
//
//import javafx.stage.Stage;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//
//public class FishSearchController2 implements Initializable {
//
//    @FXML
//    private TableView<FishSearchModel> fishTableView;
//    @FXML
//    private TableColumn<FishSearchModel, String> fNameTableColumn;
//    @FXML
//    private TableColumn<FishSearchModel, String> fSpeciesTableColumn;
//    @FXML
//    private TableColumn<FishSearchModel, String> fFavFoodTableColumn;
//    @FXML
//    private TableColumn<FishSearchModel, String> fURLTableColumn;
//    @FXML
//    private TableColumn<FishSearchModel, String> fPhTableColumn;
//    @FXML
//    private TableColumn<FishSearchModel, String> fTempTableColumn;
//    @FXML
//    private TableColumn<FishSearchModel, String> fLightTableColumn;
//    @FXML
//    private TextField searchBarSP;
//
//    ObservableList<FishSearchModel> fishSearchModelObservableList = FXCollections.observableArrayList();
//
//    @Override
//    public void initialize(URL url, ResourceBundle resource) {
//        loadFishDataFromJson();
//
//        fNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("fishName"));
//        fSpeciesTableColumn.setCellValueFactory(new PropertyValueFactory<>("fishSpecies"));
//        fFavFoodTableColumn.setCellValueFactory(new PropertyValueFactory<>("favoriteFood"));
//        fURLTableColumn.setCellValueFactory(new PropertyValueFactory<>("fishURL"));
//        fPhTableColumn.setCellValueFactory(new PropertyValueFactory<>("fishPh"));
//        fTempTableColumn.setCellValueFactory(new PropertyValueFactory<>("fishTemp"));
//        fLightTableColumn.setCellValueFactory(new PropertyValueFactory<>("fishLight"));
//
//        FilteredList<FishSearchModel> filteredFList = new FilteredList<>(fishSearchModelObservableList, x -> true);
//        searchBarSP.textProperty().addListener((observable, oldValue, newValue) -> {
//            filteredFList.setPredicate(fishSearchModel -> {
//                if(newValue.isEmpty() || newValue.isBlank()) {
//                    return true;
//                }
//                String searchKey = newValue.toLowerCase();
//                return fishSearchModel.getFishName().toLowerCase().contains(searchKey) ||
//                        fishSearchModel.getFishSpecies().toLowerCase().contains(searchKey);
//            });
//        });
//
//        SortedList<FishSearchModel> sortedFList = new SortedList<>(filteredFList);
//        sortedFList.comparatorProperty().bind(fishTableView.comparatorProperty());
//        fishTableView.setItems(sortedFList);
//    }
//
//    private void loadFishDataFromJson() {
//
//        JSONParser parser = new JSONParser();
//
//        try (FileReader reader = new FileReader("fish.json")) {
//            Object obj = parser.parse(reader);
//            JSONArray fishList = (JSONArray) obj;
//
//            for (Object fishObj : fishList) {
//                JSONObject fish = (JSONObject) fishObj;
//
//                String fName = (String) fish.get("fishName");
//                String fSpecies = (String) fish.get("fishSpecies");
//                String fFavFood = (String) fish.get("favoriteFood");
//                String fURL = (String) fish.get("fishURL");
//                String fPh = (String) fish.get("fishPh");
//                String fTemp = (String) fish.get("fishTemp");
//                String fLight = (String) fish.get("fishLight");
//
//                fishSearchModelObservableList.add(new FishSearchModel(fName, fSpecies, fFavFood, fURL, fPh, fTemp, fLight));
//            }
//        } catch (IOException | ParseException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @FXML
//    public void goHomePage2(ActionEvent event) throws IOException {
//        root = FXMLLoader.load(getClass().getResource("home-page3.fxml"));
//        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }}
