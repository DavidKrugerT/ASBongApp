package com.example.asbongapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ListView dishListView;
    private ListView orderListView;
    private ListView progressListView;
    private List<Dish> dishes = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();
    private List<Dish> progress = new ArrayList<>();
    private List<Integer> alreadyFetchedDishes = new ArrayList<>();

    private ArrayAdapter<Dish> dishAdapter;
    private ArrayAdapter<Order> orderAdapter;
    private ArrayAdapter<Dish> progressAdapter;
    private Timer intervalTimer = new Timer();
    private Timer onTheMiunuteTimer = new Timer();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find and define all objects on screen.
        dishListView = (ListView) findViewById(R.id.DishListView);
        orderListView = (ListView) findViewById(R.id.OrderListView);
        progressListView = (ListView) findViewById(R.id.ProgressListView);

        dishListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                progress.add(dishes.get(i));
                dishes.remove(i);
                progressAdapter.notifyDataSetChanged();
                dishAdapter.notifyDataSetChanged();
            }
        });
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
        intervalTimer.schedule(runWithIntervall, 0, 2000);
        RunEveryMinute runEveryMinute = new RunEveryMinute();
        onTheMiunuteTimer.schedule(runEveryMinute, 0, 6000);
    }

    private void addDish(List<Dish> tmpDishes) throws MalformedURLException {
        //Add items.
        for(int i = 0; i < tmpDishes.size(); i++){
            dishes.add(tmpDishes.get(i));
        }
        //Alert listView that something is changed.
        dishAdapter.notifyDataSetChanged();
    }

    private void updateOrderColumn(List<Dish> tmpDishes) {
        HashMap<Integer, Integer>  countingOrders = new HashMap<>();
        for (Dish dish : tmpDishes) {
            if (countingOrders.containsKey(dish.getOrderNumber())) {
                countingOrders.put(dish.getOrderNumber(), countingOrders.get(dish.getOrderNumber()) + 1);
            } else {
                countingOrders.put(dish.getOrderNumber(), 1);
            }

        }

        for (HashMap.Entry<Integer, Integer> entry : countingOrders.entrySet()) {
            Order tmpOrder = new Order();
            tmpOrder.setName(entry.getKey());
            tmpOrder.setTot(entry.getValue());
            tmpOrder.setDone(0);

            orders.add(tmpOrder);
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
                    url = new URL("http://10.250.124.20:8080/Project-WebApp/webresources/entity.dish/");
                } else {
                    url = new URL("http://10.250.124.20:8080/Project-WebApp/webresources/entity.dish/" + (dishes.size()+progress.size()) + "/10000");
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
                inputStream.close();
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
        protected void onPostExecute(List<Dish> tmpDishes) {
            super.onPostExecute(tmpDishes);

            try {
                addDish(tmpDishes);
                updateOrderColumn(tmpDishes);
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
                if (!alreadyFetchedDishes.contains(tmpDish.getDishid())) {
                    tmpDishes.add(tmpDish);
                    alreadyFetchedDishes.add(tmpDish.getDishid());
                }
            }
            return tmpDishes;
        }



    }

    class RunEveryMinute extends TimerTask {

        @Override
        public void run() {
            for (int i = 0; i < progress.size(); i++) {
                Dish dish = progress.get(i);
                dish.setCookingTime(dish.getCookingTime() - 1);
                if (dish.getCookingTime().equals(0)) {
                    Integer name = progress.get(i).getOrderNumber();
                    for (int j = 0; j < orders.size(); j++) {
                        Order order = orders.get(j);
                        if (order.getName().equals(name)) {
                            order.setDone(order.getDone() + 1);
                            if (order.getDone().equals(order.getTot())) {
                                orders.remove(j);
                                List<Dish> doneDishes = getDoneDishes(progress.get(i).getOrderNumber());
                                for (Dish doneDish : doneDishes) {
                                    updateDB(doneDish);
                                }


                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast toast = Toast.makeText(MainActivity.this, "Order is done", Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                });

                            }
                        }
                    }
                    progress.remove(i);
                }
            }

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressAdapter.notifyDataSetChanged();
                    orderAdapter.notifyDataSetChanged();
                }
            });
        }


        private List<Dish> getDoneDishes(Integer orderNumber) {
            InputStream inputStream = null;
            URL url;
            List<Dish> tmpDishes = new ArrayList<>();
            try {
                url = new URL("http://10.250.124.20:8080/Project-WebApp/webresources/entity.dish/order/" + orderNumber);
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
                inputStream.close();
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

        private void updateDB(Dish doneDish) {
            URL url;
            List<Dish> tmpDishes = new ArrayList<>();
            try {
                url = new URL("http://10.250.124.20:8080/Project-WebApp/webresources/entity.dish/" + doneDish.getDishid());

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("PUT");
                urlConnection.setRequestProperty("Content-Type", "application/json");


                JSONObject jObject= new JSONObject();

                jObject.put("done", true);
                jObject.put("dishid", doneDish.getDishid());
                jObject.put("name", doneDish.getName());
                jObject.put("orderNumber", doneDish.getOrderNumber());
                jObject.put("price", doneDish.getPrice());
                jObject.put("tableNumber", doneDish.getTableNumber());
                jObject.put("cookingTime", doneDish.getCookingTime());


                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                bWriter.write(jObject.toString());
                bWriter.flush();
                bWriter.close();
                out.close();
                System.out.println(urlConnection.getResponseCode());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
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

    public class myOwnToast extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
                                                                  
}
