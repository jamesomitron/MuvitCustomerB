package com.muvitcustomer.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AnimationUtils;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.muvitcustomer.R;
import com.muvitcustomer.tools.webInterface;

public class MainActivity extends AppCompatActivity {

    String Url = "https://swiftledger.com/muvit";
    WebView webView;
    ImageView img_refresh,progress_indicator_one,progress_indicator_two,progress_indicator_three;
    int img_refresh_width,img_refresh_height,error_layout_height,error_layout_width;
    float img_refresh_radius,error_layout_radius;
    RelativeLayout viewGroup;
    LinearLayout indicator_layout,web_layout,error_layout;
    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        indicator_layout = (LinearLayout)findViewById(R.id.indicator_layout);
        error_layout = (LinearLayout)findViewById(R.id.error_layout);
        web_layout = (LinearLayout)findViewById(R.id.web_layout);
        img_refresh = (ImageView)findViewById(R.id.img_refresh);
        img_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl(Url);
                Animator hide_img_refresh = ViewAnimationUtils.createCircularReveal(img_refresh,img_refresh_width,img_refresh_height,img_refresh_radius,0);

                hide_img_refresh.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        img_refresh.setVisibility(View.GONE);
                    }
                });
                hide_img_refresh.start();

                Animator hide_error_layout = ViewAnimationUtils.createCircularReveal(error_layout,error_layout_width,error_layout_height,error_layout_radius,0);

                hide_error_layout.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        img_refresh.setVisibility(View.GONE);
                    }
                });
                hide_error_layout.setStartDelay(600);
                hide_error_layout.start();

                indicator_layout.setVisibility(View.VISIBLE);
            }
        });

        //Progress indicator
        progress_indicator_one = (ImageView)findViewById(R.id.progress_indicator_one);
        progress_indicator_two = (ImageView)findViewById(R.id.progress_indicator_two);
        progress_indicator_three = (ImageView)findViewById(R.id.progress_indicator_three);
        progress_indicator_one.startAnimation(AnimationUtils.loadAnimation(this,R.anim.rotate_animation));
        progress_indicator_two.startAnimation(AnimationUtils.loadAnimation(this,R.anim.rotate_animation));
        progress_indicator_three.startAnimation(AnimationUtils.loadAnimation(this,R.anim.rotate_animation));
        //make the progress bar visible
        //pgBar.setVisibility(View.VISIBLE);
        //pgBar.setIndeterminate(true);




        //setup WebView rendering engine
        webView = (WebView)findViewById(R.id.web);
        webView.setVisibility(View.INVISIBLE);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setAppCacheMaxSize( 8 * 1024 * 1024 ); // 8MB
        webView.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setCacheMode( WebSettings.LOAD_DEFAULT );

        WebSettings settings = webView.getSettings();
        settings.setTextZoom(100);

        if (!isNetworkAvailable()) {
            // loading offline
            webView.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        webView.setWebViewClient(new MyBrowser());
        //webView.setWebChromeClient(new myChromeClient());
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.addJavascriptInterface(new webInterface(MainActivity.this),"SWIFTLEDGER");
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Locations");
                builder.setMessage("Share current location?")
                        .setCancelable(true)
                        .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // origin, allow, remember
                                callback.invoke(origin, true, false);
                            }
                        }).setNegativeButton("Don't Allow", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // origin, allow, remember
                        callback.invoke(origin, false, false);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
            @Override
            public void onPermissionRequest(final PermissionRequest request){
                request.grant(request.getResources());
            }
        });
        webView.loadUrl(Url);
    }

    @Override
    public void onBackPressed(){
        if(webView.canGoBack()){
            webView.goBack();
            return;
        }else{

        }
        super.onBackPressed();
    }


    /**
     * User-Defined Methods
     * **/
    private boolean isNetworkAvailable() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if(activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            Log.e("NetworkE",String.valueOf(e));
            return false;
        }
    }

    /**
     * Private classes
     * **/

    //Extends WebViewClient
    private class MyBrowser extends WebViewClient {
        @Override
        @TargetApi(Build.VERSION_CODES.M)
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest resource) {
            String current_url = resource.getUrl().toString();
            if (current_url.contains("/survey")) {
                //fab.setVisibility(View.VISIBLE);
            }
            //view.loadUrl(url);
            return false;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            webView.setVisibility(View.GONE);
            web_layout.setVisibility(View.VISIBLE);
            img_refresh_width = img_refresh.getWidth() / 2;
            img_refresh_height = img_refresh.getHeight() / 2;

            //get the radius of the view...
            img_refresh_radius = (float) Math.hypot(img_refresh_width, img_refresh_height);

            //get Instance of an Animator class
            Animator reveal_img_refresh = ViewAnimationUtils.createCircularReveal(img_refresh, img_refresh_width, img_refresh_height, 0, img_refresh_radius);

            //Start Animations
            img_refresh.setVisibility(View.VISIBLE);
            reveal_img_refresh.setStartDelay(600);
            reveal_img_refresh.start();

            error_layout_width = error_layout.getWidth() / 2;
            error_layout_height = error_layout.getHeight() / 2;

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    error_layout_width = error_layout.getWidth() / 2;
                    error_layout_height = error_layout.getHeight() / 2;

                    //get the radius of the view...
                    error_layout_radius = (float) Math.hypot(error_layout_width, error_layout_height);

                    //get Instance of an Animator class
                    Animator reveal_error_layout = ViewAnimationUtils.createCircularReveal(error_layout, error_layout_width, error_layout_height, 0, error_layout_radius);

                    //Start Animations
                    error_layout.setVisibility(View.VISIBLE);
                    reveal_error_layout.start();
                }
            },1000);
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public void onPageFinished(WebView webView, String url) {
            web_layout.setVisibility(View.VISIBLE);
            int page_width = webView.getWidth() / 2;
            int page_height = webView.getHeight() / 2;
            float page_radius = (float) Math.hypot(page_width, page_height);
            //Animator page_animation = ViewAnimationUtils.createCircularReveal(webView, page_width, page_height, 0, page_radius);
            //page_animation.start();
            webView.setVisibility(View.VISIBLE);
            indicator_layout.setVisibility(View.GONE);
        }
    }
}
