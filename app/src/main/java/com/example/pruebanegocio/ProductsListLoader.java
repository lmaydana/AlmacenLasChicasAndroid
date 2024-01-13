package com.example.pruebanegocio;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductsListLoader {

    private Context context;

    public ProductsListLoader(Context context){
        this.context = context;
    }

    private ArrayList<String> getProductsNames(ArrayList<HashMap<String,String>> productsThatWillBeModified){
        ArrayList<String> productsToChoice = new ArrayList<>();
        for (HashMap<String,String> product : productsThatWillBeModified){
            productsToChoice.add(product.get("nombre"));
        }
        return productsToChoice;
    }

    public void loadListOfProducts(ListView productsList, ArrayList<HashMap<String,String>> relatedProducts) {
        ArrayList<String> productsNames = this.getProductsNames(relatedProducts);
        ListAdapter adapter = new ArrayAdapter<>(this.context, android.R.layout.simple_list_item_multiple_choice, productsNames);
        productsList.setAdapter(adapter);
    }
}
