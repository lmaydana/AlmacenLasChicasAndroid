package com.example.pruebanegocio;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class MySqlConnection extends Activity {

    ClassConnection conn;

    private final String URL = "http://186.123.109.86:8888/almacen/json_de_productos.php";


    public MySqlConnection(){

        conn = new ClassConnection();
    }

    public ArrayList<HashMap<String, String>> mysqlQueryToArrayListOfObjects(String query){
        ArrayList<HashMap<String,String>> objects = new ArrayList<>();
        String response = getResponse(query);
        System.out.println("Respuesta de la pagina: "+ response);
        try {

            JSONArray jsonArray= new JSONArray(response);

            for( int index = 0; index < jsonArray.length(); index++) {
                HashMap<String,String> object = new HashMap<>();
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

    private String getResponse(String query){
        String response = "";
        try {
            response = conn.execute(URL, query).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    public void mysqlQueryWithoutResponse(String query){
        this.getResponse(query);
    }

}