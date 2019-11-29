package com.example.asbongapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Order {
    private Integer orderingid;
    private Date ordertime;
    private Boolean isDone;
    private Integer diningTable;
    private List<Dish> dishes;

    public Integer getOrderingid() {
        return orderingid;
    }

    public void setOrderingid(Integer orderingid) {
        this.orderingid = orderingid;
    }

    public Date getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(Date ordertime) {
        this.ordertime = ordertime;
    }

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    public Integer getDiningTable() {
        return diningTable;
    }

    public void setDiningTable(Integer diningTable) {
        this.diningTable = diningTable;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    @Override
    public String toString() {
        return "Dish = [Orderingid = " + orderingid + "\t\t\t ordertime = " + ordertime + "\t\t\t isDone = " + isDone + "]";
    }
}
