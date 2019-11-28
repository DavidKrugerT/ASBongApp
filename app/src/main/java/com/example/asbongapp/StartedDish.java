package com.example.asbongapp;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class StartedDish {
    private ArrayList<StartedDishStatus>startedDishStatuses;

    public StartedDish(){
        startedDishStatuses = new ArrayList<StartedDishStatus>();
    }
    public ArrayList<StartedDishStatus> getStartedDishStatuses() {
        return startedDishStatuses;
    }

    public void setStartedDishStatuses(ArrayList<StartedDishStatus> startedDishStatuses) {
        this.startedDishStatuses = startedDishStatuses;
    }


public void addStartedDishStatus(StartedDishStatus startedDishStatus){this.startedDishStatuses.add(startedDishStatus);}


}
