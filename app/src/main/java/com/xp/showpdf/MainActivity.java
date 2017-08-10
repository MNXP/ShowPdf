package com.xp.showpdf;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button downBtn;
    private Button showBtn;
    private WebView webView;
    private ToolUpdate toolUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downBtn = (Button) findViewById(R.id.down_btn);
        showBtn = (Button) findViewById(R.id.show_btn);
        webView = (WebView) findViewById(R.id.web);
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolUpdate = new ToolUpdate(MainActivity.this, "http://sandbox.junziqian.com/applaySign/downFile?timestamp=1502262315968&applyNo=APL895178544567689216&sign=8b47ecfe15ea1fdedf48862a23eba30e360332db");
                toolUpdate.downloadApk();
            }
        });
        initWebView();
        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = toolUpdate.pdfLj();
                if (!"".equals(url)){
                    File file = new File(url);
                    FileInputStream inputFile = null;
                    try {
                        inputFile = new FileInputStream(file);
                        byte[] buffer = new byte[(int)file.length()];
                        inputFile.read(buffer);
                        inputFile.close();
                        String  base64Token = Base64.encodeToString(buffer, Base64.DEFAULT);
                        webView.loadUrl("javascript:loadMyJS('" + base64Token + "')");
                    }  catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

    }

    private void initWebView() {
        webView.setVerticalScrollBarEnabled(false); //垂直不显示
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new MyWebViewClient());
        webView.addJavascriptInterface(new JavascriptInterface(), "Pdf");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setBlockNetworkImage(false);
        webView.loadUrl("file:///android_asset/index.html");


    }


    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
        }

    }

    private class JavascriptInterface {
        public JavascriptInterface() {
        }

        @android.webkit.JavascriptInterface
        public void getInfo(String title, String content) {

        }

        @android.webkit.JavascriptInterface
        public void runCallPhone(final String str) {

        }
    }
}
