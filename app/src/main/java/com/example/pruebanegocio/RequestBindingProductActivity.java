package com.example.pruebanegocio;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class RequestBindingProductActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_select_binding_product);

        ListView possibleProductsList = findViewById(R.id.productsToChoiceRequest);

        String[] relatedWords = getIntent().getExtras().getStringArray("relatedWords");
        String requestNew = getIntent().getExtras().getString("request");
        String providerNew = getIntent().getExtras().getString("provider");

        ProductsTable productsTable = new ProductsTable();
        productsTable.doWithRelatedProductsTo(new ArrayList<>(Arrays.asList(relatedWords)), relatedProducts->{

            ProductsListLoader productsListLoader = new ProductsListLoader(this);
            productsListLoader.loadListOfProducts(possibleProductsList, relatedProducts);

            possibleProductsList.setOnItemClickListener((adapterView, view, i, l) -> {
                HashMap<String,String> product = relatedProducts.get(i);
                String productName = product.get("nombre");
                RequestsTable requestsTable = new RequestsTable();
                requestsTable.add(requestNew, productName, providerNew, product.get("precio"));
                finish();
            });
        });



        //-----------------------------ABAJO LO VIEJO-----------------------------------------------------------------------------------------------------------------------------------

        String request = getIntent().getExtras().getString("request");

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
            mySqlConnectionInsert.mysqlQueryWithoutResponse("INSERT INTO pedidos (pedido, producto_asociado, proveedor, precio) VALUES ('" + request + "','"+productName+"','" + provider + "'," + productsPrice.get(0).get("precio") + ")");
            finish();
        });

        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

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
        System.out.println("El array formado nombres: "+agroupedProductsNames.split("#"));
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
        filteredWords.remove(0);
        for (int i =0; i<2 && obtainedProducts.size()==0 && filteredWords.size()>0; i++){
            MySqlConnection mySqlConnectionInsert = new MySqlConnection();
            obtainedProducts = mySqlConnectionInsert.mysqlQueryToArrayListOfObjects("SELECT nombre FROM productos WHERE " + this.getWhere(filteredWords));
            filteredWords.remove(0);
        }
        return obtainedProducts;
    }

    private String getWhere(ArrayList<String> filteredWords){
        String where = "";
        for (String keyWord: filteredWords){
            where += "(descripcion LIKE '%_"+keyWord+"_%' OR descripcion LIKE '_"+keyWord+"_%' OR descripcion LIKE '%_"+keyWord+"_') AND ";
        }
        System.out.println("El where:"+where);
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
        innecesaryWordsForProductDescription.add("g");
        innecesaryWordsForProductDescription.add("gramos");
        innecesaryWordsForProductDescription.add("mililitros");
        innecesaryWordsForProductDescription.add("litro");
        innecesaryWordsForProductDescription.add("litros");
        innecesaryWordsForProductDescription.add("L");
        innecesaryWordsForProductDescription.add("ML");
        innecesaryWordsForProductDescription.add("kilogramos");
        innecesaryWordsForProductDescription.add("KG");

        ArrayList<String> separatedListenedWords = new ArrayList<>(Arrays.asList(words.split(" ")));
        separatedListenedWords.removeAll(innecesaryWordsForProductDescription);

        return  separatedListenedWords;
    }

}

