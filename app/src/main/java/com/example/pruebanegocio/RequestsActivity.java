package com.example.pruebanegocio;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requests);
        AutoCompleteTextView placesForBuy = findViewById(R.id.buyPlaces);
        loadProductsTable("");

        //ArrayList<String> places = new ArrayList<>();<----------Usar esto para ir guardando los proovedores desde mysql (tendran una direccion para googlemaps)
        this.fillListWithQuery(placesForBuy, "SELECT nombre_proveedor FROM proveedores", "nombre_proveedor");
        placesForBuy.setOnItemClickListener(new MicroEvent(this, result -> {
            Intent requestProductBind = new Intent(this, RequestBindingProductActivity.class);
            requestProductBind.putExtra("request", result.getData() != null ? result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0) : "");
            requestProductBind.putExtra("provider", placesForBuy.getText().toString());
            startActivity(requestProductBind);
            loadProductsTable("");
        }));

        TextInputEditText providorSearcher = findViewById(R.id.providorSearch);
        /*providorSearcher.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                loadProductsTable("WHERE proveedor LIKE '"+providorSearcher.getText().toString()+"'");
                return false;
            }
        });*/


    }

    private void loadProductsTable( String whereProductLike ) {
        MySqlConnection mySqlConnectionReload = new MySqlConnection();
        String query = "SELECT * FROM pedidos " + whereProductLike;
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
        HashMap<String, TextView> rowView = new HashMap<>();
        AutoCompleteTextView providers = new AutoCompleteTextView(this);
        rowView.put("pedido", new TextView(this));
        rowView.put("proveedor", providers);
        rowView.put("precio", new TextView(this));
        providers.setThreshold(1);
        this.fillListWithQuery(providers, "SELECT nombre_proveedor FROM proveedores", "nombre_proveedor");

        for (String key: keys){
            TextView productAttribute = rowView.get(key);
            productAttribute.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, weights.get(key)));
            productAttribute.setGravity(Gravity.CENTER_VERTICAL);
            productAttribute.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            productAttribute.setText(product.get(key));
            productAttribute.setTextColor(Color.parseColor("#ffffff"));
            newRow.addView(productAttribute);
        }

        providers.setOnItemClickListener((adapterView, view, i, l) -> {
            MySqlConnection mySqlConnection = new MySqlConnection();
            mySqlConnection.mysqlQueryWithoutResponse("UPDATE pedidos SET proveedor = '"+adapterView.getAdapter().getItem(i)+"' WHERE pedido ='"+product.get("pedido")+"'");//Seguir con el where
        });
        productsTable.addView(newRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }

    private void fillListWithQuery(AutoCompleteTextView list, String query, String field) {
        MySqlConnection mySqlConnection = new MySqlConnection();
        ArrayList<HashMap<String, String>> entities = mySqlConnection.mysqlQueryToArrayListOfObjects(query);
        ArrayList<String> entitiesField = this.getEntitiesFieldInArrayList(entities, field);
        ArrayAdapter<String> entitiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, entitiesField);
        list.setAdapter(entitiesAdapter);
    }

    private ArrayList<String> getEntitiesFieldInArrayList( ArrayList<HashMap<String, String>> entities, String field ) {
        ArrayList<String> entitiesField = new ArrayList<>();
        for ( HashMap<String, String> entity : entities){
            entitiesField.add(entity.get(field));
        }
        return entitiesField;
    }


}
