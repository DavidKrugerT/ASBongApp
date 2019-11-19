package com.example.asbongapp;

public class OrderStatus {
    private Integer orderNumber;
    private Integer tableNumber;
    private Integer amountOfTotalDishes;
    private Integer amountOfDoneDishes;

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer name) {
        this.orderNumber = name;
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
    public Integer getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    @Override
    public String toString() {
        return orderNumber + "\n" + amountOfDoneDishes + "/" + amountOfTotalDishes;
    }
}
