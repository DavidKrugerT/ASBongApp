package com.example.asbongapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {
    private ArrayList<Dish> dishStatuses;
    private List<OrderStatus> orderStatuses;

    public Order() {
        dishStatuses = new ArrayList<Dish>();
        orderStatuses = new ArrayList<OrderStatus>();
    }
    public ArrayList<Dish> getDishStatuses() {
        return dishStatuses;
    }

    public void setDishStatuses(ArrayList<Dish> dishStatuses) {
        this.dishStatuses = dishStatuses;
    }

    public void addDishStatus(Dish dishStatus){
        this.dishStatuses.add(dishStatus);
    }

    public void addOrderStatus(OrderStatus orderStatus){
        this.orderStatuses.add(orderStatus);
    }

    public List<OrderStatus> getOrderStatuses() {
        return orderStatuses;
    }

    public void setOrderStatuses(List<OrderStatus> orderStatuses) {
        this.orderStatuses = orderStatuses;
    }
    public void sortDishesByTime(){
        Collections.sort(dishStatuses, Collections.reverseOrder());
    }
}
