module com.borgesnotas.view {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.api.client.auth;
    requires com.google.api.client.extensions.java6.auth;
    requires com.google.api.client.extensions.jetty.auth;
    requires google.api.client;
    requires com.google.api.client;
    requires com.google.api.client.json.gson;
    requires com.google.api.services.sheets;
    requires org.controlsfx.controls;


    opens com.borgesnotas to javafx.fxml;
    exports com.borgesnotas;
    exports com.borgesnotas.view;
    exports com.borgesnotas.controller;
    opens com.borgesnotas.view to javafx.fxml;
}