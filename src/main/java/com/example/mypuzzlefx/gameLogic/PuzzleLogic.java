package com.example.mypuzzlefx.gameLogic;

import javafx.scene.control.MenuBar;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

public class PuzzleLogic {

    private List<ImageView> puzzlePieces = new ArrayList<>();
    private Effect originalEffect;
    private double initialX;
    private double initialY;
    private static final int THRESHOLD = 88;
    private static final int DISTANCE_BETWEEN_PUZZLES = 10;
    private AnchorPane basicScreen;
    private MenuBar menuBar;
    private static final int HEIGHT_ONE_PUZZLE = 150;
    private static final int WIDTH_ONE_PUZZLE = 160;

    public PuzzleLogic(List<ImageView> puzzlePieces, AnchorPane anchorPane, MenuBar menuBar) {
        this.puzzlePieces = puzzlePieces;
        this.basicScreen = anchorPane;
        this.menuBar = menuBar;
    }

    public void getPuzzleLogic() throws IllegalArgumentException {
        for (ImageView imageView : puzzlePieces) {
            setDraggable(imageView);
            originalEffect = imageView.getEffect();
        }
        basicScreen.getChildren().clear();
        basicScreen.getChildren().addAll(menuBar);
        basicScreen.getChildren().addAll(1, puzzlePieces);
    }

    private void setDraggable(ImageView imageView) {
        imageView.setOnMousePressed(event -> onMousePressed(imageView, event));
        imageView.setOnMouseDragged(event -> onMouseDragged(imageView, event));
        imageView.setOnMouseReleased(event -> onMouseReleased(imageView, event));
    }

    private void onMousePressed(ImageView imageView, MouseEvent event) {
        initialX = event.getSceneX();
        initialY = event.getSceneY();
        imageView.toFront();
        Blend blend = new Blend();
        blend.setMode(BlendMode.MULTIPLY);
        imageView.setEffect(blend);
    }

    private void onMouseDragged(ImageView imageView, MouseEvent event) {
        double offsetX = event.getSceneX() - initialX;
        double offsetY = event.getSceneY() - initialY;

        imageView.setLayoutX(imageView.getLayoutX() + offsetX);
        imageView.setLayoutY(imageView.getLayoutY() + offsetY);

        initialX = event.getSceneX();
        initialY = event.getSceneY();
    }

    private void onMouseReleased(ImageView imageView1, MouseEvent event) {

        ImageView imageView2 = null;

        for (ImageView puzzle : puzzlePieces) {
            imageView2 = puzzle;

            if (Math.abs((imageView1.getLayoutX() + imageView1.getFitWidth()) - imageView2.getLayoutX())
                    < DISTANCE_BETWEEN_PUZZLES && (Math.abs(imageView1.getLayoutY() - imageView2.getLayoutY())
                    < DISTANCE_BETWEEN_PUZZLES) && imageView1 != imageView2) {
                compareAndGluePuzzlesWithLeftToRight(imageView1, imageView2, THRESHOLD);
                break;
            }
            if (Math.abs((imageView1.getLayoutY() + imageView1.getFitHeight()) - imageView2.getLayoutY())
                    < DISTANCE_BETWEEN_PUZZLES
                    && Math.abs(imageView1.getLayoutX() - imageView2.getLayoutX())
                    < DISTANCE_BETWEEN_PUZZLES && imageView1 != imageView2) {
                compareAndGlueWithUpToDown(imageView1, imageView2, THRESHOLD);
                break;
            }
            if (Math.abs((imageView2.getLayoutX() + imageView2.getFitWidth()) - imageView1.getLayoutX())
                    < DISTANCE_BETWEEN_PUZZLES &&
                    (Math.abs(imageView1.getLayoutY() - imageView2.getLayoutY())
                            < DISTANCE_BETWEEN_PUZZLES) && imageView1 != imageView2) {
                //compareAndGluePuzzlesWithRightToLeft(imageView1, imageView2, THRESHOLD);
                break;
            }
            if (Math.abs((imageView2.getLayoutY() + imageView2.getFitHeight()) - imageView1.getLayoutY())
                    < DISTANCE_BETWEEN_PUZZLES
                    && Math.abs(imageView1.getLayoutX() - imageView2.getLayoutX())
                    < DISTANCE_BETWEEN_PUZZLES && imageView1 != imageView2) {
                // compareAndGlueWithDownToUp(imageView1, imageView2, THRESHOLD);
                break;
            }
        }
        getPuzzleLogic();
    }

