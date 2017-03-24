package com.muvitcustomer.tools;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 ** Created by dolapo on 12/8/2016.
 */
public class GeoLocate extends Service {
    final String ADDRESS = "https://192.168.1.3/smallApp/website1";
    public void onCreate(){
        super.onCreate();
        /**TimerTask getLocationInterval = new TimerTask() {
            @Override
            public void run() {
                new getLocationTask().execute();
            }
        };
        Timer setTime = new Timer();
        setTime.schedule(getLocationInterval,600000);**/
    }
    public IBinder onBind(Intent intent){
        return null;
    }

    /**private class getLocationTask extends AsyncTask<Void,Void,HashMap<String,String>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected HashMap<String,String> doInBackground(Void... voids) {
            MainActivity activity = new MainActivity();
            Location location = activity.getLocation();
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();
            String sLatitude = String.valueOf(latitude);
            String sLongitude = String.valueOf(longitude);
            Log.e("COORDINATE: ",latitude+" "+longitude);
            HashMap<String,String> data = new HashMap<>();
            data.put("Latitude",sLatitude);
            data.put("Longitude",sLongitude);
            return data;

        }

        @Override
        protected void onPostExecute(HashMap<String,String> result) {
            new postCoordinateTask().execute(result);
        }
    }

    private class postCoordinateTask extends AsyncTask<HashMap<String,String>,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(HashMap<String,String>... hashMaps) {
            if(isNetworkAvailable()){
                return "No Network";
            }
            else{
                DataTransport transport = new DataTransport(ADDRESS);
                transport.postData("Coordinate",hashMaps[0]);
                return "sent";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }**/
    private boolean isNetworkAvailable() {
        boolean isConnected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }catch (Exception e){
            e.printStackTrace();
        }
        return isConnected;
    }
}
