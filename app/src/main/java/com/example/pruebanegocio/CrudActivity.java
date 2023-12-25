package com.example.pruebanegocio;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class CrudActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_product);
        this.setInitialThings();
    }

    private void setInitialThings(){

        this.loadProductsTable();

        Button addProductButton = findViewById(R.id.addProduct);
        addProductButton.setOnClickListener(new BarCodeEvent(this, result->{
            String barcode = result.getContents();
            Intent addProductActivity = new Intent(this, AddProductActivity.class);
            addProductActivity.putExtra("barcode", barcode);
            startActivity(addProductActivity);
        }));

        Button microButton = findViewById(R.id.microButton);
        microButton.setOnClickListener(new MicroEvent(this, result -> {
            ArrayList<String> listenedWordsArray = result.getData() != null ? result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) : new ArrayList<>();
            String listenedWords = listenedWordsArray.get(0);
            System.out.println("Palabras escuchadas:" + listenedWords);
            lauchProductSelectIntent(listenedWords);
        }));
    }

    private void loadProductsTable() {
        MySqlConnection mySqlConnection = new MySqlConnection();
        ArrayList<HashMap<String,String>> products = mySqlConnection.mysqlQueryToArrayListOfObjects("SELECT * FROM productos");
        ArrayList<String> colors = new ArrayList<>();
        colors.add("#0E0C0C");
        colors.add("#CC100F0F");
        int colorIndex = 0;
        for (HashMap<String, String> product: products){
            this.addNewRow(product, colors.get(colorIndex%2));
            colorIndex++;
        }
    }

    private void addNewRow(HashMap<String, String> product, String color) {
        TableLayout productsTable = findViewById(R.id.productsTable);
        TableRow newRow = new TableRow(this);
        newRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        newRow.setBackgroundColor(Color.parseColor(color));
        ArrayList<String> keys = new ArrayList<>();
        keys.add("codigo");
        keys.add("nombre");
        keys.add("precio");
        keys.add("porcentaje");
        for (String key: keys){
            TextView productAttribute = new TextView(this);
            productAttribute.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
            productAttribute.setGravity(Gravity.CENTER_VERTICAL);
            productAttribute.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            productAttribute.setText(product.get(key));
            productAttribute.setTextColor(Color.parseColor("#ffffff"));
            newRow.addView(productAttribute);
        }

        productsTable.addView(newRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }

    private void lauchProductSelectIntent(String listenedWords) {
        Intent changeProductIntent = new Intent(this, ChangeSelectedActivity.class);
        changeProductIntent.putExtra("listenedWords", listenedWords);
        startActivity(changeProductIntent);
    }





}
