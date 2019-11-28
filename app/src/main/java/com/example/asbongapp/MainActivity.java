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
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    private ListView dishListView, orderListView, startedDishListView;
    private Button addRandomOrder;
    private List<Dish> dishes = new ArrayList<>();
    private StartedDish startedDish = new StartedDish();

    private ArrayAdapter<Dish> dishAdapter;
    private ArrayAdapter<OrderStatus> orderAdapter;
    private ArrayAdapter<Dish> progressAdapter;
    Handler timerHandler = new Handler();
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find and define all objects on screen.
        dishListView = (ListView) findViewById(R.id.DishListView);
        orderListView = (ListView) findViewById(R.id.OrderListView);
        startedDishListView = (ListView) findViewById(R.id.ProgressListWiew);

        /**
        * Adapter is needed to build a dynamic list.
        * First create an adapter and then set the adapter to the listview.
        */

        dishAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, dishes);
        dishListView.setAdapter(dishAdapter);

        RunWithIntervall runWithIntervall = new RunWithIntervall();
        timer.schedule(runWithIntervall, 0, 2000);
    }

    private void addDish(Dish dish) throws MalformedURLException {
        //Add items.
        dishes.add(dish);
        //Alert listView that something is changed.
        dishAdapter.notifyDataSetChanged();
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

    private class GetOrderFromAPI extends AsyncTask<Void, Void, Dish> {
        URL url = new URL("http://192.168.100.179:8080/Project-WebApp/webresources/entity.dish/");
        //URL urlCount = new URL("http://10.250.119.122:8080/Project-WebApp/webresources/entity.dish/count");

        InputStream inputStream = null;

        private GetOrderFromAPI() throws MalformedURLException {
        }


        @Override
        protected Dish doInBackground(Void... voids) {
            Dish dish = null;
            try {
                System.out.println("New Try");
                dish = new Dish();
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");

                urlConnection.setRequestMethod("GET");
                inputStream = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
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
            } catch (IOException e) {
                e.printStackTrace();
            }

            return dish;
        }

        @Override
        protected void onPostExecute(Dish dish) {
            super.onPostExecute(dish);

            Boolean exists = false;
            for (int i = 0; i < dishes.size(); i++){
                if (dishes.get(i).getName().equals(dish.getName())) {
                    exists = true;
                }
            }
            if (!exists) {
                try {
                    addDish(dish);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        private Dish parseXML(String str) throws IOException, SAXException, ParserConfigurationException {
            Dish dish = new Dish();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(str)));
            doc.getDocumentElement().normalize();
            //Element root = doc.getDocumentElement();
            NodeList nodeList = doc.getElementsByTagName("dish");
            for (int temp = 0; temp < nodeList.getLength(); temp++){
                Node node = nodeList.item(temp);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element)node;
                    dish.setTime(15);
                    dish.setDone(true);
                    dish.setTable(1);
                    dish.setName(element.getElementsByTagName("name").item(0).getTextContent());
                    dish.setPrice(10);
                }
            }
            return dish;
        }
    }
}
