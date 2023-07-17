package com.example.kongbaekdog;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//산책화면 프래그먼트
public class PositionMap extends Fragment {

    WebView webView;
    WebSettings webSettings;
    Button PDHos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.fragment_position_map, container, false);
        PDHos=rootview.findViewById(R.id.button16);
        webView=rootview.findViewById(R.id.webview2);
        webSettings = webView.getSettings();
        GPSSettiong();
        permissionCheck();

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClientClass());
        webView.getSettings().setBuiltInZoomControls(true);

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
                callback.invoke(origin, true, false);
            }
        });

        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("https://www.google.co.kr/maps");
        PDHos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("https://www.google.co.kr/maps/search/%EB%8F%99%EB%AC%BC%EB%B3%91%EC%9B%90");
            }
        });
        return rootview;
    }

    private void GPSSettiong(){
        ContentResolver res= getContext().getContentResolver();

        boolean gpsEnabled= Settings.Secure.isLocationProviderEnabled(res, LocationManager.GPS_PROVIDER);
        if(!gpsEnabled){
            new AlertDialog.Builder(getContext()).setTitle("GPS설정").setMessage("GPS를 사용하시겠습니까?").setPositiveButton("사용", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            }).setNegativeButton("거절", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getContext(),"GPS설정을 거절하였습니다.",Toast.LENGTH_SHORT).show();
                }
            }).show();
        }
    }

    private void permissionCheck(){
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){}
        else{
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},384);
        }
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if(url.startsWith("intent:")){
                try{
                    Intent intent=Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    Intent existPackage= getContext().getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                    if(existPackage!=null){
                        startActivity(intent);
                    }else{
                        Intent marketIntent=new Intent(Intent.ACTION_VIEW);
                        marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()));
                        startActivity(marketIntent);
                    }
                    return true;
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else{
                view.loadUrl(url);
            }
            return true;
        }
    }
}
