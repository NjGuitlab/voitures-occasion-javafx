package com.cours;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainFx extends Application {

    @Override
    public void start(Stage stage) {

        stage.setTitle("Voitures d'occasion");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}