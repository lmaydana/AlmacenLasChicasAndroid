package com.example.pruebanegocio;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class MySqlConnection extends Activity {

    private ClassConnection conn;

    private final String URL = "http://laschicas.sytes.net:8888/almacen/json_de_productos.php";


    public MySqlConnection(){

        conn = new ClassConnection();
    }

    public ArrayList<HashMap<String, String>> mysqlQueryToArrayListOfObjects(String query){
        ArrayList<HashMap<String,String>> objects = new ArrayList<>();
        String response = getResponse(query);

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
        System.out.println("La constulta es:"+query);
        String response = "";
        try {
            response = conn.execute(URL, query).get();
            System.out.println("Respuesta de la pagina: "+ response);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    public void mysqlQueryWithoutResponse(String query){
        System.out.println("La constulta es:"+query);
        this.getResponse(query);
    }

}