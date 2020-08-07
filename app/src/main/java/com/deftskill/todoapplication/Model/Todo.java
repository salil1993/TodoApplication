package com.deftskill.todoapplication.Model;

public class Todo {
    private String title;
    private String date;
    private String status;
    private String id;

    public Todo() {
    }

    public Todo(String title, String date, String status) {
        this.title = title;
        this.date = date;
        this.status = status;
    }

    public Todo(String id, String title, String date, String s) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
