package com.example.dohoa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class myFavoriteManagement {

    //doc tu file myFavorite.txt
    public void insertFromFavoriteFile (Dictionary dic) {
        try {
            FileReader file_readfav = new FileReader("C:\\dohoa_6\\src\\main\\java\\com\\example\\dohoa\\myFavorite.txt");
            BufferedReader buffer_readfav = new BufferedReader(file_readfav);
            String enter = ""; //đọc xuống dòng
            while (true) {
                enter = buffer_readfav.readLine();
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
                dic.getFavoriteWords().add(new_word);
            }
        }
        catch (Exception e) {
        }
    }

    //ghi file
    public void dictionaryExportToFavoriteFile (Dictionary dic) {
        try {
            FileWriter file_writeFav = new FileWriter("C:\\dohoa_6\\src\\main\\java\\com\\example\\dohoa\\myFavorite.txt");
            BufferedWriter buffer_writeFav = new BufferedWriter(file_writeFav);
            for (Word w : dic.getFavoriteWords()) {
                buffer_writeFav.write(w.toString_All());
                buffer_writeFav.newLine();
            }
            buffer_writeFav.close();
            file_writeFav.close();
        } catch (Exception e) {
        }
    }

}
