package com.example.pruebanegocio;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MySqlQueryPro {

    private OkHttpClient client;

    private Request.Builder requestBuilder;
    public MySqlQueryPro(String url){
        client = new OkHttpClient();
        requestBuilder = new Request.Builder()
                .url(url);
    }

    private ArrayList<HashMap<String,String>> responseToArrayList(String response){
        ArrayList<HashMap<String,String>> objects = new ArrayList<>();
        try {
            JSONArray jsonArray= new JSONArray(response);

            for( int index = 0; index < jsonArray.length(); index++) {
                HashMap<String,String> object = new LinkedHashMap<>();
                JSONObject jsonObject = jsonArray.getJSONObject(index);
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()){
                    String key = keys.next();
                    object.put(key, jsonObject.getString(key));
                }

                objects.add(object);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objects;
    }

    private Request configureQuery(String query){
        ArrayList<String> names = new ArrayList<>();
        names.add("query");
        ArrayList<String> values = new ArrayList<>();
        values.add(query);
        RequestBody body = new FormBody(names,values);
        Request request = requestBuilder.post(body).build();
        return request;
    }

    public void mysqlQuery(String query, Consumer<ArrayList<HashMap<String,String>>> toDoWithResponse){
        Request request = this.configureQuery(query);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ArrayList<HashMap<String,String>> result = responseToArrayList(response.body().string());
                toDoWithResponse.accept(result);
            }
        });
    }

    public void mysqlQueryWithoutResponse(String query) {
        Request request = this.configureQuery(query);
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }



}
