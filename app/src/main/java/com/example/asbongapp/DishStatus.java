package com.example.asbongapp;

public class DishStatus {
    private String name;
    private Integer time;
    private Integer table;
    private Boolean done;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getTable() {
        return table;
    }

    public void setTable(Integer table) {
        this.table = table;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return name + "\t\t\t" + time + "\t\t\t" + table + "\t\t\t" + done;
    }
}
