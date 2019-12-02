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

import org.json.JSONArray;
import org.json.JSONException;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    private ListView dishListView;
    private ListView orderListView;
    private ListView progressListView;
    private List<Dish> dishes = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();
    private List<Dish> progress = new ArrayList<>();


    private ArrayAdapter<Dish> dishAdapter;
    private ArrayAdapter<Order> orderAdapter;
    private ArrayAdapter<Dish> progressAdapter;
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find and define all objects on screen.
        dishListView = (ListView) findViewById(R.id.DishListView);
        orderListView = (ListView) findViewById(R.id.OrderListView);
        progressListView = (ListView) findViewById(R.id.ProgressListView);

        /**
        * Adapter is needed to build a dynamic list.
        * First create an adapter and then set the adapter to the listview.
        */

        dishAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, dishes);
        dishListView.setAdapter(dishAdapter);
        orderAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, orders);
        orderListView.setAdapter(orderAdapter);
        progressAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, progress);
        progressListView.setAdapter(progressAdapter);

        /**
         *
         * dishAdapter = new ArrayAdapter<DishStatus>
         *                 (getApplicationContext(), android.R.layout.simple_list_item_1, order.getDishStatuses()){
         *             @Override
         *             public View getView(int position, View convertView, ViewGroup parent){
         *                 // Get the Item from ListView
         *                 View view = super.getView(position, convertView, parent);
         *
         *                 // Initialize a TextView for ListView each Item
         *                 TextView tv = (TextView) view.findViewById(android.R.id.text1);
         *
         *                 // Set the text color of TextView (ListView Item)
         *                 tv.setTextColor(Color.BLACK);
         *                 tv.setMinHeight(0); // Min Height
         *                 tv.setMinimumHeight(0); // Min Height
         *                 tv.setHeight(100); // Height
         *                 tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
         *
         *                 tv.setTypeface(null, Typeface.BOLD);
         *                 tv.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
         *
         *
         *
         *
         *                 // Generate ListView Item using TextView
         *                 return view;
         *             }
         *         };
         *
         *
         *
         *             dishListView.setAdapter(dishAdapter);*/
        RunWithIntervall runWithIntervall = new RunWithIntervall();
        timer.schedule(runWithIntervall, 0, 2000);
    }

    private void addDish(List<Dish> tmpDishes) throws MalformedURLException {
        //Add items.
        for(int i = 0; i < tmpDishes.size(); i++){
            dishes.add(tmpDishes.get(i));
        }
        //Alert listView that something is changed.
        dishAdapter.notifyDataSetChanged();
    }

    private void updateOrderColumn() {
        Integer tmpTot;
        Integer tmpDone;

        orders.removeAll(orders);

        for (int i = 1; i <= 10; i++) {
            tmpTot = 0;
            tmpDone = 0;
            for (int j = 0; j < dishes.size(); j++) {
                if (dishes.get(j).getOrderNumber() == i) {
                    tmpTot++;
                    if (dishes.get(j).getDone()){
                        tmpDone++;
                    }
                }
            }
            if (tmpTot != 0) {
                Order order = new Order();
                order.setName(i);
                order.setTot(tmpTot);
                order.setDone(tmpDone);
                orders.add(order);
            }
        }
        orderAdapter.notifyDataSetChanged();
    }

    private class RunWithIntervall extends TimerTask{
        @Override
        public void run() {
            try {
                new GetOrderFromAPI().execute();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }














    private class GetOrderFromAPI extends AsyncTask<Void, Void, List<Dish>> {
        InputStream inputStream = null;

        private GetOrderFromAPI() throws MalformedURLException {
        }
        @Override
        protected List<Dish> doInBackground(Void... voids) {
            URL url;
            List<Dish> tmpDishes = new ArrayList<>();
            try {
                if (dishes.isEmpty() && progress.isEmpty()){
                    url = new URL("http://10.250.124.8:8080/Project-WebApp/webresources/entity.dish/");
                } else {
                    url = new URL("http://10.250.124.8:8080/Project-WebApp/webresources/entity.dish/" + dishes.size()+progress.size() + "/10000");
                }
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept", "application/json");

                urlConnection.setRequestMethod("GET");
                inputStream = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                br.close();
                tmpDishes = parser(stringBuilder.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return tmpDishes;
        }

        @Override
        protected void onPostExecute(List<Dish> tmp) {
            super.onPostExecute(tmp);

            try {
                addDish(tmp);
                updateOrderColumn();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }


        /**
         *
         * @param str
         * @return
         * @throws JSONException
         */
        private List<Dish> parser(String str) throws JSONException {
            List<Dish> tmpDishes = new ArrayList<>();
            JSONArray jArray = new JSONArray(str);
            for (int i = 0; i < jArray.length(); i++) {
                Dish tmpDish = new Dish();
                JSONObject jObject = jArray.getJSONObject(i);
                tmpDish.setDishid(jObject.getInt("dishid"));
                tmpDish.setName(jObject.getString("name"));
                tmpDish.setPrice(jObject.getDouble("price"));
                tmpDish.setCookingTime(jObject.getInt("cookingTime"));
                tmpDish.setOrderNumber(jObject.getInt("orderNumber"));
                tmpDish.setTableNumber(jObject.getInt("tableNumber"));
                tmpDish.setDone(jObject.getBoolean("done"));
                tmpDishes.add(tmpDish);
            }
            return tmpDishes;
        }
    }

}
