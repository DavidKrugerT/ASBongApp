package com.example.asbongapp;

public class Dish implements Comparable<Dish>{
    private String name;
    private Integer time;
    private Integer table;
    private Integer price;
    private Boolean done;

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

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
    public int compareTo(Dish dishStatus) {
        if(this.getTime() < dishStatus.getTime()){
            return -1;
        }
        else if(this.getTime() == dishStatus.getTime()){
            return 0;
        }
        else return 1;
    }

    @Override
    public String toString() {
        return "Dish = [name = " + name + "]";
    }
}
