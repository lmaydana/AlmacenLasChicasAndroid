package com.example.pruebanegocio;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ChangeSelectedActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_product_from_ambiguous_request);

        ListView productsListNew = findViewById(R.id.productsToChoice);
        Button changeSelectsButtonNew = findViewById(R.id.changeSelecteds);
        Button cancelButtonNew = findViewById(R.id.cancel);

        Bundle extras = getIntent().getExtras();

        String[] relatedWordsStringArray = extras.getStringArray("relatedWords");
        ArrayList<String> relatedWords = new ArrayList<>(Arrays.asList(relatedWordsStringArray));
        String priceString = extras.getString("priceString");
        String connector = extras.getString("connector");

        ProductsTable productsTable = new ProductsTable();
        productsTable.doWithRelatedProductsTo(relatedWords, relatedProducts->{
            if( relatedProducts.size() == 0 ){
                Toast.makeText(this, "No se encontró ningún producto para modificar." ,Toast.LENGTH_SHORT).show();
                finish();
            }
            ProductsListLoader productsListLoader = new ProductsListLoader(this);
            productsListLoader.loadListOfProducts(productsListNew, relatedProducts);
            this.checkProducts(productsListNew, relatedProducts.size(), connector);
        });

        changeSelectsButtonNew.setOnClickListener(view -> {
            productsTable.modifyProductPriceWhereNameLike(priceString, this.collectTheSelectedItems());
            finish();
        });

        cancelButtonNew.setOnClickListener( view -> finish() );


        //--------------------ABAJO LO VIEJO-------------------------------------------------------------------------------------------------------------------
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

            ProductsListLoader productsListLoader = new ProductsListLoader(this);
            productsListLoader.loadListOfProducts(productsListNew, productsThatPossiblyWillBeModified);
            this.checkProducts(productsListNew, productsThatPossiblyWillBeModified.size(), connector);


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
        //------------------------------------------------------------------------------------------------------------------------------------------------------
    }

    private void checkProducts(ListView productsList, int namesAmount, String connector){
        for (int i = 0; connector.equals("OR") && i < namesAmount; i++) {
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
                String selectedProduct = productsList.getItemAtPosition(i).toString();
                System.out.println("Producto Seleccionado:"+selectedProduct);
                productNamesThatWillBeModified.add(selectedProduct);
            }
        }

        return productNamesThatWillBeModified;
    }


}
