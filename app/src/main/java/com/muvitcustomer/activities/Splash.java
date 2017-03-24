package com.muvitcustomer.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.muvitcustomer.R;

/**
 ** Created by dolapo on 12/8/2016.
 */
public class Splash extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new SplashTask().execute();
            }
        },5000);
    }

    private class SplashTask extends AsyncTask<Void,Void,String>{
        public void onPreExecute(){}
        public String doInBackground(Void... v){
            return "Result";
        }
        public void onPostExecute(String result){
            Intent intent = new Intent(Splash.this,MainActivity.class);
            startActivity(intent);
        }
    }
}
