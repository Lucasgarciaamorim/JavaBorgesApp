package com.borgesnotas.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class BorgesAppGuiApplication extends Application {

    @Override
    public void start(Stage stage) {
        Scene splashScene = createSplashScene(stage);

        stage.setScene(splashScene);

        stage.show();
    }

    private Scene createSplashScene(Stage stage) {
        Image logo = new Image(Objects.requireNonNull(getClass().getResource("/images/logo.jpeg")).toExternalForm());

        ImageView logoView = new ImageView(logo);
        StackPane root = new StackPane();
        root.getChildren().addAll(logoView);
        Scene splashScene = new Scene(root, 400, 200);


        Task<Void> initializationTask = new Task<Void>() {
            @Override
            protected Void call() {


                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        };

        initializationTask.setOnSucceeded(event -> {

            Platform.runLater(() -> loadMainScene(stage));
        });

        new Thread(initializationTask).start();

        return splashScene;
    }

    private void loadMainScene(Stage stage) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(BorgesAppGuiApplication.class.getResource("BorgesAppGui-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());


            stage.setScene(scene);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.setTitle("Envio de Nota Fiscal");


            stage.setOnCloseRequest(e -> {
                if (BorgesAppGuiController.onCloseQuery()) {
                    System.exit(0);
                } else {
                    e.consume();
                }
            });
        } catch (IOException e) {
            // Lidar com exceções, se necessário
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}