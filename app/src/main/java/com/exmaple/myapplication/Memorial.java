package com.exmaple.myapplication;


import java.io.Serializable;

public class Memorial implements Serializable {
    private int id;
    private String title;
    private String content;
    private String time;
    private String type;
    private String img;

    public Memorial(int id, String title, String content, String time, String type, String img) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
        this.type = type;
        this.img = img;
    }

    public Memorial(String title, String content, String time, String type, String img) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.type = type;
        this.img = img;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
