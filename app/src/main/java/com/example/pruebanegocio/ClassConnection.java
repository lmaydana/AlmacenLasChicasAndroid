package com.example.pruebanegocio;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ClassConnection extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection httpURLConnection = null;
        URL url = null;
        StringBuffer buffer = new StringBuffer();
        try {
            url = new URL(strings[0]);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            int code = httpURLConnection.getResponseCode();
            if( code == HttpURLConnection.HTTP_OK ){
                InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line="";
                while( (line = reader.readLine()) != null ){
                    buffer.append(line);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
