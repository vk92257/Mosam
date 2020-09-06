package com.e.mosam;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;

public class MosamAdapter extends RecyclerView.Adapter<MosamAdapter.MosamViewHolder> {


   String [] aTemp,aCloud,aTime;
    public MosamAdapter(String [] aTemp, String [] aCloud, String[] aTime) {
        this.aTemp = aTemp;
        this.aCloud = aCloud;
        this.aTime = aTime;

    }

    @NonNull
    @Override
    public MosamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View v =LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_hourly_forcast,parent,false);
        MosamViewHolder holder = new MosamViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MosamViewHolder holder, int position) {
        /*    holder.temp.setText(Forecast.sTemprature[position]);
            holder.time.setText(Forecast.time[position]);
            holder.cloud.setImageResource(R.drawable.cloud);*/
         String temp = aTemp[position];
        String cloud = aCloud[position];
        String time = aTime[position];
        holder.temp.setText(temp+Forecast.par);
        holder.time.setText(time);

        switch (cloud){
            case "broken clouds":
                holder.cloud.setImageResource(R.drawable.broken_clouds);
                break;

            case "clear sky":
                holder.cloud.setImageResource(R.drawable.clear_sky);
                break;

            case "few clouds":
                holder.cloud.setImageResource(R.drawable.few_clouds);
                break;
            case "light rain":
                holder.cloud.setImageResource(R.drawable.light_rain);
                break;
            case "moderate rain":
                holder.cloud.setImageResource(R.drawable.moderate_rain);
                break;
            case "overcast clouds":
                holder.cloud.setImageResource(R.drawable.overcast_clouds);
                break;
          }

       // Log.i("", "onBindViewHolder:"+cloud);

    }

    @Override
    public int getItemCount() {
        return aTemp.length;
    }

    public class MosamViewHolder extends RecyclerView.ViewHolder{
       public ImageView cloud;
       public TextView time,temp;

        public MosamViewHolder(@NonNull View itemView) {
            super(itemView);
            cloud =itemView.findViewById(R.id.cloud);
            temp = itemView.findViewById(R.id.temprature);
            time = itemView.findViewById(R.id.time);
        }
    }
}
