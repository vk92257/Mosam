package com.e.mosam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
   TextView textView ,result;

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

                JSONObject jsonObject =  new JSONObject(s);
                String json =  jsonObject.getString("weather");
                Log.i(" weather ", json);
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0 ;i < jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    Log.i("main ", object.getString("main"));
                    result.setText("  description  :::  "+object.getString("description"));

                    Log.i(" description", object.getString("description"));

                }

            }catch (Exception e ){
                e.printStackTrace();
            }

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (TextView) findViewById(R.id.result);


    }
public void weather(View view){
     textView = (TextView) findViewById(R.id.textView);

     String value = textView.getText().toString();

     Toast.makeText(this, value,Toast.LENGTH_LONG).show();
     DownloadTask    task = new DownloadTask();
    task.execute("https://openweathermap.org/data/2.5/weather?q="+value+"&appid=b6907d289e10d714a6e88b30761fae22");


}

}
