package com.example.pruebanegocio;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestsActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        this.loadRequestsTable("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requests);
        AutoCompleteTextView placesForBuy = findViewById(R.id.buyPlaces);
        loadRequestsTable("");

        //ArrayList<String> places = new ArrayList<>();<----------Usar esto para ir guardando los proovedores desde mysql (tendran una direccion para googlemaps)
        this.fillListWithQuery(placesForBuy, "SELECT nombre_proveedor FROM proveedores", "nombre_proveedor");
        placesForBuy.setOnItemClickListener(new MicroEvent(this, result -> {
            Intent requestProductBind = new Intent(this, RequestBindingProductActivity.class);
            requestProductBind.putExtra("request", result.getData() != null ? result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0) : "");
            requestProductBind.putExtra("provider", placesForBuy.getText().toString());
            startActivity(requestProductBind);
        }));



        SearchView searchView = findViewById(R.id.providorSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                loadRequestsTable( "WHERE proveedor LIKE '"+s+"%'" );
                System.out.println("La S:"+s);
                return false;
            }
        });

        Button deleteButton = findViewById(R.id.deleteStrikeThrough);
        deleteButton.setOnClickListener(view -> {
            MySqlConnection mySqlConnection = new MySqlConnection();
            mySqlConnection.mysqlQueryWithoutResponse("DELETE FROM pedidos WHERE tachado = 1");
            loadRequestsTable("");
        });

    }

    private void loadRequestsTable(String whereProductLike ) {
        MySqlConnection mySqlConnectionReload = new MySqlConnection();
        String query = "SELECT * FROM pedidos " + whereProductLike+" ORDER BY id DESC";
        ArrayList<HashMap<String,String>> requests = mySqlConnectionReload.mysqlQueryToArrayListOfObjects(query);
        System.out.println("Array de productos:" + requests);
        ArrayList<String> colors = new ArrayList<>();
        colors.add("#0E0C0C");
        colors.add("#CC100F0F");
        int colorIndex = 0;
        TableLayout requestsTable = findViewById(R.id.requestTable);
        requestsTable.removeAllViews();
        for (HashMap<String, String> product: requests){
            this.addNewRow(requestsTable, product, colors.get(colorIndex%2));
            colorIndex++;
        }
    }

    private void addNewRow( TableLayout productsTable, HashMap<String, String> requests, String color ) {
        TableRow newRow = new TableRow(this);
        newRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        newRow.setBackgroundColor(Color.parseColor(color));
        ArrayList<String> keys = new ArrayList<>();
        keys.add("pedido");
        keys.add("producto_asociado");
        keys.add("proveedor");
        keys.add("precio");
        HashMap<String,Float> weights = new HashMap<>();
        weights.put("pedido", 3.0f);
        weights.put("producto_asociado", 3.0f);
        weights.put("proveedor", 3.0f);
        weights.put("precio", 1.0f);
        HashMap<String, TextView> rowView = new HashMap<>();
        AutoCompleteTextView providers = this.getProvidersView(requests);
        rowView.put("pedido", new TextView(this));
        rowView.put("producto_asociado", new TextView(this));
        rowView.put("proveedor", providers);
        rowView.put("precio", new TextView(this));

        for (String key: keys){
            TextView productAttribute = rowView.get(key);
            productAttribute.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, weights.get(key)));
            productAttribute.setGravity(Gravity.CENTER_VERTICAL);
            productAttribute.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            productAttribute.setText(requests.get(key));
            productAttribute.setTextColor(Color.parseColor("#ffffff"));
            newRow.addView(productAttribute);
        }

        this.setStrikethrough(rowView.get("producto_asociado"), requests.get("tachado"));
        productsTable.addView(newRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }

    private void setStrikethrough(TextView associatedProduct, String strikethrough) {
        if( Boolean.parseBoolean(strikethrough) ){
            associatedProduct.setPaintFlags(associatedProduct.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        associatedProduct.setOnClickListener(view -> {
            MySqlConnection mySqlConnection = new MySqlConnection();
            if( (associatedProduct.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0) {
                associatedProduct.setPaintFlags( associatedProduct.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                mySqlConnection.mysqlQueryWithoutResponse("UPDATE pedidos SET tachado = 0 WHERE producto_asociado = '"+associatedProduct.getText().toString()+"'");
            }else {
                associatedProduct.setPaintFlags(associatedProduct.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                mySqlConnection.mysqlQueryWithoutResponse("UPDATE pedidos SET tachado = 1 WHERE producto_asociado = '"+associatedProduct.getText().toString()+"'");
            }
        });
    }


    private AutoCompleteTextView getProvidersView(HashMap<String, String> product){
        AutoCompleteTextView providers = new AutoCompleteTextView(this);
        this.fillListWithQuery(providers, "SELECT nombre_proveedor FROM proveedores", "nombre_proveedor");
        providers.setOnItemClickListener((adapterView, view, i, l) -> {
            MySqlConnection mySqlConnection = new MySqlConnection();
            mySqlConnection.mysqlQueryWithoutResponse("UPDATE pedidos SET proveedor = '"+adapterView.getAdapter().getItem(i)+"' WHERE pedido ='"+product.get("pedido")+"'");//Seguir con el where
        });
        return providers;
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
