package com.example.asbongapp;

public class Order {
    private Integer name;
    private Integer tot;
    private Integer done;

    public Integer getTot() {
        return tot;
    }

    public void setTot(Integer tot) {
        this.tot = tot;
    }

    public Integer getDone() {
        return done;
    }

    public void setDone(Integer done) {
        this.done = done;
    }

    public Integer getName() {
        return name;
    }

    public void setName(Integer name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Order: " + name + ", " + done + " of " + tot + " is done.";
    }



}
