package com.example.mypuzzlefx.gameLogic;

import com.example.mypuzzlefx.data.DataInImageViewArray;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;


public class LoadingThePlayingField {
    Stage stageForPlayingField = new Stage();
    DataInImageViewArray imageViewArray = new DataInImageViewArray();
    ImageView[] imageViewsFirst = imageViewArray.getImageViewOne();
    ImageView[] imageViewsSecond = imageViewArray.getImageViewTwo();
    AnchorPane anchorPane = new AnchorPane();
    private MenuBar menuBar = new MenuBar();
    private Menu menu = new Menu("Menu");
    private MenuItem restartItem = new MenuItem("Restart");
    private MenuItem autoSolveItem = new MenuItem("AutoSolve");
    private MenuItem exitItem = new MenuItem("Exit");

    private static final double TITLE_BAR_HEIGHT = 70;
    private static final int ARRAY_SIZE_TEN = 10;
    private static final int ARRAY_SIZE_FIVE = 5;

    private Screen screen = Screen.getPrimary();

    private double screenWidth = screen.getBounds().getWidth();
    private double screenHeight = screen.getBounds().getHeight();
    List<ImageView> generalArray;


    public void getPlayingField() {

        createMenuBar();
        startAllButtons();
        generalArray = getGeneralArrayWithImageView();

        anchorPane.getChildren().addAll(menuBar);
        anchorPane.getChildren().addAll(1,generalArray);

        Scene scene = new Scene(anchorPane, screenWidth, screenHeight - TITLE_BAR_HEIGHT);
        stageForPlayingField.initStyle(StageStyle.DECORATED);
        stageForPlayingField.setTitle("Game window");
        stageForPlayingField.setScene(scene);
        stageForPlayingField.show();

        PuzzleLogic puzzleLogic = new PuzzleLogic(generalArray, anchorPane, menuBar);
        puzzleLogic.getPuzzleLogic();

    }

    private List<ImageView> getGeneralArrayWithImageView() {
        List<ImageView> generalResult = new ArrayList<>();
        ImageView[] result1 = createLeftBlock(imageViewsFirst);
        ImageView[] result2 = createRightBlock(imageViewsSecond);
        for (int i = 0; i < ARRAY_SIZE_TEN; i++) {
            generalResult.add(result1[i]);
            generalResult.add(result2[i]);
        }

        return generalResult;
    }

    private ImageView[] createLeftBlock(ImageView[] firstArray) {
        ImageView[] firstArrayResult = new ImageView[ARRAY_SIZE_TEN];
        ImageView imageView;
        double heightImage = 0;
        double weight = firstArray[0].getFitWidth();
        for (int i = 0; i < ARRAY_SIZE_FIVE; i++) {

            imageView = firstArray[i];
            imageView.setLayoutX(10);
            imageView.setLayoutY(10 + heightImage);
            firstArrayResult[i] = imageView;
            heightImage += imageView.getFitHeight();
        }
        heightImage = 0;
        for (int i = 5; i < ARRAY_SIZE_TEN; i++) {

            imageView = firstArray[i];
            imageView.setLayoutX(10 + weight);
            imageView.setLayoutY(10 + heightImage);
            firstArrayResult[i] = imageView;
            heightImage += imageView.getFitHeight();
        }


        return firstArrayResult;
    }

    private ImageView[] createRightBlock(ImageView[] firstArray) {
        ImageView[] secondArrayResult = new ImageView[ARRAY_SIZE_TEN];
        ImageView imageView;
        double heightImage = 0;
        double widthImage = firstArray[0].getFitWidth();
        for (int i = 0; i < ARRAY_SIZE_FIVE; i++) {

            imageView = firstArray[i];
            imageView.setLayoutX(screenWidth - 10 - widthImage);
            imageView.setLayoutY(10 + heightImage);
            secondArrayResult[i] = imageView;
            heightImage += imageView.getFitHeight();
        }
        heightImage = 0;
        widthImage *= 2;
        for (int i = 5; i < ARRAY_SIZE_TEN; i++) {

            imageView = firstArray[i];
            imageView.setLayoutX(screenWidth - 10 - widthImage);
            imageView.setLayoutY(10 + heightImage);
            secondArrayResult[i] = imageView;
            heightImage += imageView.getFitHeight();
        }


        return secondArrayResult;
    }

    private void createMenuBar() {
        menuBar.setPrefHeight(30);
        menuBar.setPrefWidth(65);

        AnchorPane.setLeftAnchor(menuBar, (screenWidth / 2 - menuBar.getWidth() / 2));
        AnchorPane.setBottomAnchor(menuBar, 10.0);

        menu.getItems().addAll(restartItem, autoSolveItem, exitItem);
        menuBar.getMenus().add(menu);
    }

    private void startAllButtons() {
        restartItem.setOnAction(event -> onRestartButtonClick());
        autoSolveItem.setOnAction(event -> onAutoSolveButtonClick());
        exitItem.setOnAction(event -> onExitButtonClick());
    }

    private void onRestartButtonClick() {
        System.out.println(" Метод не реалізован");
    }

    private void onAutoSolveButtonClick() {
        System.out.println(" Метод не реалізован");
    }

    private void onExitButtonClick() {
        stageForPlayingField.close();
    }
}

