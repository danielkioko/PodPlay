package com.danielkioko.podplay;

public class Item {

    public Item() {
    }

    String label, cover;
    int audio;

    public Item (String cover, String label, int audio) {

    this.cover = cover;
    this.label = label;
    this.audio = audio;

    }

    public String getCover() { return cover; }

    public void setCover(String cover) { this.cover = cover; }

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


