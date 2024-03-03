package com.example.smartdiary;

public class DiaryItemWednsday {
    private String text;
    private String time;
    private int imageResourceId;

    public DiaryItemWednsday(String text, String time, int imageResourceId) {
        this.text = text;
        this.time = time;
        this.imageResourceId = imageResourceId;
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        return time;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
