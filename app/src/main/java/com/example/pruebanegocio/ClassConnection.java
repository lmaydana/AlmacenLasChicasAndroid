package com.example.pruebanegocio;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ClassConnection extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... strings) {
        try {
            return this.post(strings[0], strings[1]);
        } catch (IOException e) {

            throw new RuntimeException(e);
        }

    }

    OkHttpClient client = new OkHttpClient();

    private String post(String url, String json) throws IOException {
        ArrayList<String> names = new ArrayList<>();
        names.add("query");
        ArrayList<String> values = new ArrayList<>();
        values.add(json);
        RequestBody body = new FormBody(names,values);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
