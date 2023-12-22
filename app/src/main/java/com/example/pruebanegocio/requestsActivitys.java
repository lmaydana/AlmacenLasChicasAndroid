package com.example.pruebanegocio;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class requestsActivitys extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requests);
        AutoCompleteTextView placesForBuy = findViewById(R.id.buyPlaces);
        //ArrayList<String> places = new ArrayList<>();<----------Usar esto para ir guardando los proovedores desde mysql (tendran una direccion para googlemaps)
        String[] places = getResources().getStringArray(R.array.buy_places);
        ArrayAdapter<String> placesAdapter = new ArrayAdapter<>(this,R.layout.buy_places_items,places);
        placesForBuy.setAdapter(placesAdapter);
    }
}
