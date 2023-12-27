package com.example.pruebanegocio;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestBindingProductActivity extends AppCompatActivity {




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_select_binding_product);



        String request = getIntent().getExtras().getString("request");

        System.out.println("Salio de request: "+ request);
        String provider = getIntent().getExtras().getString("provider");
        String[] productsNames = this.mysqlThings(request, provider);
        ListView possibleProducts = findViewById(R.id.productsToChoiceRequest);
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productsNames);
        possibleProducts.setAdapter(adapter);

        possibleProducts.setOnItemClickListener((adapterView, view, i, l) -> {
            String productName = adapterView.getAdapter().getItem(i).toString();
            MySqlConnection mySqlConnectionSelect = new MySqlConnection();
            ArrayList<HashMap<String, String>> productsPrice = mySqlConnectionSelect.mysqlQueryToArrayListOfObjects("SELECT precio FROM productos WHERE nombre LIKE '" + productName + "'");
            MySqlConnection mySqlConnectionInsert = new MySqlConnection();
            mySqlConnectionInsert.mysqlQueryWithoutResponse("INSERT INTO pedidos (pedido, proveedor, precio) VALUES ('" + request + "','" + provider + "'," + productsPrice.get(0).get("precio") + ")");
            finish();
            //mySqlConnection.mysqlQueryWithoutResponse("INSERT INTO pedidos (pedido, proveedor, precio) VALUES ('" + request + "','" + provider + "'," + productsPrice.get(0).get("precio") + ")");
        });

    }

    public String[] mysqlThings(String listenedWords, String provider) {
        ArrayList<String> filteredWords = this.filterWords(listenedWords);
        ArrayList<HashMap<String, String>> relatedProducts = this.getRelatedProducts(filteredWords);
        String agroupedProductsNames = "";
        if (relatedProducts.size() == 0) {
            MySqlConnection mySqlConnectionInsert = new MySqlConnection();
            mySqlConnectionInsert.mysqlQueryWithoutResponse("INSERT INTO pedidos (pedido, proveedor) VALUES ('" + listenedWords + "','" + provider + "')");
            finish();
        } else {
            ArrayList<String> relatedProductsNames = this.getProductsNames(relatedProducts);
            agroupedProductsNames = this.agroupProductNames(relatedProductsNames);
        }
        return agroupedProductsNames.split("#");
    }

    private ArrayList<String> getProductsNames(ArrayList<HashMap<String, String>> relatedProducts) {
        ArrayList<String> productsNames = new ArrayList<>();
        for( HashMap<String,String> relatedProduct:  relatedProducts){
            productsNames.add(relatedProduct.get("nombre"));
        }
        return productsNames;
    }

    private String agroupProductNames( ArrayList<String> products ){
        String agrouped = "";
        for (String productName : products){
            agrouped += productName+"#";
        }
        return agrouped.substring(0,agrouped.length()-1);
    }

    private ArrayList<HashMap<String,String>> getRelatedProducts(ArrayList<String> filteredWords){
        MySqlConnection mySqlConnectionSelect = new MySqlConnection();
        ArrayList<HashMap<String,String>> obtainedProducts = mySqlConnectionSelect.mysqlQueryToArrayListOfObjects("SELECT nombre FROM productos WHERE " + this.getWhere(filteredWords));

        //System.out.println("LA consulta iterada es:SELECT nombre FROM productos WHERE " + this.getWhere(filteredWords));
        for (int i =0; i<2 && obtainedProducts.size()==0; i++){
            filteredWords.remove(0);
            MySqlConnection mySqlConnectionInsert = new MySqlConnection();
            //System.out.println("LA consulta iterada es:SELECT nombre FROM productos WHERE " + this.getWhere(filteredWords));
            obtainedProducts = mySqlConnectionInsert.mysqlQueryToArrayListOfObjects("SELECT nombre FROM productos WHERE " + this.getWhere(filteredWords));
        }
        return obtainedProducts;
    }

    private String getWhere(ArrayList<String> filteredWords){
        String where = "";
        for (String keyWord: filteredWords){
            where += "(descripcion LIKE '%_"+keyWord+"_%' OR descripcion LIKE '_"+keyWord+"_%' OR descripcion LIKE '%_"+keyWord+"_') AND ";
        }
        return where.substring(0, where.length()-4);
    }

    private ArrayList<String> filterWords(String words){
        ArrayList<String> innecesaryWordsForProductDescription = new ArrayList<>();
        innecesaryWordsForProductDescription.add("en");
        innecesaryWordsForProductDescription.add("el");
        innecesaryWordsForProductDescription.add("la");
        innecesaryWordsForProductDescription.add("a");
        innecesaryWordsForProductDescription.add("un");
        innecesaryWordsForProductDescription.add("una");
        innecesaryWordsForProductDescription.add("lo");
        innecesaryWordsForProductDescription.add("de");
        innecesaryWordsForProductDescription.add("por");
        innecesaryWordsForProductDescription.add("x");
        innecesaryWordsForProductDescription.add("*");

        ArrayList<String> separatedListenedWords = new ArrayList<>(Arrays.asList(words.split(" ")));
        separatedListenedWords.removeAll(innecesaryWordsForProductDescription);

        return  separatedListenedWords;
    }

    private String filterWordsToString(String words){
        ArrayList<String> filteredWordsArray = this.filterWords(words);
        String filteredWords = "";
        for (String word: filteredWordsArray){
            filteredWords+= word + " ";
        }
        return filteredWords.substring(0, filteredWords.length()-1);
    }
}

