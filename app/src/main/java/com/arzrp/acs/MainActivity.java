package com.arzrp.acs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private WebView webview1;

    public static boolean isActiveAdBlocker(Activity activity, Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Network activeNetwork = cm.getActiveNetwork();
            if (activeNetwork != null) {
                LinkProperties linkProperties = cm.getLinkProperties(activeNetwork);
                if (linkProperties != null) {
                    String privateDnsHost = linkProperties.getPrivateDnsServerName();
                    if (privateDnsHost != null) {
                        String dns = privateDnsHost.toLowerCase();
                        String[] adBlockers = new String[]{"adguard", "nextdns", "controld", "libredns", "blokada", "quad9", "adblock", "rethinkdns", "cleanbrowsing"};
                        for (String blocker : adBlockers) {
                            if (dns.contains(blocker)) {
                                Log.w("MtgTools", "Detected AD blocker: " + privateDnsHost);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility"})
    private void setupWebView(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setLoadsImagesAutomatically(true);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        webView.requestFocus(View.FOCUS_DOWN);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
                if (!v.hasFocus()) v.requestFocus();
            }
            return false;
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        webview1 = (WebView) findViewById(R.id.webview1);
        setupWebView(webview1);

        webview1.loadUrl("https://pla1keo.github.io/mobile/");

        new Ads(MainActivity.this);

    }


}