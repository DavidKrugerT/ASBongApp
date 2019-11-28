package com.example.asbongapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import android.os.Handler;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
            try {
                fetchDishesStatus();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
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
        try {
            order.addDishStatus(createDish());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
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
    private DishStatus createDish() throws MalformedURLException {
        DishStatus dish = new DishStatus();
      /*  do {
            GetOrderFromAPI getOrderFromAPI = new GetOrderFromAPI();
            getOrderFromAPI.execute(dish);
        }while(dish.getFoodName() == null);*/

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

    private void fetchDishesStatus() throws MalformedURLException {
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
    private class GetOrderFromAPI extends AsyncTask<DishStatus, DishStatus, DishStatus> {
        URL url = new URL("http://10.250.119.122:8080/Project-WebApp/webresources/entity.dish/");
        //URL urlCount = new URL("http://10.250.119.122:8080/Project-WebApp/webresources/entity.dish/count");

        InputStream inputStream = null;
        private GetOrderFromAPI() throws MalformedURLException {
        }

        @Override
        protected void onPostExecute(DishStatus dish) {
            super.onPostExecute(dish);
        }

        @Override
        protected DishStatus doInBackground(DishStatus... dishStatuses) {
            DishStatus dish = dishStatuses[0];
            try {
/*
                HttpURLConnection urlConnectionCount = (HttpURLConnection) urlCount.openConnection();
                urlConnectionCount.setRequestMethod("GET");
                inputStream = urlConnectionCount.getInputStream();
                BufferedReader brCount = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilderCount = new StringBuilder();
                String line1;
                while ((line1 = brCount.readLine()) !=null) {
                    stringBuilderCount.append(line1).append("\n");
                }

                Log.d(this.getClass().toString(), "test count************" + stringBuilderCount.toString());

*/


                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");

                urlConnection.setRequestMethod("GET");
                inputStream = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = br.readLine()) !=null) {
                    stringBuilder.append(line).append("\n");
                }
                br.close();
                Log.d(this.getClass().toString(), stringBuilder.toString());
                try {
                    dish = parseXML(stringBuilder.toString());
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }

            return dish;
        }

        private DishStatus parseXML(String str) throws IOException, SAXException, ParserConfigurationException {
            DishStatus dish = new DishStatus();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(str)));
            doc.getDocumentElement().normalize();
            //Element root = doc.getDocumentElement();
            Log.d(this.getClass().toString(), "test************" + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("dish");
            for (int temp = 0; temp < nodeList.getLength(); temp++){
                Node node = nodeList.item(temp);
                Log.d(this.getClass().toString(), "test22222************" + node.getNodeName());
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element)node;
                    Log.d(this.getClass().toString(), "test333************" + element.getElementsByTagName("price").item(0).getTextContent());
                    dish.setTime(15);
                    dish.setDone(true);
                    dish.setTable(1);
                    dish.setFoodName(element.getElementsByTagName("name").item(0).getTextContent());
                    dish.setPrice(10);
                }
            }
            //
            return dish;
        }
    }
}
