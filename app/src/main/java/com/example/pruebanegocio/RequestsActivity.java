package com.example.pruebanegocio;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requests);
        AutoCompleteTextView placesForBuy = findViewById(R.id.buyPlaces);
        AutoCompleteTextView placeForBuy = findViewById(R.id.column1BuyPlace);
        MySqlConnection mySqlConnection = new MySqlConnection();
        ArrayList<HashMap<String,String>> products = mySqlConnection.mysqlQueryToArrayListOfObjects("SELECT * FROM pedidos");
        loadProductsTable("");

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
            loadProductsTable("");
        }));


    }

    private void loadProductsTable( String whereProductLike ) {
        MySqlConnection mySqlConnectionReload = new MySqlConnection();
        String query = "SELECT * FROM pedidos" + whereProductLike;
        ArrayList<HashMap<String,String>> products = mySqlConnectionReload.mysqlQueryToArrayListOfObjects(query);
        System.out.println("Array de productos:" + products);
        ArrayList<String> colors = new ArrayList<>();
        colors.add("#0E0C0C");
        colors.add("#CC100F0F");
        int colorIndex = 0;
        TableLayout productsTable = findViewById(R.id.requestTable);
        productsTable.removeAllViews();
        for (HashMap<String, String> product: products){
            this.addNewRow(productsTable, product, colors.get(colorIndex%2));
            colorIndex++;
        }
    }

    private void addNewRow(TableLayout productsTable, HashMap<String, String> product, String color) {
        TableRow newRow = new TableRow(this);
        newRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        newRow.setBackgroundColor(Color.parseColor(color));
        ArrayList<String> keys = new ArrayList<>();
        keys.add("pedido");
        keys.add("proveedor");
        keys.add("precio");
        HashMap<String,Float> weights = new HashMap<>();
        weights.put("pedido", 3.0f);
        weights.put("proveedor", 2.0f);
        weights.put("precio", 1.0f);
        for (String key: keys){
            TextView productAttribute = new TextView(this);
            productAttribute.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, weights.get(key)));
            productAttribute.setGravity(Gravity.CENTER_VERTICAL);
            productAttribute.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            productAttribute.setText(product.get(key));
            productAttribute.setTextColor(Color.parseColor("#ffffff"));
            newRow.addView(productAttribute);
        }

        productsTable.addView(newRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }


}
