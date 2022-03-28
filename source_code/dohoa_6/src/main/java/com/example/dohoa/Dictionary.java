package com.example.dohoa;

import java.util.ArrayList;

public class Dictionary {

    private ArrayList<Word> words = new ArrayList<Word>();

    private ArrayList<Word> favoriteWords = new ArrayList<>();

    public ArrayList<Word> getWords() {
        return words;
    }
    public ArrayList<Word> getFavoriteWords() {
        return favoriteWords;
    }

}
