package com.arzrp.acs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity {

    private WebView webview1;

    private static boolean isActiveAdBlocker(Context context) {
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
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
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

        // backup link https://mtgmods.github.io/pla1keo.github.io/

        if (isActiveAdBlocker(this)) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("ℹ️ Обнаружен AD Blocker (Private DNS) ℹ️")
                    .setMessage(
                            "Данное приложение распространяется бесплатно, а реклама при запуске помогает поддерживать его 💖\n\n"
                                    + "Вы же используете Private DNS, который блокирует показ рекламы 🥺\n\n"
                                    + "👉 Отключите DNS в настройках сети\n"
                    )
                    .setPositiveButton("Открыть настройки", (dialog, which) -> {
                        try {
                            Intent intent = new Intent("android.settings.PRIVATE_DNS_SETTINGS");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            this.startActivity(intent);
                        } catch (Exception e) {
                            try {
                                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                MainActivity.this.startActivity(intent);
                            } catch (Exception ex) {
                                Toast.makeText(this, "Настройки -> Сеть -> DNS", Toast.LENGTH_LONG).show();
                            }
                        }
                        dialog.dismiss();
                    })
                    .setNegativeButton("Нет", (dialog, which) -> {
                        Toast.makeText(this, "😭😭😭", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    })
                    .setCancelable(true)
                    .show();
        } else {
            new Ads(MainActivity.this);
        }

    }

}