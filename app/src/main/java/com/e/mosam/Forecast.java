package com.e.mosam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Forecast extends AppCompatActivity implements View.OnClickListener {
static String value;
 static String []time;
static String[]cloud;
    private ProgressBar progressBar;
static String[] sTemprature;
static String par="";
    RecyclerView recyclerView;
TextView minTemp,maxTemp,tWind,humidty ,temprature ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        par="\u2103";
        Button deg = findViewById(R.id.dig);
        Button fahr = findViewById(R.id.farn);
deg.setOnClickListener(this);
fahr.setOnClickListener(this);


            progressBar = findViewById(R.id.forcast_progressbar);
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager =new LinearLayoutManager(this);

            recyclerView.setLayoutManager(layoutManager);
             temprature = findViewById(R.id.tempreature);
        minTemp = findViewById(R.id.min_temp);
        maxTemp = findViewById(R.id.max_temp);
        tWind = findViewById(R.id.wind_text);
        humidty = findViewById(R.id.humidty_text);
         Process process = new Process();
         if(value==null){
           value = "kharar";
       }
        process.execute("https://openweathermap.org/data/2.5/forecast/hourly?q="+value+",DE&appid=439d4b804bc8187953eb36d2a8c26a02");
          progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View v) {
         String fahrenheit="\u2109";
         String degree="\u2103";

        switch (v.getId()){

            case R.id.dig:
            par=degree;

                break;
            case R.id.farn:
                par=fahrenheit;
                break;
        }
    }

    class Process extends AsyncTask<String ,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
          String result ="";
            URL url = null;
            HttpURLConnection httpURLConnection = null;

                    try{
                    url = new URL(strings[0]);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                       InputStream inputStream = httpURLConnection.getInputStream();
                        InputStreamReader reader = new InputStreamReader(inputStream);
                        int data = reader.read();
                        while (data!=-1){
                            char c = (char) data;
                            result+=c;
                            data = reader.read();
                        }

                        Log.i("msg ", "doInBackground: "+result);
                return result;
                    }

   catch (Exception e){e.printStackTrace();}

return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

             JSONObject jsonObject = new JSONObject(s);
             String jsonData = jsonObject.getString("list");
             JSONArray jsonArray = new JSONArray(jsonData);
              int size = jsonArray.length();
                sTemprature = new String[size];
               cloud = new String[size];
                time = new String[size];

                for (int i = 0; i <size; i++) {

                 //Storing the data into string array of hourly forcast

                    //sTemprature = new String[i];
                  //  sTemprature = new String[i];

                    JSONObject object = jsonArray.getJSONObject(i);
                    Log.i(""+i, "main: "+object.getString("main"));
                    Log.i(""+i, "wind: "+object.getString("wind"));
                    // another array for the wheather ifno in
                    JSONArray weather = new JSONArray(object.getString("weather"));
                    for (int j = 0; j <weather.length(); j++) {
                       JSONObject description = weather.getJSONObject(j);
                        cloud[i]=description.getString("description");
                    }


                    JSONObject main = object.getJSONObject("main");
                    JSONObject wind = object.getJSONObject("wind");
                     sTemprature[i]=main.getString("temp");
                    //cloud[i]=weather.getString("description");
                    time[i]=object.getString("dt_txt");

                    Log.i(""+i, "onPostExecute: "+main.getString("temp"));

                  // primary info for the weather is inside this if(i==o)


                            progressBar.setVisibility(View.GONE);

                   if (i==0){
                       // adding the main temprature
                       float mainTemp=  Float.valueOf(main.getString("temp"));
                        temprature.setText(mainTemp+" "+par);

                    // adding the min - temp
                       float minTempI=  Float.valueOf(main.getString("temp_min"));
                       Log.i("", "min_temp: "+main.getString("temp_min"));
                       minTemp.setText(minTempI+par);

                      // max temp

                       float maxTempI=  Float.valueOf(main.getString("temp_max"));
                       Log.i("", "min_temp: "+main.getString("temp_max"));
                       maxTemp.setText(maxTempI+par);
                       // humidty
                       Log.i("", "humidity: "+main.getString("humidity"));
                       humidty.setText(main.getString("humidity"));
                       // speed
                       Log.i("", "speed: "+wind.getString("speed"));
                       tWind.setText(wind.getString("speed"));

                   }



                }
                recyclerView.setAdapter(new MosamAdapter(sTemprature,cloud,time));

                ;

             /*   for (int j = 0; j <sTemprature.length ; j++) {
                    Log.i(""+j, "sTeprature: "+sTemprature[j]);
                    Log.i(""+j, "time: "+time[j]);
                    Log.i(""+j, "description: "+cloud[j]);
                }*/

               /* String temp="";
                JSONObject jsonObject = new JSONObject(s);
                String jsonData = jsonObject.getString("list");
                Log.i("list", "onPostExecute: "+jsonData);
                JSONArray jsonArray = new JSONArray(jsonData);
                boolean visited= true;
                for (int i = 0; i <jsonArray.length() ; i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                   if(visited){
                       temp =object.getString("main") ;
                  visited= false;
                   }
                    Log.i("", "onPostExecute: "+temp);
                }*/
              //  updateHour();
        }



        catch (Exception e){e.printStackTrace();}
        }

    }



    // old code for filling the imformation
    /*
private void updateHour(){
 int[] aTemp ={R.id.temp_zero,R.id.temp_one,R.id.temp_two,R.id.temp_three,R.id.temp_four,R.id.temp_five,};
    int[] aTime ={R.id.time_zero,R.id.time_one,R.id.time_two,R.id.time_three,R.id.time_four,R.id.time_five,};
    int[] aImage ={R.id.image_zero,R.id.image_one,R.id.image_two,R.id.image_three,R.id.image_four,R.id.image_five,};
    for (int i = 0; i <aTemp.length ; i++) {
        // setting temprature for the forcast
        TextView textView = findViewById(aTemp[i]);
         textView.setText(sTemprature[i]+degree);
            //setting time for the forcast
        TextView textview_time = findViewById(aTime[i]);
        textview_time.setText(time[i]);

    }
}*/
}