    private void compareAndGluePuzzlesWithLeftToRight(ImageView imageView1, ImageView imageView2, int threshold) {
        Image image1 = imageView1.getImage();
        Image image2 = imageView2.getImage();

        PixelReader reader1 = image1.getPixelReader();
        PixelReader reader2 = image2.getPixelReader();

        int width1 = (int) image1.getWidth();
        int height1 = (int) image1.getHeight();
        int width2 = (int) image2.getWidth();
        int height2 = (int) image2.getHeight();
        int heightMin = height1 < height2 ? height1 : height2;
        int heightMax = height1 > height2 ? height1 : height2;

        int totalDiff = 0;
        int minus = 0;
        for (int i = 1; i < 5; i++) {
            if (width1 / WIDTH_ONE_PUZZLE == i) {
                minus = i;
            }
        }
        for (int x1 = width1 - minus, x2 = 0; x1 < width1 && x2 < 1; x1++, x2++) {
            for (int y = 0; y < heightMin; y++) {
                int argb1 = reader1.getArgb(x1, y);
                int argb2 = reader2.getArgb(x2, y);
                int diffR = Math.abs((argb1 >> 16) & 0xFF - (argb2 >> 16) & 0xFF);
                int diffG = Math.abs((argb1 >> 8) & 0xFF - (argb2 >> 8) & 0xFF);
                int diffB = Math.abs(argb1 & 0xFF - argb2 & 0xFF);

                int alpha1 = (argb1 >> 24) & 0xFF;
                int alpha2 = (argb2 >> 24) & 0xFF;
                int diffAlpha = Math.abs(alpha1 - alpha2);
                totalDiff += diffR + diffG + diffB + diffAlpha;
            }
        }
        double averageDiff = totalDiff / heightMin;

        System.out.println(averageDiff);

        if (averageDiff > threshold) {
            System.out.println("false");

        } else {
            System.out.println("true");

            WritableImage combinedImage = new WritableImage(width1 + width2, heightMax);
            PixelWriter pixelWriter = combinedImage.getPixelWriter();

            PixelReader pixelReader1 = image1.getPixelReader();
            PixelReader pixelReader2 = image2.getPixelReader();
            int x1 = 0;
            for (x1 = 0; x1 < width1; x1++) {
                for (int y = 0; y < height1; y++) {

                    Color color1 = pixelReader1.getColor(x1, y);
                    pixelWriter.setColor(x1, y, color1);
                }

            }
            for (int x2 = 0; x2 < width2; x2++) {
                for (int y = 0; y < height2; y++) {

                    Color color2 = pixelReader2.getColor(x2, y);
                    pixelWriter.setColor(x1 + x2, y, color2);

                }

            }
            ImageView combinedImageView = new ImageView(combinedImage);

            combinedImageView.setLayoutX(imageView2.getLayoutX() - imageView1.getFitWidth());
            combinedImageView.setLayoutY(imageView2.getLayoutY());
            combinedImageView.setFitWidth(width1 + width2);
            combinedImageView.setFitHeight(heightMax);

            puzzlePieces.remove(imageView1);
            puzzlePieces.remove(imageView2);
            puzzlePieces.add(combinedImageView);

            Toolkit.getDefaultToolkit().beep();
        }
    }

