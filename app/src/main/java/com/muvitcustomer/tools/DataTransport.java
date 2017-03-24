package com.muvitcustomer.tools;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 ** Created by dolapo on 12/9/2016.
 */
public class DataTransport {
    String url;
    public DataTransport(String address){
        url = address;
    }
    public void postData(String Header, HashMap<String,String> dict){
        String data = "",response = "";
        DataOutputStream printout;
        try{
            URL address = new URL(url);
            try{
                data = getPostData(Header,dict);
            }catch (UnsupportedEncodingException ex){
                ex.printStackTrace();
            }
            try{
                HttpURLConnection httpURLConnection = (HttpURLConnection)address.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setFixedLengthStreamingMode(data.getBytes().length);

                //Posting Data
                printout = new DataOutputStream(httpURLConnection.getOutputStream ());
                byte[] dataB = data.getBytes("UTF-8");
                printout.write(dataB);
                printout.flush();
                printout.close();

                //Getting the result/feedback
                int responseCode = httpURLConnection.getResponseCode();
                Log.e("RESCODE",String.valueOf(responseCode));
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    while ((line=br.readLine()) != null) {
                        response+=line;
                    }
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }catch (MalformedURLException ex){
            ex.printStackTrace();
        }
    }
    private String getPostData(String Header,HashMap<String, String> params) throws UnsupportedEncodingException {
        JSONObject jsonBody = new JSONObject();

        for(Map.Entry<String, String> entry : params.entrySet()){
            try{
                jsonBody.put(entry.getKey(), entry.getValue());
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put(Header,jsonBody);
        }catch (JSONException jE){
            jE.printStackTrace();
        }
        return jsonObject.toString();
    }
}
