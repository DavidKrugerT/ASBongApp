package com.example.asbongapp;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private ArrayList<DishStatus> dishStatuses;
    private List<TableStatus> tableStatuses;

    public Order() {
        dishStatuses = new ArrayList<DishStatus>();
        tableStatuses = new ArrayList<TableStatus>();
    }
    public ArrayList<DishStatus> getDishStatuses() {
        return dishStatuses;
    }

    public void setDishStatuses(ArrayList<DishStatus> dishStatuses) {
        this.dishStatuses = dishStatuses;
    }

    public List<TableStatus> getTableStatuses() {
        return tableStatuses;
    }

    public void setTableStatuses(List<TableStatus> tableStatuses) {
        this.tableStatuses = tableStatuses;
    }
}