    private void compareAndGlueWithUpToDown(ImageView imageView1, ImageView imageView2, int threshold) {
        Image image1 = imageView1.getImage();
        Image image2 = imageView2.getImage();

        PixelReader reader1 = image1.getPixelReader();
        PixelReader reader2 = image2.getPixelReader();

        int width1 = (int) image1.getWidth();
        int height1 = (int) image1.getHeight();
        int width2 = (int) image2.getWidth();
        int height2 = (int) image2.getHeight();
        int widthMin = width1 < width2 ? width1 : width2;
        int widthMax = width1 > width2 ? width1 : width2;
        int totalDiff = 0;

        int minus = 0;
        for (int i = 1; i < 4; i++) {
            if (height1 / HEIGHT_ONE_PUZZLE == i) {
                minus = i;
            }
        }

        for (int y1 = height1 - minus, y2 = 0; y1 < height1 && y2 < 1; y1++, y2++) {
            for (int x = 0; x < widthMin; x++) {
                int argb1 = reader1.getArgb(x, y1);
                int argb2 = reader2.getArgb(x, y2);

                int diffR = Math.abs((argb1 >> 16) & 0xFF - (argb2 >> 16) & 0xFF);
                int diffG = Math.abs((argb1 >> 8) & 0xFF - (argb2 >> 8) & 0xFF);
                int diffB = Math.abs(argb1 & 0xFF - argb2 & 0xFF);

                int alpha1 = (argb1 >> 24) & 0xFF;
                int alpha2 = (argb2 >> 24) & 0xFF;
                int diffAlpha = Math.abs(alpha1 - alpha2);
                totalDiff += diffR + diffG + diffB + diffAlpha;
            }
        }
        double averageDiff = totalDiff / widthMin;

        System.out.println(averageDiff);

        if (averageDiff > threshold) {
            System.out.println("false");

        } else {
            System.out.println("true");
            WritableImage combinedImage = new WritableImage(widthMax, height1 + height2);
            PixelWriter pixelWriter = combinedImage.getPixelWriter();

            PixelReader pixelReader1 = image1.getPixelReader();
            PixelReader pixelReader2 = image2.getPixelReader();
            int x1 = 0;
            for (x1 = 0; x1 < width1; x1++) {
                for (int y1 = 0; y1 < height1; y1++) {

                    Color color1 = pixelReader1.getColor(x1, y1);
                    pixelWriter.setColor(x1, y1, color1);
                }

            }
            for (int x2 = 0; x2 < width2; x2++) {
                for (int y2 = 0; y2 < height2; y2++) {

                    Color color2 = pixelReader2.getColor(x2, y2);
                    pixelWriter.setColor(x2, y2 + (height1 - 1), color2);

                }

            }
            ImageView combinedImageView = new ImageView(combinedImage);

            combinedImageView.setLayoutX(imageView1.getLayoutX());
            combinedImageView.setLayoutY(imageView2.getLayoutY() - imageView1.getFitHeight());
            combinedImageView.setFitHeight(height1 + height2);
            combinedImageView.setFitWidth(widthMax);

            puzzlePieces.remove(imageView1);
            puzzlePieces.remove(imageView2);
            puzzlePieces.add(combinedImageView);

            Toolkit.getDefaultToolkit().beep();
        }

    }

    private void compareAndGluePuzzlesWithRightToLeft(ImageView imageView1, ImageView imageView2, int threshold) {
        Image image1 = imageView1.getImage();
        Image image2 = imageView2.getImage();

        PixelReader reader1 = image1.getPixelReader();
        PixelReader reader2 = image2.getPixelReader();

        int width1 = (int) image1.getWidth();
        int height1 = (int) image1.getHeight();
        int width2 = (int) image2.getWidth();
        int height2 = (int) image2.getHeight();

        int heightMin = height1 < height2 ? height1 : height2;
        int heightMax = height1 > height2 ? height1 : height2;

        int totalDiff = 0;
        int minus = 0;
        for (int i = 1; i < 5; i++) {
            if (width1 / WIDTH_ONE_PUZZLE == i) {
                minus = i;
            }
        }

        for (int x1 = 0, x2 = width2 - minus; x1 < 1 && x2 < width2; x1++, x2++) {
            for (int y = 0; y < heightMin; y++) {
                int argb1 = reader1.getArgb(x1, y);
                int argb2 = reader2.getArgb(x2, y);
                System.out.println(x1 + " " + width1 + " : " + x2 + " " + width2);
                int diffR = Math.abs((argb1 >> 16) & 0xFF - (argb2 >> 16) & 0xFF);
                int diffG = Math.abs((argb1 >> 8) & 0xFF - (argb2 >> 8) & 0xFF);
                int diffB = Math.abs(argb1 & 0xFF - argb2 & 0xFF);

                int alpha1 = (argb1 >> 24) & 0xFF;
                int alpha2 = (argb2 >> 24) & 0xFF;
                int diffAlpha = Math.abs(alpha1 - alpha2);
                totalDiff += diffR + diffG + diffB + diffAlpha;
            }
        }
        double averageDiff = totalDiff / heightMin;

        System.out.println(averageDiff);

        if (averageDiff > threshold) {
            System.out.println("false");

        } else {
            System.out.println("true");

            WritableImage combinedImage = new WritableImage(width1 + width2, heightMax);
            PixelWriter pixelWriter = combinedImage.getPixelWriter();

            PixelReader pixelReader1 = image1.getPixelReader();
            PixelReader pixelReader2 = image2.getPixelReader();
            int x2 = 0;
            int x1 = 0;
            for (x1 = 0; x1 < width1; x1++) {
                for (int y1 = 0; y1 < height1; y1++) {

                    Color color1 = pixelReader1.getColor(x1, y1);
                    pixelWriter.setColor(159 + x1, y1, color1);
                }

            }
            for (x2 = 0; x2 < width2; x2++) {
                for (int y2 = 0; y2 < height2; y2++) {

                    Color color2 = pixelReader2.getColor(x2, y2);
                    pixelWriter.setColor(x2, y2, color2);

                }
            }
            ImageView combinedImageView = new ImageView(combinedImage);

            combinedImageView.setLayoutX(imageView1.getLayoutX() - imageView2.getFitWidth());
            combinedImageView.setLayoutY(imageView2.getLayoutY());
            combinedImageView.setFitWidth(width1 + width2);
            combinedImageView.setFitHeight(heightMax);

            puzzlePieces.remove(imageView1);
            puzzlePieces.remove(imageView2);
            puzzlePieces.add(combinedImageView);

            Toolkit.getDefaultToolkit().beep();
        }
    }

