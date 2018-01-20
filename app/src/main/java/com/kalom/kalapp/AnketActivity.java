package com.kalom.kalapp;


import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kalom.kalapp.classes.Config;
import com.kalom.kalapp.classes.SessionManager;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Array;


public class AnketActivity extends AppCompatActivity {


    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anket_layout);

        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

       android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle extras=getIntent().getExtras();
        int anketID = extras.getInt("anket_id");



        WebView webview = findViewById(R.id.anket_webview);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        SessionManager session = new SessionManager(getApplicationContext());

        webview.loadUrl(Config.api_server+"?action=anket&hash="+session.getToken()+"&do=anket_getir&id="+ anketID);



        webview.setWebViewClient(new WebViewClient() {
            final ProgressBar pb=findViewById(R.id.progressBar);

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pb.setVisibility(View.GONE);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplicationContext(), "Bir hata oluştu", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
            }
        });



    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);

        EventBus.getDefault().post("ANKETTEN_GERI_DONULDU");

    }



}
