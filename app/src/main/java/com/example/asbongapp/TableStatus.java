package com.example.asbongapp;

public class TableStatus {
    private Integer name;
    private Integer amountOfTotalDishes;
    private Integer amountOfDoneDishes;

    public Integer getName() {
        return name;
    }

    public void setName(Integer name) {
        this.name = name;
    }

    public Integer getAmountOfTotalDishes() {
        return amountOfTotalDishes;
    }

    public void setAmountOfTotalDishes(Integer amountOfTotalDishes) {
        this.amountOfTotalDishes = amountOfTotalDishes;
    }

    public Integer getAmountOfDoneDishes() {
        return amountOfDoneDishes;
    }

    public void setAmountOfDoneDishes(Integer amountOfDoneDishes) {
        this.amountOfDoneDishes = amountOfDoneDishes;
    }

    @Override
    public String toString() {
        return name + "\n" + amountOfDoneDishes + "/" + amountOfTotalDishes;
    }
}
