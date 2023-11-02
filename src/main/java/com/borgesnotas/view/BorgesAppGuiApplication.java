package com.borgesnotas.view;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BorgesAppGuiApplication extends Application {

    @Override

    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BorgesAppGuiApplication.class.getResource("BorgesAppGui-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setTitle("Envio de Nota Fiscal ");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(e -> {
            if (BorgesAppGuiController.onCloseQuery()) {
                System.exit(0);
            } else {
                e.consume();
            }
        });
    }

    public static void main(String[] args) {


        launch();
    }

}