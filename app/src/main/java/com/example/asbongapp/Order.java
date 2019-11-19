package com.example.asbongapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {
    private ArrayList<DishStatus> dishStatuses;
    private List<OrderStatus> orderStatuses;

    public Order() {

        dishStatuses = new ArrayList<DishStatus>();
        orderStatuses = new ArrayList<OrderStatus>();
    }
    public ArrayList<DishStatus> getDishStatuses() {
        return dishStatuses;
    }

    public void setDishStatuses(ArrayList<DishStatus> dishStatuses) {
        this.dishStatuses = dishStatuses;
    }

    public void addDishStatus(DishStatus dishStatus){
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
