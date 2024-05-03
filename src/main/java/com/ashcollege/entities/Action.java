package com.ashcollege.entities;

public class Action {

    private int id;
    private int type;
    private int timing;
    private int goX;
    private int goY;


    public Action() {
    }

    public Action(int type, int timing, int goX, int goY) {
        this.type = type;
        this.timing = timing;
        this.goX = goX;
        this.goY = goY;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTiming() {
        return timing;
    }

    public void setTiming(int timing) {
        this.timing = timing;
    }

    public int getGoX() {
        return goX;
    }

    public void setGoX(int goX) {
        this.goX = goX;
    }

    public int getGoY() {
        return goY;
    }

    public void setGoY(int goY) {
        this.goY = goY;
    }
}
