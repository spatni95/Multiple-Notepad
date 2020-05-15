package com.example.multi_nodepad;

public class Notes {
    public String title;
    public String date;
    public String description;

    public Notes(String title, String date, String description) {
        this.title = title;
        this.date = date;
        this.description = description;
    }

    public Notes(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
