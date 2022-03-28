package com.example.dohoa;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.controlsfx.control.action.Action;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;



public class HelloController implements Initializable {
    Dictionary dic = new Dictionary();
    DictionaryManagement dic_manage = new DictionaryManagement();
    myFavoriteManagement fav_manage = new myFavoriteManagement();

    private String EV = "English - Vietnamese";
    private String VE = "Vietnamese - English";
    private String EE = "English - English";

    @FXML
    private TextField yourWord;

    @FXML
    public TextArea meanLabel;
    @FXML
    private Label languageLabel;

    @FXML
    private ListView<String> searchListView;

    @FXML
    private TableView<Word> historyTable;

    @FXML
    private TableColumn<Word,String> targetHis;
    @FXML
    private TableColumn<Word,String> speechHis;
    @FXML
    private TableColumn<Word,String> explainVnHis;

    private ObservableList<Word> historyList;

    /**
     *Bảng lịch sử tra từ - TableView.
     */
    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        historyList = FXCollections.observableArrayList();
        targetHis.setCellValueFactory(new PropertyValueFactory<Word,String>("word_target"));
        speechHis.setCellValueFactory(new PropertyValueFactory<Word,String>("part_of_speech"));
        explainVnHis.setCellValueFactory(new PropertyValueFactory<Word,String>("word_explain_vn"));
        historyTable.setItems(historyList);
    }

    public void detailHistory (ActionEvent event) {
        Word selectedWord = historyTable.getSelectionModel().getSelectedItem();
        if (languageLabel.getText().equals(EV)==true) {
            meanLabel.setText(selectedWord.toString_EN_VN());
        }
        else if (languageLabel.getText().equals(EE)==true){
            meanLabel.setText(selectedWord.toString_EN_EN());
        }
        else{
            meanLabel.setText(selectedWord.toString_VN_EN());
        }
    }
    public void deleteHistory (ActionEvent event) {
        Word selectedWord = historyTable.getSelectionModel().getSelectedItem();
        historyList.remove(selectedWord);
    }
    public void clearHistory (ActionEvent event) {
        historyList.clear();
    }

    /**
     *chọn ngôn ngữ dịch - Button & ChoiceDialog.
     */
    public void selectLanguage(ActionEvent event) {
        ChoiceDialog<String> selectDialog = new ChoiceDialog<>(EV,VE,EE);
        Optional<String> language = selectDialog.showAndWait();
        language.ifPresent(languageEvent -> {
            languageLabel.setText(languageEvent);
        });
    }

    /**
     * kiểm tra xem từ muốn thêm vào list History đã có trong list chưa
     */
    boolean check_Trung_His(Word w){
        boolean kt = false;
        for(Word e: historyList) {
            if (w.getWord_target().equals(e.getWord_target())==true&&w.getPart_of_speech().equals(e.getPart_of_speech())==true) {
                kt=true;
                break;
            }
        }
        return kt;
    }

    /**
     *hàm in ra giải thích từ sau khi tra từ.
     */
    //anh->việt
    public String dictionaryLookup_EN_VN (Dictionary dic, String findWord) {
        String mean = "";
        int dem = 0,vt = 0;
        //đếm xem trong từ điển có bao nhiêu từ find_word
        for(Word o : dic.getWords()) {
            if ((o.getWord_target()).equals(findWord) == true) {
                dem++;
            }
        }
        //thêm từ và nghĩa của từ đầu tiên tìm được vào
        for(int i=0;i<dic.getWords().size();i++) {
            if ((dic.getWords().get(i).getWord_target()).equals(findWord) == true) {
                mean = dic.getWords().get(i).toString_EN_VN();
                if (check_Trung_His(dic.getWords().get(i))==false) {
                    historyList.add(dic.getWords().get(i));
                }
                vt=i;
                break;
            }
        }
        //nếu trong từ điển tìm được hai từ find_word trở lên(giống nhau về từ nhưng khác về loại từ và về nghĩa
        if (dem > 1) {
            for(int j=vt+1;j<dic.getWords().size();j++) {
                if ((dic.getWords().get(j).getWord_target()).equals(findWord) == true) {
                    //thêm vào chuỗi các phần thuộc tính trừ từ mới, tránh in lặp hai từ trùng nhau
                    mean = mean + dic.getWords().get(j).to_String_EN_VN_noVocab();
                    if (check_Trung_His(dic.getWords().get(j))==false) {
                        historyList.add(dic.getWords().get(j));
                    }
                }
            }
        }
        return mean;
    }

    //việt -> anh
    public String dictionaryLookup_VN_EN (Dictionary dic, String find_word) {
        String mean = "";
        for(Word o : dic.getWords()) {
            if ((o.getWord_explain_vn()).equals(find_word) == true) {
                mean = o.toString_VN_EN();
                if (check_Trung_His(o)==false) {
                    historyList.add(o);
                }
            }
        }
        return mean;
    }

    //anh->anh
    public String dictionaryLookup_EN_EN (Dictionary dic, String findWord) {
        String mean = "";
        int dem=0,vt=0;
        for(Word o : dic.getWords()) {
            if ((o.getWord_target()).equals(findWord) == true) {
                dem++;
            }
        }
        for(int i=0;i<dic.getWords().size();i++) {
            if ((dic.getWords().get(i).getWord_target()).equals(findWord) == true) {
                mean = dic.getWords().get(i).toString_EN_EN();
                if (check_Trung_His(dic.getWords().get(i))==false) {
                    historyList.add(dic.getWords().get(i));
                }
                vt=i;
                break;
            }
        }
        if (dem > 1) {
            for(int j=vt+1;j<dic.getWords().size();j++) {
                if ((dic.getWords().get(j).getWord_target()).equals(findWord) == true) {
                    mean = mean + dic.getWords().get(j).to_String_EN_EN_noVocab();
                    if (check_Trung_His(dic.getWords().get(j))==false) {
                        historyList.add(dic.getWords().get(j));
                    }
                }
            }
        }
        return mean;
    }

    /**
     *hàm tra từ: nhập đầy đủ ký tự các từ để tra.
     */
    public void dictionaryLookup (ActionEvent event) {
        String yourword = yourWord.getText();
        yourword = yourword.toLowerCase(Locale.ROOT);
        dic.getWords().clear();
        dic_manage.insertFromFile(dic);
        Alert thong_bao = new Alert(Alert.AlertType.INFORMATION);
        //nếu chưa chọn ngôn ngữ
        if (languageLabel.getText().equals("Select a language") == true) {
            thong_bao.setContentText("You have to select a language!");
            thong_bao.show();
            return;
        }
        //nếu chưa nhập từ
        else if (yourword.equals("") == true) {
            thong_bao.setContentText("You have to enter the word!");
            thong_bao.show();
            return;
        } else {
            String mean = "";
            if (languageLabel.getText().equals(EV) == true) {
                mean = dictionaryLookup_EN_VN(dic, yourword);
            } else if (languageLabel.getText().equals(VE) == true) {
                mean = dictionaryLookup_VN_EN(dic, yourword);
            } else if (languageLabel.getText().equals(EE) == true) {
                mean = dictionaryLookup_EN_EN(dic, yourword);
            }
            if (mean.equals("") == false) {
                meanLabel.setText(mean);
                return;
            } else {
                thong_bao.setContentText("The word have not been updated!");
                thong_bao.show();
            }
        }
    }

    /**
     * hàm search từ: khi nhập vài ký tự đầu -> có gợi ý dưới list word để tra.
     */
    String searchstr;
    public void dictionarySearcher (ActionEvent event) {
        dic.getWords().clear();
        dic_manage.insertFromFile(dic);
        searchListView.getItems().clear();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        String yourword = yourWord.getText();
        //nếu chưa chọn ngôn ngữ
        if(languageLabel.getText().equals("Select a language")==true){
            alert.setContentText("You have to select a language!");
            alert.show();
            return;
        }
        //nếu chưa nhập từ
        else if(yourword.equals("")==true) {
            alert.setContentText("You have to enter the word!");
            alert.show();
            return;
        }
        else {
            String mean = "";
            dic.getWords().clear();
            dic_manage.insertFromFile(dic);
            String ds = ""; //ghi lại ds các từ có các ký tự đầu trùng với ký tự nhập vào
            int dem = 0;
            //đếm số lượng từ và ghi lại ds từ
            if (languageLabel.getText().equals(EE)==true||languageLabel.getText().equals(EV)==true) {
                for (Word o : dic.getWords()) {
                    //nếu vị trí của yourword tìm được trong từ là vị trí đầu tiên
                    if (o.getWord_target().indexOf(yourword) == 0) {
                        ds = ds + o.getWord_target() + "\t"; //ghi từ vào ds và ngăn cách bằng \t
                        dem++;//tăng số lượng từ
                    }
                }
            }
            else if (languageLabel.getText().equals(VE) == true){
                for (Word o : dic.getWords()) {
                    if (o.getWord_explain_vn().indexOf(yourword) == 0) {
                        ds = ds + o.getWord_explain_vn() + "\t";
                        dem++;
                    }
                }
            }
            //nếu không có từ nào
            if (dem == 0) {
                Alert newAlert = new Alert(Alert.AlertType.INFORMATION);
                newAlert.setContentText("The word have not been updated!");
                newAlert.show();
                return;
            } else { //nếu có từ
                //lấy từ ra và lưu vào mảng tu
                String[] tu = ds.split("\t");
                //check trùng lặp, nếu tìm được từ xuất hiện nhiều hơn 1 lần thì chỉ in ra 1 lần
                for(int i = 0; i < tu.length - 1; i++){
                    for(int j = i + 1; j < tu.length; j++){
                        if(tu[i].equals(tu[j]) == true) {
                            tu[j] = "1"; //nếu bằng thì lưu biến sau là 1 để không thêm vào list
                        }
                    }
                }
                for (int i = 0;i < tu.length; i++) {
                    if (tu[i].equals("1") == false) {
                        searchListView.getItems().add(tu[i]);//nếu không phải là ký tự 1 thì mới thêm vào list word để hiện ra
                    }
                }
                //xử lý sự kiện khi click vào list word để từ hiện ra
                searchListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String o, String t1) {
                        searchstr = searchListView.getSelectionModel().getSelectedItem();//từ đã chọn trong list word
                        String result = "";
                        //thuật toán tương tự như tra từ
                        if (languageLabel.getText().equals(EV)==true) {
                            result = dictionaryLookup_EN_VN(dic,searchstr);
                        }
                        else if (languageLabel.getText().equals(EE)==true) {
                            result = dictionaryLookup_EN_EN(dic,searchstr);
                        }
                        else if (languageLabel.getText().equals(VE) == true){
                            result = dictionaryLookup_VN_EN(dic,searchstr);
                        }
                        meanLabel.setText(result);
                    }
                });
            }
        }
    }

    /**
     * hàm thêm từ vào ds - dialog.
     */
    public void dictionaryAdd (ActionEvent event) {
        dic.getWords().clear();
        dic_manage.insertFromFile(dic);

        Dialog<Word> dialog = new Dialog<>();
        dialog.setTitle("Add word dialog");
        dialog.setHeaderText("(*): required" + "\n"+"Add new word to dictionary");

        GridPane grid = new GridPane();
        grid.setHgap(20);//các cột cách nhau 20
        grid.setVgap(20);//các hàng cách nhau 20
        grid.setPadding(new Insets(20,100,10,10));

        //set những từ trong textField để gợi ý thêm
        TextField target = new TextField();
        target.setPromptText("Type a new word...");
        TextField speech = new TextField();
        speech.setPromptText("Noun/Verb/Adverb/...");
        TextField pronun = new TextField();
        pronun.setPromptText("Pronunciation...");
        TextField explain_vn = new TextField();
        explain_vn.setPromptText("Vietnamese meaning...");
        TextField explain_en = new TextField();
        explain_en.setPromptText("English meaning...");
        TextField example = new TextField();
        example.setPromptText("Ex:... ");

        //set vị trí của label và textField của các trường từ
        grid.add(new Label("Add new word(*): "),0,0);
        grid.add(target,1,0);
        grid.add(new Label("Add part of speech(*): "),0,1);
        grid.add(speech,1,1);
        grid.add(new Label("Add pronunciation(*): "),0,2);
        grid.add(pronun,1,2);
        grid.add(new Label("Add the explanation of Vietnamese(*):"),0,3);
        grid.add(explain_vn,1,3);
        grid.add(new Label("Add the explanation of English(*): "),0,4);
        grid.add(explain_en,1,4);
        grid.add(new Label("Add example(*): "),0,5);
        grid.add(example,1,5);

        ButtonType addButType = new ButtonType("Add",ButtonBar.ButtonData.OK_DONE);//button add
        dialog.getDialogPane().getButtonTypes().addAll(addButType,ButtonType.CANCEL);//button cancel

        Node addButton = dialog.getDialogPane().lookupButton(addButType);
        addButton.setDisable(true);// set cái button add là true


        target.textProperty().addListener((observableValue, gtCu, gtMoi) -> {
            addButton.setDisable(gtMoi.trim().isEmpty());
        });

        //lấy giá trị của các trường vừa nhập vào
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(addDialogEvent -> {
            if (addDialogEvent == addButType) {
                return new Word(target.getText(), speech.getText(), pronun.getText(), explain_vn.getText(), explain_en.getText(), example.getText());
                //trả về dữ liệu ở result
            }
            return null;
        });
        //chờ người dùng nhập và lưu kết quả nhập vào result
        Optional <Word> result = dialog.showAndWait();
        //kiểm tra nếu result nhận đúng định dạng của word (6 chuỗi string)
        result.ifPresent(newAddWord -> {
            Alert thong_bao = new Alert(Alert.AlertType.INFORMATION);
            if (target.getText().equals("")==true||speech.getText().equals("")==true||pronun.getText().equals("")==true||explain_vn.getText().equals("")==true||explain_en.getText().equals("")==true||example.getText().equals("")==true) {
                thong_bao.setContentText("Add a new word unsuccessfully!" + "\n" + "You have not already filled in the blanks");
                thong_bao.show();
            }
            else {
                Word addWord = new Word(target.getText(), speech.getText(), pronun.getText(), explain_vn.getText(), explain_en.getText(), example.getText());
                int checkTrung = dic_manage.check_vt_trung(dic, target.getText(), speech.getText());
                if (checkTrung == -1) {
                    dic.getWords().add(addWord);
                    thong_bao.setContentText("Add a new word to dictionary successfully!");
                } else {
                    thong_bao.setContentText("The word already existed in dictionary!");
                }
                thong_bao.show();
            }
        });
        //cập nhật lại mảng vào file
        dic_manage.dictionaryExportToFile(dic);
    }

    /**
     * hàm sửa từ - dialog.
     */
    public void dictionaryFix (ActionEvent event) {
        dic.getWords().clear();
        dic_manage.insertFromFile(dic);
        Dialog<Word> dialog = new Dialog<>();
        dialog.setTitle("Fix word dialog");
        dialog.setHeaderText("(*): required" + "\n" + "Fill one or some blanks which you want to fix...");

        ButtonType fixButtonType = new ButtonType("Fix",ButtonBar.ButtonData.OK_DONE);//button fix
        dialog.getDialogPane().getButtonTypes().addAll(fixButtonType,ButtonType.CANCEL);//button cancel

        GridPane grid = new GridPane();
        grid.setHgap(20);//các cột cách nhau 20
        grid.setVgap(20);//các hàng cách nhau 20
        grid.setPadding(new Insets(20,100,10,10));

        TextField targetExist = new TextField();
        targetExist.setPromptText("Find a word...");
        TextField speechExist = new TextField();
        speechExist.setPromptText("Noun/Verb....");

        TextField target = new TextField();
        target.setPromptText("Type a word...");
        TextField speech = new TextField();
        speech.setPromptText("Noun/Verb/Adjective/Adverb/...");
        TextField pronun = new TextField();
        pronun.setPromptText("Pronunciation...");
        TextField explain_vn = new TextField();
        explain_vn.setPromptText("Vietnamese meaning...");
        TextField explain_en = new TextField();
        explain_en.setPromptText("English meaning...");
        TextField example = new TextField();
        example.setPromptText("Ex:... ");

        //bắt buộc nhập từ tiếng anh muốn sửa và loại từ, phòng trường hợp nhiều từ nhưng khác loại từ thì chỉ sửa từ và loại từ đã nhập
        grid.add(new Label("Type a word which you want to fix (*): "),0,0);
        grid.add(targetExist,1,0);
        grid.add(new Label("Part of speech(*) : "),0,1);
        grid.add(speechExist,1,1);

        //chỉ điền những trường bạn muốn thay đổi
        grid.add(new Label("Fix the word: "),0,2);
        grid.add(target,1,2);
        grid.add(new Label("Fix part of speech: "),0,3);
        grid.add(speech,1,3);
        grid.add(new Label("Fix pronunciation: "),0,4);
        grid.add(pronun,1,4);
        grid.add(new Label("Fix the explanation of Vietnamese:"),0,5);
        grid.add(explain_vn,1,5);
        grid.add(new Label("Fix the explanation of English: "),0,6);
        grid.add(explain_en,1,6);
        grid.add(new Label("Fix example: "),0,7);
        grid.add(example,1,7);

        Node fixButton = dialog.getDialogPane().lookupButton(fixButtonType);
        fixButton.setDisable(true);// set cái button add là true

        targetExist.textProperty().addListener((observableValue, old_value, new_value) -> {
            fixButton.setDisable(new_value.trim().isEmpty());
        });//bắt sự kiện text ở target, nếu có ký tự thì mới hiện button fix


        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(fix_dialog -> {
            if (fix_dialog == fixButtonType) {
                return new Word(target.getText(), speech.getText(), pronun.getText(), explain_vn.getText(), explain_en.getText(), example.getText());
                //trả về dữ liệu ở result
            }
            return null;
        });
        //chờ người dùng nhập và lưu kết quả nhập vào result
        Optional <Word> result = dialog.showAndWait();
        //kiểm tra nếu result nhận đúng định dạng của word (6 chuỗi string)
        result.ifPresent(fixWord -> {
            Alert thong_bao = new Alert(Alert.AlertType.INFORMATION);
            if (targetExist.getText().equals("")==true||speechExist.getText().equals("")==true) {
                thong_bao.setContentText("Fix the word unsuccessfully!" + "\n" + "You have not already filled in required blanks");
                thong_bao.show();
            } else {
                Word fixword = new Word(target.getText(), speech.getText(), pronun.getText(), explain_vn.getText(), explain_en.getText(), example.getText());
                //kiểm tra xem có tìm thấy từ và loại từ trong từ điển không
                int vt = dic_manage.check_vt_trung(dic, targetExist.getText(), speechExist.getText());

                if (vt != -1) {//nếu tìm thấy thì sửa
                    Word fixedWord = dic_manage.check_fix_word(dic, fixword, targetExist.getText(), speechExist.getText());
                    dic.getWords().add(vt,fixedWord);
                    thong_bao.setContentText("Fix the word in dictionary successfully!");
                } else {
                    thong_bao.setContentText("The word have not been updated!");
                }
                thong_bao.show();
            }
        });
        //cập nhật vào file
        dic_manage.dictionaryExportToFile(dic);
    }

    /**
     * hàm xóa từ.
     */
    public void dictionaryDelete (ActionEvent event) {
        dic.getWords().clear();
        dic_manage.insertFromFile(dic);
        int kt = 0; //kt xem có tìm thấy từ để xóa không
        Alert thong_bao = new Alert(Alert.AlertType.INFORMATION);
        if(languageLabel.getText().equals("Select a language")==true){
            thong_bao.setContentText("You have to select a language!");
            thong_bao.show();
            return;
        }
        else if (yourWord.getText().equals("")==true) {
            thong_bao.setContentText("You have to enter the word!");
            thong_bao.show();
            return;
        }
        else {
            if (languageLabel.getText().equals(EE)==true||languageLabel.getText().equals(EV)==true) {
                for (int i = 0; i < dic.getWords().size(); i++) {
                    if (dic.getWords().get(i).getWord_target().equals(yourWord.getText()) == true) {
                        dic.getWords().remove(i);
                        kt++;
                    }
                }
            }
            else {
                for (int i = 0; i < dic.getWords().size(); i++) {
                    if (dic.getWords().get(i).getWord_explain_vn().equals(yourWord.getText()) == true) {
                        dic.getWords().remove(i);
                        kt++;
                    }
                }
            }
            if (kt != 0) {
                thong_bao.setContentText("Delete the new word successfully!");
            } else {
                boolean check=false;
                for (Word e:dic.getWords()) {
                    if (e.getWord_target().indexOf(yourWord.getText())==0) {
                        check = true;
                        break;
                    }
                }
                //nếu có từ trong từ điển chứa dữ liệu nhập vào mà chưa đủ
                if (check == true) {
                    thong_bao.setContentText("You have to fill enough characters!");
                } else { //nếu không có trong từ điển
                    thong_bao.setContentText("The word have not been update!");
                }
            }
            thong_bao.show();
            dic_manage.dictionaryExportToFile(dic);
        }
    }

    /**
     * hàm phát âm
     */
    public void dictionaryPronunciation (ActionEvent event) {
        if (yourWord.getText().equals("")==true&&meanLabel.getText().equals("")==true) {
            Alert thong_bao = new Alert(Alert.AlertType.INFORMATION);
            thong_bao.setContentText("You have to enter a word!");
            thong_bao.show();
        }
        else if (yourWord.getText().equals("")==false&&meanLabel.getText().equals("")==true){
            if (languageLabel.getText().equals(EE)==true||languageLabel.getText().equals(EV)==true) {
                dic_manage.speak(dic, yourWord.getText());
            }
        }
        else {
            String[] tu = meanLabel.getText().split("\\r?\\n");
            if (languageLabel.getText().equals(EV) == true || languageLabel.getText().equals(EE) == true) {
                dic_manage.speak(dic, tu[0]);
            } else if (languageLabel.getText().equals(VE)==true) {
                dic_manage.speak(dic,tu[1]);
            }
        }
    }

    //thoát chương trình
    public void exitButton (ActionEvent event) {
        System.exit(0);
    }

    //chuyển scene của favorite
    public void changeSceneMyFavorite(ActionEvent event) throws IOException{
        //gọi favstage là stage sẽ hiển thị trang Favorite
        Stage favstage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader favloader = new FXMLLoader();
        //tìm vị trí của file fxml để load trang myFavorite
        favloader.setLocation(getClass().getResource("myFavorite.fxml"));
        //load trang myFavourite
        Parent favoriteView = favloader.load();
        Scene favscene = new Scene(favoriteView);
        //hiển thị trang favStage
        favstage.setScene(favscene);
    }

    /**
     *hàm thêm từ vào favorite.
     */
    public void favoriteAdd (ActionEvent event) {
        dic.getFavoriteWords().clear();
        fav_manage.insertFromFavoriteFile(dic);
        String wordFav = meanLabel.getText();
        String[] vocabs = wordFav.split("\\r?\\n");
        Alert thong_bao = new Alert(Alert.AlertType.INFORMATION);
        int dem=0;
        if (languageLabel.getText().equals(EE)==true || languageLabel.getText().equals(EV)==true) {
            //check từ có trùng với từ trong danh sách favorite hiện có không?
            for (Word w: dic.getFavoriteWords()) {
                if (w.getWord_target().equals(vocabs[0])==true) {
                    dem++;
                }
            }
            //nếu không có thì thêm
            if (dem==0) {
                for (Word o : dic.getWords()) {
                    if (o.getWord_target().equals(vocabs[0]) == true) {
                        dic.getFavoriteWords().add(o);
                    }
                }
                thong_bao.setContentText("Add the word to my Favourites successfully!");
                thong_bao.show();
            }
            else{
                thong_bao.setContentText("The word existed in my Favourites!");
                thong_bao.show();
            }
        }
        else {
            for (Word w: dic.getFavoriteWords()) {
                if (w.getWord_explain_vn().equals(vocabs[0])==true) {
                    dem++;
                }
            }
            if (dem==0) {
                for (Word o : dic.getWords()) {
                    if (o.getWord_explain_vn().equals(vocabs[0]) == true) {
                        dic.getFavoriteWords().add(o);
                        break;
                    }
                }
                thong_bao.setContentText("Add the word to my Favourites successfully!");
                thong_bao.show();
            }
            else{
                thong_bao.setContentText("The word existed in my Favourites!");
                thong_bao.show();
            }
        }
        fav_manage.dictionaryExportToFavoriteFile(dic);
    }

    /**
     * chuyển scene sang intro.
     */
    public void goBackIntro (ActionEvent event) throws IOException {
        Stage introStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader introloader = new FXMLLoader();
        introloader.setLocation(getClass().getResource("intro.fxml"));
        Parent main_scene = introloader.load();
        Scene scene = new Scene(main_scene);
        introStage.setScene(scene);
    }
}