package com.example.asbongapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView dishListView, tableListView;
    private Button addRandomOrder;
    private Order order = new Order();
    private ArrayAdapter<DishStatus> dishAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find and define all objects on screen.
        dishListView = (ListView) findViewById(R.id.DishListView);
        tableListView = (ListView) findViewById(R.id.TableListView);
        addRandomOrder = (Button) findViewById(R.id.AddRandomOrder);

        //Adapter is needed to build a dynamic list.
        //First create an adapter and then set the adapter to the listview.
        dishAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, order.getDishStatuses());
        dishListView.setAdapter(dishAdapter);

        //Adding listner on button.
        addRandomOrder.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {

        //Adding crap into Order.dishStuses.
        ArrayList<DishStatus> tmpDishes = order.getDishStatuses();
        tmpDishes.add(createDish());
        order.setDishStatuses(tmpDishes);

        //Alert listView that something is changed.
        dishAdapter.notifyDataSetChanged();
    }


    //Creating dishStatus with random stuff.
    private DishStatus createDish(){
        DishStatus dish = new DishStatus();
        dish.setName(Math.random()>0.5?"Krabba":"Häst");
        dish.setTable(Math.random()>0.5?1:2);
        dish.setTime((int) (Math.random()*100));
        dish.setDone(false);
        return dish;
    }
}
