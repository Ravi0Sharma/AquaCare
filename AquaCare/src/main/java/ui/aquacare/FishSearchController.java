package ui.aquacare;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class FishSearchController implements Initializable {
    @FXML
    private Pane fishContainer;
    @FXML
    private Label fishName;
    @FXML
    private Label fishSpecies;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        JSONArray fishData = parseJSONFile("/ui/aquacare/fish.json");

        for (Object obj : fishData) {
            JSONObject fishObj = (JSONObject) obj;
            Fish fish = new Fish(
                    (String) fishObj.get("Name"),
                    (String) fishObj.get("Species"),
                    (String) fishObj.get("URL")
            );
            Pane fishPane = null;
            try {
                fishPane = createFishPane(fish);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            fishContainer.getChildren().add(fishPane);
        }
    }

    private JSONArray parseJSONFile(String filename) {
        JSONParser parser = new JSONParser();
        try {
            // Load the JSON file from the resources directory
            InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("fish.json"));
            Object obj = parser.parse(reader);
            return (JSONArray) obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Pane createFishPane(Fish fish) throws IOException {
        // Load FXML file for a single fish pane
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fish-search2.fxml"));
        Pane fishPane = loader.load();

        // Access elements in the fish pane
        Label fishNameLabel = (Label) fishPane.lookup("#fishNameLabel");
        Label fishSpeciesLabel = (Label) fishPane.lookup("#fishSpeciesLabel");
//        ImageView fishImageView = (ImageView) fishPane.lookup("#fishImageView");

        // Populate elements with fish information
        fishNameLabel.setText(fish.getFishName());
        fishSpeciesLabel.setText(fish.getFishSpecies());
//        fishImageView.setImage(new Image(fish.getFishURL()));

        return fishPane;
    }

// TODO: do we need to create and interface or an abstract class to avoid redundancy?

    public void goSearchPage(ActionEvent actionEvent) {
    }

    public void goConfigPage(ActionEvent actionEvent) {
    }

    public void quit(ActionEvent actionEvent) {
    }

    public void goSettings(ActionEvent actionEvent) {
    }

    public void goHomePage(ActionEvent actionEvent) {
    }
}
