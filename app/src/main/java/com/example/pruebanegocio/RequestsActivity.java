package com.example.pruebanegocio;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;

public class RequestsActivity extends AppCompatActivity {
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
        placesForBuy.setOnItemClickListener(new MicroEvent(this, result -> {
            Intent requestProductBind = new Intent(this, RequestBindingProductActivity.class);
            requestProductBind.putExtra("request", result.getData() != null ? result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0) : "");
            requestProductBind.putExtra("provider", placesForBuy.getText().toString());
            startActivity(requestProductBind);
        }));
    }


}
