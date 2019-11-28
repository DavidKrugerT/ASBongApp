package com.example.asbongapp;

public class DishStatus implements Comparable<DishStatus>{
    private String foodName;
    private Integer orderNumber;
    private Integer time;
    private Integer table;

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    private Integer price;
    private Boolean done;


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
    public int compareTo(DishStatus dishStatus) {
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
        return foodName + "\t\t\t" + time + "\t\t\t" + table + "\t\t\t" + done + "\t\t\t Orderid: " + orderNumber;
    }
}
