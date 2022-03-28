package com.example.dohoa;

import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class DictionaryManagement {
    /**
     * Đọc danh sách từ điển từ file.
     */
    public void insertFromFile(Dictionary dic) {
        try {
            FileReader fileDicread = new FileReader("C:\\dohoa_6\\src\\main\\java\\com\\example\\dohoa\\dictionaries.txt");
            BufferedReader buffer_read = new BufferedReader(fileDicread);
            String enter = ""; //đọc xuống dòng
            while (true) {
                enter = buffer_read.readLine();
                if (enter == null) {
                    break;
                }
                String text[] = enter.split("\t");//đọc từng dòng trong file
                String target = text[0];
                String speech = text[1];
                String pronun = text[2];
                String explain_vn = text[3];
                String explain_en = text[4];
                String example = text[5];
                Word new_word = new Word(target, speech,pronun,explain_vn,explain_en,example);
                dic.getWords().add(new_word);
            }
        }
        catch (Exception e) {
        }
    }


    /**
     * hàm ghi vào file.
     */
    public static void dictionaryExportToFile (Dictionary dic) {
        try {
            FileWriter file_write = new FileWriter("C:\\dohoa_6\\src\\main\\java\\com\\example\\dohoa\\dictionaries.txt");
            BufferedWriter buffer_write = new BufferedWriter(file_write);
            for (Word w : dic.getWords()) {
                buffer_write.write(w.toString_All());
                buffer_write.newLine();
            }
            buffer_write.close();
            file_write.close();
        } catch (Exception e) {
        }
    }

    /**
     * hàm phát âm.
     */
    public void speak (Dictionary dic,String word) {
        try {
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us" + ".cmu_us_kal.KevinVoiceDirectory");
            Central.registerEngineCentral("com.sun.speech.freetts" + ".jsapi.FreeTTSEngineCentral");
            Synthesizer speed = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));
            speed.allocate();
            speed.resume();
            speed.speakPlainText(word, null);
            speed.waitEngineState(Synthesizer.QUEUE_EMPTY);

        } catch (Exception even) {
            System.out.println("1");
        }
    }

    /**
     * hàm check từ muốn thêm có trùng với từ đã có trong từ điển không
     */
    public int check_vt_trung (Dictionary dic, String target, String partOfSpeech) {
        dic.getWords().clear();
        insertFromFile(dic);
        for (int i=0;i<dic.getWords().size();i++) {
            if ((target).compareToIgnoreCase(dic.getWords().get(i).getWord_target())==0&&(partOfSpeech.compareToIgnoreCase(dic.getWords().get(i).getPart_of_speech())==0)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * hàm sửa từ trả về từ đã sửa xong.
     */
    public Word check_fix_word (Dictionary dic, Word fixword, String target, String speech) {
        dic.getWords().clear();
        insertFromFile(dic);
        ArrayList<String> exist = new ArrayList<>();
        ArrayList<String> fixNew = new ArrayList<>();
        fixNew.add(fixword.getWord_target());
        fixNew.add(fixword.getPart_of_speech());
        fixNew.add(fixword.getPronunciation());
        fixNew.add(fixword.getWord_explain_vn());
        fixNew.add(fixword.getWord_explain_en());
        fixNew.add(fixword.getExample());
        for (Word w: dic.getWords()) {
            if ((target.compareToIgnoreCase(w.getWord_target())==0) &&(speech.compareToIgnoreCase(w.getPart_of_speech())==0)) {
                exist.add(w.getWord_target());
                exist.add(w.getPart_of_speech());
                exist.add(w.getPronunciation());
                exist.add(w.getWord_explain_vn());
                exist.add(w.getWord_explain_en());
                exist.add(w.getExample());
                dic.getWords().remove(w);
                break;
            }
        }
        for (int i=0;i<=5;i++){
            if(fixNew.get(i).equals("")==true) {
                fixNew.set(i,exist.get(i));
            }
        }

        Word fixed_word = new Word(fixNew.get(0),fixNew.get(1),fixNew.get(2),fixNew.get(3),fixNew.get(4),fixNew.get(5));
        return fixed_word;
    }

}
