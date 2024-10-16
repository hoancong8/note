package com.example.myapplication;

public class Note {
    private String date;
    private String title;
    private String contentTitle,status,clock;
    private int id,status1;

    public Note(String date, String title, String contentTitle, String status, int id) {
        this.date = date;
        this.title = title;
        this.contentTitle = contentTitle;
        this.status = status;
        this.id = id;
    }

    public Note(String date, String title, String contentTitle, String status, String clock, int id, int status1) {
        this.date = date;
        this.title = title;
        this.contentTitle = contentTitle;
        this.status = status;
        this.clock = clock;
        this.id = id;
        this.status1 = status1;
    }

    public Note(String date, String title, String contentTitle, String clock, int id, int status1) {
        this.date = date;
        this.title = title;
        this.contentTitle = contentTitle;
        this.clock = clock;
        this.id = id;
        this.status1 = status1;
    }

    public Note(String date, String title, String contentTitle, int id, String clock) {
        this.date = date;
        this.title = title;
        this.contentTitle = contentTitle;
        this.id = id;
        this.clock = clock;
    }

    public String getClock() {
        return clock;
    }

    public int getStatus1() {
        return status1;
    }

    public void setStatus1(int status1) {
        this.status1 = status1;
    }

    public void setClock(String clock) {
        this.clock = clock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Note(String title, String contentTitle) {
        this.title = title;
        this.contentTitle = contentTitle;
    }


    public Note(String date, String title, String contentTitle) {
        this.date = date;
        this.title = title;
        this.contentTitle = contentTitle;
    }

    public Note(String date, String title, String contentTitle, int id) {
        this.date = date;
        this.title = title;
        this.contentTitle = contentTitle;
        this.id = id;
    }

    public Note(String date, String title, String contentTitle, String status) {
        this.date = date;
        this.title = title;
        this.contentTitle = contentTitle;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
