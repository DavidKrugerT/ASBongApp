package com.example.asbongapp;

public class StartedDishStatus {
    private Integer orderNumber;
    private String foodName;
    private Integer time;

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return orderNumber + "\t\t" + foodName + "\t\t" + time;
    }
}
