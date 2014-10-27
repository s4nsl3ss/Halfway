package com.sans.halfway;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;


	public class WebWayView extends Activity {

        private WebView webView;
        static public GeoPoint gp= new GeoPoint(0,0);

            @SuppressLint("SetJavaScriptEnabled")
            public void onCreate (Bundle savedInstanceState){
                super.onCreate(savedInstanceState);

                setContentView(R.layout.webview_main);

                webView = (WebView) findViewById(R.id.webview1);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl("http://maps.google.com/?q="+gp.toString());

            }


        public boolean onKeyDown(int keyCode, KeyEvent event)
        {
            //replaces the default 'Back' button action
            if(keyCode==KeyEvent.KEYCODE_BACK)
            {
                finish();
                setContentView(R.layout.activity_main);
            }
            return true;
        }

        public void setLoc(GeoPoint gep){
            gp= gep;
        }

    }
