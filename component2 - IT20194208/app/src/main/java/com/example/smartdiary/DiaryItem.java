package com.example.smartdiary;

public class DiaryItem {
    private String text;
    private int hour;
    private int minute;
    private String selectedTime;

    public DiaryItem(String text, int hour, int minute,String selectedTime) {
        this.text = text;
        this.hour = hour;
        this.minute = minute;
        this.selectedTime = selectedTime;
    }

    public String getText() {
        return text;
    }


    public void setText(String text) {
        this.text = text;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
    // Getter and setter for the selected time
    public String getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(String selectedTime) {
        this.selectedTime = selectedTime;
    }
}