package ui.aquacare;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import javafx.application.Application;

public class GraphicalInterface extends Application{


    @Override
    public void start(Stage stage) throws IOException {

        stage.initStyle(StageStyle.UNDECORATED);
//        stage.initStyle(StageStyle.UTILITY);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("homepage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),1280, 768);

//        scene.getStylesheets().add(getClass().getResource("stage.css").toExternalForm());
        stage.setTitle("Aquarium Care");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
