package com.example.dohoa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class myFavoriteController extends HelloController implements Initializable {
    @FXML
    Button removeButton;

    @FXML
    TextArea detailWord;

    @FXML
    TableView<Word> favoriteTable;

    @FXML
    private TableColumn<Word,String> targetFav;
    @FXML
    private TableColumn<Word,String> speechFav;
    @FXML
    private TableColumn<Word,String> pronunFav;
    @FXML
    private TableColumn<Word,String> explainVnFav;
    @FXML
    private TableColumn<Word,String> explainEnFav;

    private ObservableList<Word> favoriteList;

    /**
     *bảng favourite dùng tableView.
     */
    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        fav_manage.insertFromFavoriteFile(dic);
        favoriteList = FXCollections.observableArrayList(dic.getFavoriteWords());
        targetFav.setCellValueFactory(new PropertyValueFactory<Word,String>("word_target"));
        speechFav.setCellValueFactory(new PropertyValueFactory<Word,String>("part_of_speech"));
        pronunFav.setCellValueFactory(new PropertyValueFactory<Word,String>("pronunciation"));
        explainVnFav.setCellValueFactory(new PropertyValueFactory<Word,String>("word_explain_vn"));
        explainEnFav.setCellValueFactory(new PropertyValueFactory<Word,String>("word_explain_en"));
        favoriteTable.setItems(favoriteList);
    }

    //xem chi tiết từ trong bảng
    public void detail (ActionEvent event) {
        Word selected_detail = favoriteTable.getSelectionModel().getSelectedItem();
        detailWord.setText(selected_detail.toString_All_());
    }

    //xóa từ trong my Favourite
    public void removeFavWord (ActionEvent event) {
        Word selected_remove = favoriteTable.getSelectionModel().getSelectedItem();
        dic.getFavoriteWords().remove(selected_remove);
        favoriteList.remove(selected_remove);
        fav_manage.dictionaryExportToFavoriteFile(dic);
        Alert thong_bao = new Alert(Alert.AlertType.INFORMATION);
        thong_bao.setContentText("Remove the word successfully!");
        thong_bao.show();
    }

    //quay lại tra từ
    public void goBack (ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("hello-view.fxml"));
        Parent main_scene = loader.load();
        Scene scene = new Scene(main_scene);
        stage.setScene(scene);
    }

    //thoát chương trình
    public void exitBut (ActionEvent event) {
        System.exit(0);
    }

    //đi đến intro
    public void goIntro (ActionEvent event) throws IOException {
        Stage introstage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader introloader = new FXMLLoader();
        introloader.setLocation(getClass().getResource("intro.fxml"));
        Parent intro_scene = introloader.load();
        Scene introscene = new Scene(intro_scene);
        introstage.setScene(introscene);
    }
}