    private void compareAndGlueWithDownToUp(ImageView imageView1, ImageView imageView2, int threshold) {
        Image image1 = imageView1.getImage();
        Image image2 = imageView2.getImage();

        PixelReader reader1 = image1.getPixelReader();
        PixelReader reader2 = image2.getPixelReader();

        int width1 = (int) image1.getWidth();
        int height1 = (int) image1.getHeight();
        int width2 = (int) image2.getWidth();
        int height2 = (int) image2.getHeight();
        int widthMin = width1 < width2 ? width1 : width2;
        int widthMax = width1 > width2 ? width1 : width2;
        int totalDiff = 0;

        int minus = 0;
        for (int i = 1; i < 4; i++) {
            if (height1 / HEIGHT_ONE_PUZZLE == i) {
                minus = i;
            }
        }

        for (int y2 = height2 - minus, y1 = 0; y2 < height2 && y1 < 1; y1++, y2++) {
            for (int x = 0; x < widthMin; x++) {
                int argb1 = reader1.getArgb(x, y1);
                int argb2 = reader2.getArgb(x, y2);

                int diffR = Math.abs((argb1 >> 16) & 0xFF - (argb2 >> 16) & 0xFF);
                int diffG = Math.abs((argb1 >> 8) & 0xFF - (argb2 >> 8) & 0xFF);
                int diffB = Math.abs(argb1 & 0xFF - argb2 & 0xFF);

                int alpha1 = (argb1 >> 24) & 0xFF;
                int alpha2 = (argb2 >> 24) & 0xFF;
                int diffAlpha = Math.abs(alpha1 - alpha2);
                totalDiff += diffR + diffG + diffB + diffAlpha;
            }
        }
        double averageDiff = totalDiff / widthMin;

        System.out.println(averageDiff);

        if (averageDiff > threshold) {
            System.out.println("false");

        } else {
            System.out.println("true");
            WritableImage combinedImage = new WritableImage(widthMax, height1 + height2);
            PixelWriter pixelWriter = combinedImage.getPixelWriter();

            PixelReader pixelReader1 = image1.getPixelReader();
            PixelReader pixelReader2 = image2.getPixelReader();

            for (int x1 = 0; x1 < width1; x1++) {
                for (int y1 = 0; y1 < height1; y1++) {

                    Color color1 = pixelReader1.getColor(x1, y1);
                    pixelWriter.setColor(x1, y1 + (height2), color1);

                }

                for (int x2 = 0; x2 < width2; x2++) {
                    for (int y2 = 0; y2 < height2; y2++) {

                        Color color2 = pixelReader2.getColor(x2, y2);
                        pixelWriter.setColor(x2, y2, color2);
                    }

                }

            }
            ImageView combinedImageView = new ImageView(combinedImage);

            combinedImageView.setLayoutX(imageView1.getLayoutX());
            combinedImageView.setLayoutY(imageView1.getLayoutY() - imageView2.getFitHeight());
            combinedImageView.setFitHeight(height1 + height2);
            combinedImageView.setFitWidth(widthMax);

            puzzlePieces.remove(imageView1);
            puzzlePieces.remove(imageView2);
            puzzlePieces.add(combinedImageView);

            Toolkit.getDefaultToolkit().beep();
        }

    }

}

