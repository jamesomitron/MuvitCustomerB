package com.muvitcustomer.tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.JavascriptInterface;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 ** Created by Dolapo on 1/25/2017.
 */

public class webInterface {
        Activity activity;
        String preferenceFile = "myPrefs";
        SharedPreferences sharedPref;
        public webInterface(Activity activity) {
            this.activity = activity;
        }
        @JavascriptInterface
        public void showDialog(String name){
            Log.e("WEBVIEW","YES");
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage(name+" WebView Control")
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //DO SOMETHING..
                        }
                    }).setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            builder.show();
            //Toast.makeText(this.activity.getApplicationContext(), name+" is welcome",Toast.LENGTH_LONG).show();
        }

        @JavascriptInterface
        public String getCameraIntent(){
            //Take picture and save in a file
            File path = null;
            String photoPath = "s";
            try {

                //Taking a picture
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(activity.getPackageManager())!= null){

                    //Creating file to save image
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imagePath = "SWIFTLEDGER_" + timeStamp;
                    File file = new File(this.activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES),"SWIFTLEDGER");
                    if(file != null){
                        if(!file.mkdir()){
                            if(!file.exists()){
                                //do nothing..
                            }
                        }
                    }
                    path = File.createTempFile(imagePath, ".jpg", file);
                    photoPath = "content://" + path.getAbsolutePath();
                    Log.e("Path2",photoPath);
                    sharedPref = activity.getSharedPreferences(preferenceFile, Context.MODE_PRIVATE);
                    sharedPref.edit().putString("ImageFile",photoPath).apply();

                    //Saving image in file
                    File f = new File(path.getAbsoluteFile().toString());
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    activity.startActivityForResult(takePictureIntent,1);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return photoPath;
        }

}
