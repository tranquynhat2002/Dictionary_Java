package com.example.dohoa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Optional;

public class dichVBController {
    Dictionary dic = new Dictionary();
    DictionaryManagement dicManage = new DictionaryManagement();

    @FXML
    TextArea input;

    @FXML
    TextArea output;

    @FXML
    Label languages;
    private String languageFrom = "";
    private String languageTo = "";

    /**
     * chọn ngôn ngữ dịch.
     */
    public void choiceLanguage(ActionEvent event) {
        ChoiceDialog<String> selected = new ChoiceDialog<>("English -> Vietnamese","Vietnamese -> English");
        Optional<String> language = selected.showAndWait();
        language.ifPresent(languageEvent -> {
            languages.setText(languageEvent);
        });
    }

    /**
     *Dịch - button Translate.
     */
    public void translate (ActionEvent event) throws IOException {
        if (languages.getText().equals("Language -> Language")==true) {
            Alert thong_bao = new Alert(Alert.AlertType.INFORMATION);
            thong_bao.setContentText("You have to select Language -> Language");
            thong_bao.show();
            return;
        }
        else {
            if (languages.getText().equals("English -> Vietnamese") == true) {
                languageFrom = "en";
                languageTo = "vi";
            } else if (languages.getText().equals("Vietnamese -> English") == true) {
                languageFrom = "vi";
                languageTo = "en";
            }
            output.setText(translate(languageFrom, languageTo, input.getText()));
        }
    }

    /**
     * hàm dịch VB.
     */
    private static String translate(String languageFrom, String languageTo, String text) throws IOException {
        //lấy link url link đến phần tra từ
        String urlStr = "https://script.google.com/macros/s/AKfycbzxMxyIRR3PS8hcPqrxceX2aFYr57gkFvmDgqRZCA/exec" +
                "?q=" + URLEncoder.encode(text, "UTF-8") + "&target=" + languageTo + "&source=" + languageFrom;
        URL urlTranslate = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        //nhận link url bằng openConnection() và truyền kết quả đến connect
        HttpURLConnection connect = (HttpURLConnection) urlTranslate.openConnection();
        connect.setRequestProperty("User-Agent", "Mozilla/5.0");
        //tạo bufferedReader để đọc trang có link url đã kết nối
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(connect.getInputStream()));
        String inputLine;
        while ((inputLine = inputReader.readLine()) != null) {
            response.append(inputLine);
        }
        inputReader.close();
        return response.toString();
    }

    /**
     *chuyển scene về intro
     */
    public void goHome (ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader introloader = new FXMLLoader();
        introloader.setLocation(getClass().getResource("intro.fxml"));
        Parent intro_scene = introloader.load();
        Scene scene = new Scene(intro_scene);
        stage.setScene(scene);
    }

    //xóa input để nhập lại.
    public void clearInput (ActionEvent event) {
        input.clear();
        output.clear();
    }

    //đọc tiếng anh đoạn văn bản chưa dịch/đã dịch
    public void phatam (ActionEvent event) {
        if (languages.getText().equals("English -> Vietnamese")==true) {
            dicManage.speak(dic, input.getText());
        }
        else {
            dicManage.speak(dic,output.getText());
        }
    }

    //thoát chương trình
    public void exitAll(ActionEvent event) {
        System.exit(0);
    }
}
