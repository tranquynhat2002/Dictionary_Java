package com.example.dohoa;

public class Word {
    private String word_target;
    private String part_of_speech;
    private String pronunciation;
    private String word_explain_vn;
    private String word_explain_en;
    private String example;

    public String getWord_target() {
        return word_target;
    }

    public void setWord_target() {
        this.word_target = word_target;
    }

    public String getPart_of_speech() {
        return part_of_speech;
    }

    public void setPart_of_speech(String part_of_speech) {
        this.part_of_speech = part_of_speech;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getWord_explain_vn() {
        return word_explain_vn;
    }

    public void setWord_explain_vn() {
        this.word_explain_vn = word_explain_vn;
    }

    public String getWord_explain_en() {
        return word_explain_en;
    }

    public void setWord_explain_en(String word_explain_en) {
        this.word_explain_en = word_explain_en;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public Word (String word_target, String part_of_speech, String pronunciation, String word_explain_vn, String word_explain_en, String example) {
        this.word_target = word_target;
        this.part_of_speech = part_of_speech;
        this.pronunciation = pronunciation;
        this.word_explain_vn = word_explain_vn;
        this.word_explain_en = word_explain_en;
        this.example = example;
    }

    public String toString_EN_VN() {
        return word_target + "\n" + pronunciation + "\n" + part_of_speech + "\n" + word_explain_vn + "\n" +  example+"\n";
    }

    public String to_String_EN_VN_noVocab() {
        return part_of_speech + "\n" + word_explain_vn + "\n"  + example+"\n";
    }

    public String toString_EN_EN() {
        return word_target + " " + pronunciation + "\n" + part_of_speech + "\n" + word_explain_en + "\n" +  example+"\n";
    }

    public String to_String_EN_EN_noVocab() {
        return part_of_speech + "\n" + word_explain_en + "\n" +  example+"\n";
    }

    public String toString_VN_EN() {
        return word_explain_vn + "\n" + word_target + "\n" + pronunciation + "\n" + part_of_speech + "\n" + word_explain_en + "\n" +  example+"\n";
    }

    public String toString_All() {
        return word_target + "\t" + part_of_speech + "\t" + pronunciation + "\t" + word_explain_vn + "\t" + word_explain_en + "\t" +  example;
    }

    public String toString_All_() {
        return word_target + "\n" + part_of_speech + "\n" + pronunciation + "\n" + word_explain_vn + "\n" + word_explain_en + "\n" +  example;
    }

}
