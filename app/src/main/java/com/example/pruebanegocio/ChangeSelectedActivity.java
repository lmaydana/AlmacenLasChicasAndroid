package com.example.pruebanegocio;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ChangeSelectedActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_product_from_ambiguous_request);
        ListView productsList = findViewById(R.id.productsToChoice);
        Button changeSelectsButton = findViewById(R.id.changeSelecteds);
        final String listenedWords = getIntent().getExtras().getString("listenedWords");
        final InstructionParser parser = new InstructionParser();
        try {
            ArrayList<HashMap<String,String>> productsThatPossiblyWillBeModified = parser.getProductsThatPossiblyWillBeModified(listenedWords);

            if( productsThatPossiblyWillBeModified.size() == 0 ){
                Toast.makeText(this, "No se encontró ningún producto para modificar." ,Toast.LENGTH_SHORT).show();
                finish();
            }

            ArrayList<String> productsNamesThatPossiblyWillBeModified = this.getProductsNames(productsThatPossiblyWillBeModified);
            loadListOfProducts(productsList, productsNamesThatPossiblyWillBeModified);


            changeSelectsButton.setOnClickListener(view -> {
                MySqlConnection mySqlConnectionInternal = new MySqlConnection();
                String where = getWhere();
                parser.setWhere(where);
                try {
                    mySqlConnectionInternal.mysqlQueryWithoutResponse(parser.getCorrectInstruction(listenedWords));
                } catch (BadOrderException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                finish();
            });

            Button cancelButton = findViewById(R.id.cancel);
            cancelButton.setOnClickListener(view -> {
                finish();
            });
        } catch (BadOrderException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private ArrayList<String> getProductsNames(ArrayList<HashMap<String,String>> productsThatWillBeModified){
        ArrayList<String> productsToChoice = new ArrayList<>();
        for (HashMap<String,String> product : productsThatWillBeModified){
            productsToChoice.add(product.get("nombre"));
        }
        return productsToChoice;
    }

    private void loadListOfProducts(ListView productsList, ArrayList<String> productsNames) {
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, productsNames);
        productsList.setAdapter(adapter);

        for (int i = 0; i < productsNames.size(); i++) {
            productsList.setItemChecked(i, true);
        }

    }

    private String getWhere() {
        ArrayList<String> productsNamesThatWillBeModified = this.collectTheSelectedItems();
        String where = "";
        for (String productName: productsNamesThatWillBeModified){
            where += "nombre LIKE '"+productName+"' OR ";
        }
        where = where.substring(0, where.length()-3);
        return where;
    }

    private ArrayList<String> collectTheSelectedItems(){
        ArrayList<String> productNamesThatWillBeModified = new ArrayList<>();
        ListView productsList = findViewById(R.id.productsToChoice);
        for (int i = 0; i < productsList.getAdapter().getCount(); i++) {
            if( productsList.isItemChecked(i) ){
                String selectedProduct = (String) productsList.getItemAtPosition(i);
                System.out.println("El producto: " + selectedProduct + " está seleccionado.");
                productNamesThatWillBeModified.add(selectedProduct);
            }
        }

        return productNamesThatWillBeModified;
    }


}
