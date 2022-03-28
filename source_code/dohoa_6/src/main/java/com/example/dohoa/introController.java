package com.example.dohoa;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class introController {
    /**
     * chuyển scene sang dịch văn bản.
     */
    public void changeSceneSearch (ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader translateLoader = new FXMLLoader();
        translateLoader.setLocation(getClass().getResource("dichVB.fxml"));
        Parent searchView = translateLoader.load();
        Scene scene = new Scene(searchView);
        stage.setScene(scene);
    }

    /**
     *Chuyển scene sang tra từ.
     */
    public void changeSceneDictionary (ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader dictionaryloader = new FXMLLoader();
        dictionaryloader.setLocation(getClass().getResource("hello-view.fxml"));
        Parent dictionaryView = dictionaryloader.load();
        Scene scene = new Scene(dictionaryView);
        stage.setScene(scene);
    }

    /**
     * thoát chương trình.
     */
    public void exitButton (ActionEvent event) {
        System.exit(0);
    }
}
