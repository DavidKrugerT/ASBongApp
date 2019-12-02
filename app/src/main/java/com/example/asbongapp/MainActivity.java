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

    private ListView dishListView;
    private List<Dish> dishes = new ArrayList<>();

    private ArrayAdapter<Dish> dishAdapter;
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find and define all objects on screen.
        dishListView = (ListView) findViewById(R.id.DishListView);

        /**
        * Adapter is needed to build a dynamic list.
        * First create an adapter and then set the adapter to the listview.
        */

        dishAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, dishes);
        dishListView.setAdapter(dishAdapter);

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
            dishes.add(i, tmpDishes.get(i));
        }
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

    private class GetOrderFromAPI extends AsyncTask<Void, Void, List<Dish>> {
        URL url = new URL("http://10.250.124.26:8080/Project-WebApp/webresources/entity.dish/");
        //URL urlCount = new URL("http://10.250.119.122:8080/Project-WebApp/webresources/entity.dish/count");

        InputStream inputStream = null;

        private GetOrderFromAPI() throws MalformedURLException {
        }


        @Override
        protected List<Dish> doInBackground(Void... voids) {
            List<Dish> tmpDishes = new ArrayList<>();
            try {
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
                try {
                    tmpDishes = parseXML(stringBuilder.toString());
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return tmpDishes;
        }

        @Override
        protected void onPostExecute(List<Dish> tmp) {
            super.onPostExecute(tmp);

            try {
                addDish(tmp);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        private List<Dish> parseXML(String str) throws IOException, SAXException, ParserConfigurationException {
            List<Dish> tmpDishes = new ArrayList<>();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(str)));
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("dish");
            for (int temp = 0; temp < nodeList.getLength(); temp++){
                Node node = nodeList.item(temp);
                Dish dish = new Dish();
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element)node;
                    dish.setDishid(Integer.valueOf(element.getElementsByTagName("dishid").item(0).getTextContent()));
                    dish.setName(element.getElementsByTagName("name").item(0).getTextContent());
                    dish.setDescription(element.getElementsByTagName("description").item(0).getTextContent());
                    dish.setPrice((element.getElementsByTagName("price").item(0).getTextContent()));
                    dish.setCookingTime((element.getElementsByTagName("cookingTime").item(0).getTextContent()));
                }
                tmpDishes.add(dish);
            }
            return tmpDishes;
        }
    }
}
