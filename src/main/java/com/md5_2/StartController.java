package com.md5_2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.spreadsheet.Grid;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class StartController {
    @FXML
    VBox gridPanel;
    @FXML
    private TextField fireChanceField;
    @FXML
    private RadioButton enableRegrowRadioButton;
    @FXML
    private Button runButton;
    protected double baseFireChance;
    protected boolean isRegrow;

    @FXML
    private void startFire(){
        baseFireChance = Double.parseDouble(fireChanceField.getText());
        isRegrow = enableRegrowRadioButton.isSelected();
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("forest-fire-view.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gridPanel.getScene().getWindow().setOpacity(0);
    }
}
