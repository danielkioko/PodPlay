package com.danielkioko.podplay;

public class Item {

    public Item() {
    }

    String label;
    int cover;
    int audio;

    public Item (int cover, String label, int audio) {

    this.cover = cover;
    this.label = label;
    this.audio = audio;

    }

    public int getCover() {
        return cover;
    }

    public void setCover(int cover) {
        this.cover = cover;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getAudio() {
        return audio;
    }

    public void setAudio(int audio) {
        this.audio = audio;
    }

}


