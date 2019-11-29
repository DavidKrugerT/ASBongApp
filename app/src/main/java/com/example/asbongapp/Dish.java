package com.example.asbongapp;

public class Dish implements Comparable<Dish>{
    private Integer dishid;
    private String name;
    private String description;
    private String cookingTime;
    private String price;


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
        return "Dish = [Name = " + name + "\t\t\t Description = " + description + "\t\t\t Time = " + cookingTime + "]";
    }

}
