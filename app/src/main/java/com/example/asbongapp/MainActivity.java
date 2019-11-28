package com.example.asbongapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import android.os.Handler;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView dishListView, orderListView, startedDishListView;
    private Button addRandomOrder;
    private Order order = new Order();
    private StartedDish startedDish = new StartedDish();
    private ArrayAdapter<DishStatus> dishAdapter;
    private ArrayAdapter<OrderStatus> orderAdapter;
    private ArrayAdapter<OrderStatus> startedDishAdapter;
    Handler timerHandler = new Handler();
    //Starts function every other second
    Runnable timerRunnable = new Runnable(){
        @Override
        public void run(){
            fetchDishesStatus();
            fetchOrdersStatus();
            timerHandler.postDelayed(this, 2000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find and define all objects on screen.
        dishListView = (ListView) findViewById(R.id.DishListView);
        orderListView = (ListView) findViewById(R.id.OrderListView);
        startedDishListView = (ListView) findViewById(R.id.StartedDishesListWiew);
        addRandomOrder = (Button) findViewById(R.id.StopPrintingEverySecond);

        //Adapter is needed to build a dynamic list.
        //First create an adapter and then set the adapter to the listview.
        order.addDishStatus(createDish());
        startedDish.addStartedDishStatus(createStartedDish());


        dishAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, order.getDishStatuses());
        dishListView.setAdapter(dishAdapter);

        orderAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, order.getOrderStatuses());
        orderListView.setAdapter(orderAdapter);

        timerHandler.postDelayed(timerRunnable, 2000);
        //Adding listener on button.
        addRandomOrder.setOnClickListener(this);



    }
    // stopbutton for runfunction
    @Override
    public void onClick(View view) {
        timerHandler.removeCallbacks(timerRunnable);
    }


    //Creating dishStatus with random stuff.
    private DishStatus createDish(){
        DishStatus dish = new DishStatus();
        dish.setFoodName(Math.random()>0.5?"Krabba":"Häst");
        dish.setTable(Math.random()>0.5?1:2);
        dish.setOrderNumber(Math.random()>0.5?1:2);
        dish.setTime((int) (Math.random()*100));
        dish.setDone(false);
        return dish;
    }
    private OrderStatus createOrder(){
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setAmountOfDoneDishes(0);
        orderStatus.setAmountOfTotalDishes(3);
        orderStatus.setOrderNumber(1);
        orderStatus.setTableNumber(3);
        return orderStatus;
    }
    private StartedDishStatus createStartedDish(){
        StartedDishStatus startedDishStatus = new StartedDishStatus();
        startedDishStatus.setFoodName(Math.random()>0.5?"Fisk":"Ost");
        startedDishStatus.setOrderNumber(Math.random()>0.5?1:2);
        startedDishStatus.setTime((int) (Math.random()*100));
        return startedDishStatus;
    }

    private void fetchDishesStatus() {
        //Adding crap into Order.dishStuses.
        order.addDishStatus(createDish());
        //Alert listView that something is changed.
        dishAdapter.notifyDataSetChanged();
        //sort list by descending time order
        order.sortDishesByTime();
    }

    private void fetchOrdersStatus() {
        //Adding crap into Order.orderStatses.
        order.addOrderStatus(createOrder());
        //Alert listView that something is changed.
        orderAdapter.notifyDataSetChanged();
        //sort list by descending time order

    }

}
