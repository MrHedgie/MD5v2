package com.md5_2;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.apache.commons.io.FileUtils;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ForestFireController {
    @FXML
    private TextField xBombCord;
    @FXML
    private TextField yBombCord;
    @FXML
    private Button dropWaterBombButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Button unpauseButton;
    @FXML
    private HBox controlPanel;
    @FXML
    private ImageView imageView;
    @FXML
    private ChoiceBox<String> windBox;
    @FXML
    private TextField iterationsField;
    @FXML
    private Button ritualButton;
    @FXML
    private Button startButton;
    private String pathChangeable = "src/main/resources/TestBig3.png";
    private final ForestFire forestFire = new ForestFire(pathChangeable);
    private int flag = 0;
    private final static ExecutorService executor = Executors.newFixedThreadPool(2);
    private static Timeline timeline;


    @FXML
    protected void initialize() {
        controlPanel.setManaged(true);
        controlPanel.setVisible(true);
        windBox.getItems().addAll("N", "W", "E", "S", "NONE");
        imageView.setImage(FileHandler.openImage(pathChangeable));
    }

    @FXML
    private void run() {
        try {
            FileUtils.cleanDirectory(new File("src/main/java/images"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AtomicInteger iter = new AtomicInteger();
        StartController startController = new StartController();
        flag = 1;
        executor.submit(() -> {
            while (flag == 1) {
                forestFire.logic(startController.baseFireChance, startController.isRegrow, flag, iter.get());
                iter.getAndIncrement();
                iterationsField.setText(iter.toString());
            }
        });
        timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            try {
                Thread.sleep(70);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            Image nextImage = FileHandler.openImage("src/main/java/images/" + iter + ".png");
            imageView.setImage(nextImage);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        if (flag == 1) timeline.play();
        executor.shutdown();
    }

    @FXML
    private void dropWaterBomb() {

    }

    @FXML
    private void castRitual() {
        flag = 0;
        forestFire.castRitual();
        imageView.setImage(FileHandler.openImage("src/main/java/images/RitualedImage.png"));
    }

    @FXML
    private void pause() {
        flag = 0;
        timeline.pause();
    }

    @FXML
    private void unpause() {
        flag = 1;
        timeline.play();
    }
}
