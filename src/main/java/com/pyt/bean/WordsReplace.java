package com.pyt.bean;

/**
 * Created by PC on 2020/5/26.
 */
public class WordsReplace {

    private Long id;
    private String oldWord;
    private String newWord;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOldWord() {
        return oldWord;
    }

    public void setOldWord(String oldWord) {
        this.oldWord = oldWord;
    }

    public String getNewWord() {
        return newWord;
    }

    public void setNewWord(String newWord) {
        this.newWord = newWord;
    }
}
