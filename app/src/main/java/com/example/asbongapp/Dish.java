package com.example.asbongapp;

public class Dish implements Comparable<Dish>{
    private Integer dishid;
    private String name;
    private Double price;
    private Integer cookingTime;
    private Integer orderNumber;
    private Integer tableNumber;
    private Boolean done;


    public Integer getDishid() {
        return dishid;
    }

    public void setDishid(Integer dishid) {
        this.dishid = dishid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(Integer cookingTime) {
        this.cookingTime = cookingTime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    @Override
    public int compareTo(Dish dishStatus) {
        if(Integer.valueOf(this.getCookingTime()) < Integer.valueOf(dishStatus.getCookingTime())){
            return -1;
        }
        else if(this.getCookingTime() == dishStatus.getCookingTime()){
            return 0;
        }
        else return 1;
    }

    @Override
    public String toString() {
        return dishid +  ",   " + name + ",         Cooking Time: " + cookingTime + ",          Order: " + orderNumber;
    }


}
