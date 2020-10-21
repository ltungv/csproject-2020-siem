package vn.edu.vgu.jupiter.dashboard;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Dashboard extends Application {

    public static final double HEIGHT = 300;
    public static final double WIDTH = 400;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        var root = new VBox();
        root.setAlignment(Pos.CENTER);

        primaryStage.setTitle("SIEM Dashboard");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.show();
    }

}


