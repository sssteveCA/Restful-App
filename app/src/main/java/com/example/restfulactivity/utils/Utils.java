package com.example.restfulactivity.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import retrofit2.Response;

public class Utils {
    //public static final String ipLocale = "http://192.168.0.20:8000"; //Indirizzo IP locale
    //Verifica che il telefono sia connesso ad Internet
    public static boolean connesso(Context ctx){
        boolean connesso = false;
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())){
            connesso = true;
        }
        Log.d("Utils","connesso = "+connesso);
        return connesso;
    }
    //controlla che la stringa passata sia un numero
    public static boolean numero(String s){
        boolean numero = true;
        if(s != null && !s.equals("")){
            String regex = "[1-9][0-9]*";
            numero = Pattern.matches(regex,s);
        }//if(s != null && !s.equals("")){
        else{
            numero = false;
        }
        return numero;
    }

}
