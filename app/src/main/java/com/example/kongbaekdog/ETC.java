package com.example.kongbaekdog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

//기타 화면 프래그먼트
public class ETC extends Fragment {

    Button AnimalPS, WithPet;
    LinearLayout containersite;
    WebView webView;
    static int num=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_e_t_c, container, false);

        AnimalPS=rootView.findViewById(R.id.button11);
        WithPet=rootView.findViewById(R.id.button12);
        containersite=rootView.findViewById(R.id.containersite);

        webView=rootView.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);


        if(num==1){
            webView.loadUrl("https://www.animal.go.kr");
            webView.setWebChromeClient(new WebChromeClient());
            webView.setWebViewClient(new WebViewClientClass());
        }
        else if(num==2){
            webView.loadUrl("https://m.easylaw.go.kr/MOB/CsmInfoRetrieve.laf?csmSeq=1287&ccfNo=2&cciNo=3&cnpClsNo=1");
            webView.setWebChromeClient(new WebChromeClient());
            webView.setWebViewClient(new WebViewClientClass());
        }
        else{
            webView.loadUrl("https://www.animal.go.kr");
            webView.setWebChromeClient(new WebChromeClient());
            webView.setWebViewClient(new WebViewClientClass());
        }

        AnimalPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl("https://www.animal.go.kr");
                webView.setWebChromeClient(new WebChromeClient());
                webView.setWebViewClient(new WebViewClientClass());
                num=1;
            }
        });

        WithPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl("https://m.easylaw.go.kr/MOB/CsmInfoRetrieve.laf?csmSeq=1287&ccfNo=2&cciNo=3&cnpClsNo=1");
                webView.setWebChromeClient(new WebChromeClient());
                webView.setWebViewClient(new WebViewClientClass());
                num=2;
            }
        });

        return  rootView;
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return onKeyDown(keyCode, event);
    }

    private class WebViewClientClass extends WebViewClient {//페이지 이동
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("check URL",url);
            view.loadUrl(url);
            return true;
        }
    }
}