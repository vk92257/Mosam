package com.e.mosam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
   TextView textView ,result,secondText;
   ImageView icon;
   private ProgressBar progressBar;
   Button bLocation;
   private LinearLayout forcastLayout,forcastButton;
   LinearLayout iconLayout;
   LocationManager locationManager;
   LocationListener locationListener;

    public  class DownloadTask extends AsyncTask<String,Void ,String>{

        @Override
        protected String doInBackground(String... urls) {
           String resutl ="";
            URL url ;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL ( urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in =  urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1){
                    char c = (char) data ;
                    resutl += c;
                    data = reader.read();
             }
                return resutl ;
            }
            catch (Exception e ){
                e.printStackTrace();
                return  null;
            }
         }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
             try {
                 /*   JSONObject jsonObject = new JSONObject(s) ;

                    String info = jsonObject.getString("main");

                Log.i("temp is ", "onPostExecute: "+info);
                JSONArray jsonArray = new JSONArray(info);
                    for (int i = 0; i <jsonArray.length() ; i++) {
                        Log.i("temp is ", "onPostExecute: "+jsonArray.getJSONObject(i));
                }*/
                    JSONObject jsonObject =  new JSONObject(s);
                String json =  jsonObject.getString("weather");
                Log.i(" weather ", json);
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0 ;i < jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    Log.i("main ", object.getString("main"));
                    progressBar.setVisibility(View.INVISIBLE);
                    forcastLayout.setVisibility(View.VISIBLE);
                        icon = findViewById(R.id.icon);
                        iconLayout= findViewById(R.id.icon_layout);
                        iconLayout.setVisibility(View.VISIBLE);
                        icon.setImageResource(R.drawable.cloud);
                        result.setText(object.getString("main" ));
                        secondText .setText(object.getString("description"));
                }

            }catch (Exception e ){
                e.printStackTrace();
            }
             finally {
                 progressBar.setVisibility(View.INVISIBLE);
             }

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       Drawable drawable =getResources().getDrawable(R.drawable.custom_progressbar);
        progressBar = findViewById(R.id.progress_circular);
        progressBar.setProgressDrawable(drawable);
        forcastLayout = findViewById(R.id.forcast_layout);
       forcastButton = findViewById(R.id.forcast_button);
        //getting the location
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        result = (TextView) findViewById(R.id.result);
        secondText = findViewById(R.id.second_text);

    }


    // on click listenre method
public void weather(View view){

    switch (view.getId()){
        case R.id.search:
            progressBar.setVisibility(View.VISIBLE);
            textView = (TextView) findViewById(R.id.textView);
            String value = textView.getText().toString();
            Forecast.value = value;
            Toast.makeText(this, value,Toast.LENGTH_LONG).show();
            DownloadTask    task = new DownloadTask();
            task.execute("https://openweathermap.org/data/2.5/weather?q="+value+"&appid=439d4b804bc8187953eb36d2a8c26a02");
            break;


        case R.id.forcast_button:
            Intent intent = new Intent(this,Forecast.class);
             startActivity(intent);

            break;
        case R.id.location:
            bLocation = findViewById(R.id.location);
            bLocation.setBackgroundResource(R.drawable.locatio_two);
            currentLocaton();
            break;

    }



}
   /// my current location mehtod
    private void currentLocaton() {
         locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    if (listAddresses != null && listAddresses.size()>0){
                        String address = " ";
                        if(listAddresses.get(0).getLocality()!= null){
                            address = listAddresses.get(0).getLocality();

                            DownloadTask    task = new DownloadTask();
                            task.execute("https://openweathermap.org/data/2.5/weather?q="+address+"&appid=b6907d289e10d714a6e88b30761fae22");
                            Forecast.value=address;
                          //Toast.makeText(MainActivity.this,  "onLocationChanged: "+address, Toast.LENGTH_SHORT).show();

                        }
                    }
                }catch (Exception e ){
                    e.printStackTrace();
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        // if  user hasn't give the permission than we are asking him for the permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String [] {Manifest.permission.ACCESS_FINE_LOCATION},1);
        }// if user has given the permission
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }

    }

//          default method which is having the access to all the user request in the app


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // checking if the grant result is having something and request is granted
        if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
               locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
        }
    }
}
