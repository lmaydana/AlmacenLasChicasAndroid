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


    MySqlConnection mySqlConnection;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_select_binding_product);

        this.mySqlConnection = new MySqlConnection();

        String request = getIntent().getExtras().getString("request");

        System.out.println("Salio de request: "+ request);
        String provider = getIntent().getExtras().getString("provider");
        String[] productsNames = this.mysqlThings(request, provider);
        ListView possibleProducts = findViewById(R.id.productsToChoiceRequest);
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productsNames);
        possibleProducts.setAdapter(adapter);

        possibleProducts.setOnItemClickListener((adapterView, view, i, l) -> {
            String productName = adapterView.getAdapter().getItem(i).toString();
            ArrayList<HashMap<String, String>> productsPrice = mySqlConnection.mysqlQueryToArrayListOfObjects("SELECT precio FROM productos WHERE nombre LIKE '%" + productName + "%'");
            this.doQuery("INSERT INTO pedidos (pedido, proveedor, precio) VALUES ('" + request + "','" + provider + "'," + productsPrice.get(0).get("precio") + ")");
            //mySqlConnection.mysqlQueryWithoutResponse("INSERT INTO pedidos (pedido, proveedor, precio) VALUES ('" + request + "','" + provider + "'," + productsPrice.get(0).get("precio") + ")");
        });

    }

    private void doQuery(String query){
        OkHttpClient client = new OkHttpClient();
        ArrayList<String> names = new ArrayList<>();
        names.add("query");
        ArrayList<String> values = new ArrayList<>();
        values.add(query);
        RequestBody body = new FormBody(names,values);
        Request request = new Request.Builder()
                .url("http://186.123.109.86:8888/almacen/json_de_productos.php")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
    }

    public String[] mysqlThings(String listenedWords, String provider) {
        ArrayList<String> filteredWords = this.filterWords(listenedWords);
        ArrayList<HashMap<String, String>> relatedProducts = this.getRelatedProducts(filteredWords);
        String agroupedProductsNames = "";
        if (relatedProducts.size() == 0) {
            mySqlConnection.mysqlQueryWithoutResponse("INSERT INTO pedidos (pedido, proveedor) VALUES ('" + listenedWords + "','" + provider + "')");
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
        ArrayList<HashMap<String,String>> obtainedProducts = mySqlConnection.mysqlQueryToArrayListOfObjects("SELECT nombre FROM productos WHERE " + this.getWhere(filteredWords));

        for (int i =0; i<3 && obtainedProducts.size()==0; i++){
            filteredWords.remove(0);
            obtainedProducts = mySqlConnection.mysqlQueryToArrayListOfObjects("SELECT nombre FROM productos WHERE " + this.getWhere(filteredWords));
        }
        return obtainedProducts;
    }

    private String getWhere(ArrayList<String> filteredWords){
        String where = "";
        for (String keyWord: filteredWords){
            where += "descripcion LIKE '%"+keyWord+"%' AND ";
        }
        return where.substring(0, where.length()-4);
    }

    private ArrayList<String> filterWords(String listenedWords){
        ArrayList<String> innecesaryWordsForProductDescription = new ArrayList<>();
        innecesaryWordsForProductDescription.add("en");
        innecesaryWordsForProductDescription.add("el");
        innecesaryWordsForProductDescription.add("la");
        innecesaryWordsForProductDescription.add("a");
        innecesaryWordsForProductDescription.add("un");
        innecesaryWordsForProductDescription.add("una");
        innecesaryWordsForProductDescription.add("lo");
        innecesaryWordsForProductDescription.add("de");

        ArrayList<String> separatedListenedWords = new ArrayList<>(Arrays.asList(listenedWords.split(" ")));
        separatedListenedWords.removeAll(innecesaryWordsForProductDescription);

        return  separatedListenedWords;
    }
}

