package com.example.mypuzzlefx;

import com.example.mypuzzlefx.gameLogic.LoadingThePlayingField;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static final String WELCOME_LABEL = "Welcome to the puzzle game!";
    private static final String BUTTON_START = "Start";
    private static final String WINDOW_TITLE = "Start window";
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private static final int HIGH_WELCOME_LABEL = 20;
    private static final double SET_TOP_WELCOME_LABEL = 150;
    private static final double SET_LEFT_WELCOME_LABEL = 50;
    private static final double SET_TOP_BUTTON_START = 200;
    private static final double SET_LEFT_BUTTON_START = 170;

    LoadingThePlayingField playingField = new LoadingThePlayingField();
    AnchorPane anchorPane = new AnchorPane();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {


        Label welcomeLabel = new Label(WELCOME_LABEL);
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, HIGH_WELCOME_LABEL));
        anchorPane.getChildren().add(welcomeLabel);

        AnchorPane.setTopAnchor(welcomeLabel, SET_TOP_WELCOME_LABEL);
        AnchorPane.setLeftAnchor(welcomeLabel, SET_LEFT_WELCOME_LABEL);


        Button startGameButton = new Button(BUTTON_START);

        startGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                playingField.getPlayingField();
                stage.close();
            }
        });

        anchorPane.getChildren().add(startGameButton);
        AnchorPane.setTopAnchor(startGameButton, SET_TOP_BUTTON_START);
        AnchorPane.setLeftAnchor(startGameButton, SET_LEFT_BUTTON_START);

        Scene scene = new Scene(anchorPane, WIDTH, HEIGHT);
        stage.setTitle(WINDOW_TITLE);
        stage.setScene(scene);
        stage.show();
    }

}

