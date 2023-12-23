package com.example.pruebanegocio;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MySqlConnection mySqlConnection = new MySqlConnection();
        ArrayList<HashMap<String,String>> products = mySqlConnection.mysqlQueryToArrayListOfObjects("select * from productos");
        for( HashMap<String, String> product : products ){
            System.out.println("código: "+product.get("codigo")+", nombre: "+product.get("nombre")+", precio: "+ product.get("precio")+", porcentaje: "+product.get("porcentaje"));
        }
    }

    public void startRequestsView(View view){
        Intent requestIntent = new Intent(this, RequestsActivity.class);
        startActivity(requestIntent);
    }

    public void startCrudActivity( View view ){
        Intent CrudIntent = new Intent(this, CrudActivity.class);
        startActivity(CrudIntent);
    }
}