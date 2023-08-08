package com.example.mypuzzlefx.breakImage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class ImageCutter {
    private ImageView[] pieces = new ImageView[20];
    private Image originalImage = new Image(ImageCutter.class.getResourceAsStream("/images/lions 800х600.jpg"));

    public ImageView[] cutImage() {

        int pieceWidth = (int) originalImage.getWidth() / 5;
        int pieceHeight = (int) originalImage.getHeight() / 4;

        PixelReader pixelReader = originalImage.getPixelReader();

        int pieceIndex = 0;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 5; x++) {
                int xPos = x * pieceWidth;
                int yPos = y * pieceHeight;

                WritableImage pieceImage = new WritableImage(pixelReader, xPos, yPos, pieceWidth, pieceHeight);
                pieces[pieceIndex] = new ImageView(pieceImage);
                pieceIndex++;
            }
        }
        return pieces;
    }
}

