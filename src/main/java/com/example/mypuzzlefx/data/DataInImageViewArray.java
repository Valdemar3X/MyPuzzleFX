package com.example.mypuzzlefx.data;

import com.example.mypuzzlefx.breakImage.ImageCutter;
import javafx.scene.image.ImageView;

public class DataInImageViewArray {
    private ImageView [] imageViewOne = new ImageView[10];
    private ImageView [] imageViewTwo = new ImageView[10];
    private  static final double HEIGHT = 150.0;
    private  static final double WIDTH = 160.0;


    public ImageView[] getImageViewOne() {
        recordImageView();
        return imageViewOne;
    }

    public ImageView[] getImageViewTwo() {
        recordImageView();
        return imageViewTwo;
    }

    private void recordImageView(){
        ImageCutter imageCutter = new ImageCutter();
        ImageView[] images = imageCutter.cutImage();


       for (int i = 0, j = 1, k = 0; j < images.length; i += 2, j += 2, k ++){
           imageViewOne[k] = images[i];
           imageViewOne[k].setFitWidth(WIDTH);
           imageViewOne[k].setFitHeight(HEIGHT);
           imageViewTwo[k] = images[j];
           imageViewTwo[k].setFitWidth(WIDTH);
           imageViewTwo[k].setFitHeight(HEIGHT);
       }
    }
}
