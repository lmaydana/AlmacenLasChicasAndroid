package com.example.pruebanegocio;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class RequestsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requests);
        AutoCompleteTextView placesForBuy = findViewById(R.id.buyPlaces);
        AutoCompleteTextView placeForBuy = findViewById(R.id.column1BuyPlace);

        //ArrayList<String> places = new ArrayList<>();<----------Usar esto para ir guardando los proovedores desde mysql (tendran una direccion para googlemaps)
        String[] places = getResources().getStringArray(R.array.buy_places);
        ArrayAdapter<String> placesAdapter = new ArrayAdapter<>(this,R.layout.buy_places_items,places);
        placesForBuy.setAdapter(placesAdapter);
        placeForBuy.setAdapter(placesAdapter);
    }
}
