package com.example.pruebanegocio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.InputType;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CrudActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_product);
        this.setInitialThings();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.loadProductsTable("");
    }

    private void setInitialThings(){


        this.loadProductsTable("");

        Button addProductButton = findViewById(R.id.addProduct);
        addProductButton.setOnClickListener(new BarCodeEvent(this, result->{
            if( result.getContents() != null ) {
                String barcode = result.getContents();
                Intent addProductActivity = new Intent(this, AddProductActivity.class);
                addProductActivity.putExtra("barcode", barcode);
                startActivity(addProductActivity);
            }
        }));

        Button microButton = findViewById(R.id.microButton);
        microButton.setOnClickListener(new MicroEvent(this, result -> {
            ArrayList<String> listenedWordsArray = result.getData() != null ? result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) : new ArrayList<>();
            if( listenedWordsArray.size() > 0 ) {
                String listenedWords = listenedWordsArray.get(0);
                launchProductSelectIntent(listenedWords);
            }
        }));

        SearchView productSearch = findViewById(R.id.searchBar);
        productSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String where = getWhere(s);
                loadProductsTable(where);
                return false;
            }
        });


    }

    private String getWhere(String keyWords){
        String[] separatedKeyWords = keyWords.split(" ");
        String where = "";
        ArrayList<String> arrayListKeyWords = new ArrayList<>(Arrays.asList(separatedKeyWords));
        ArrayList<String> innecesaryWordsForProductDescription = new ArrayList<>();
        innecesaryWordsForProductDescription.add("en");
        innecesaryWordsForProductDescription.add("el");
        innecesaryWordsForProductDescription.add("la");
        innecesaryWordsForProductDescription.add("a");
        innecesaryWordsForProductDescription.add("un");
        innecesaryWordsForProductDescription.add("una");
        innecesaryWordsForProductDescription.add("lo");
        innecesaryWordsForProductDescription.add("de");
        arrayListKeyWords.removeAll(innecesaryWordsForProductDescription);
        for ( String keyWord : arrayListKeyWords){
            where+=  "(UPPER(descripcion) LIKE UPPER('%_"+keyWord+"%') OR UPPER(descripcion) LIKE UPPER( '"+keyWord+"%' ) OR UPPER(descripcion) LIKE UPPER('%_"+keyWord+"')) AND ";
        }
        return where.substring(0, where.length()-4);
    }

    private void loadProductsTable(String where) {
        MySqlConnection mySqlConnection = new MySqlConnection();
        ArrayList<HashMap<String,String>> products = mySqlConnection.mysqlQueryToArrayListOfObjects("SELECT * FROM productos ORDER BY nombre ASC LIMIT 30 " + where);
        ArrayList<String> colors = new ArrayList<>();
        TableLayout productsTable = findViewById(R.id.productsTable);
        colors.add("#0E0C0C");
        colors.add("#CC100F0F");
        productsTable.removeAllViews();
        int colorIndex = 0;
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
        keys.add("codigo");
        keys.add("nombre");
        keys.add("precio");
        keys.add("porcentaje");
        HashMap<String, EditText> fields = new HashMap<>();
        for (String key: keys){
            EditText productAttribute = new TextInputEditText(this);
            productAttribute.setBackgroundResource(android.R.color.transparent);
            productAttribute.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
            productAttribute.setGravity(Gravity.CENTER_VERTICAL);
            productAttribute.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            productAttribute.setText(product.get(key));
            productAttribute.setTextColor(Color.parseColor("#ffffff"));
            fields.put(key, productAttribute);
            newRow.addView(productAttribute);
        }

        this.setFieldsListener(fields);

        productsTable.addView(newRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    private void setFieldsListener(HashMap<String, EditText> fields) {
        EditText codeField = fields.remove("codigo");
        codeField.setFocusable(false);
        String code = codeField.getText().toString();
        fields.forEach( (key, field)->{
            field.setFocusable(false);
            field.setOnClickListener(new DoubleClickListener() {
                @Override
                public void onSingleClick(View v) {

                }

                @Override
                public void onDoubleClick(View v) {
                    field.setFocusableInTouchMode(true);
                    field.requestFocusFromTouch();
                }
            });
            final String[] initialText = {""};
            field.setOnFocusChangeListener((view, b) -> {
                if( b ) {
                    initialText[0] = field.getText().toString();
                }else {
                    if( !initialText[0].equals(field.getText().toString()) ) {
                        Intent updaterActivity = new Intent(this, AcceptUpdateActivity.class);
                        String query = "UPDATE productos SET " + key + " = '" + field.getText().toString() + "' WHERE codigo = " + code;
                        updaterActivity.putExtra("query", query);
                        startActivity(updaterActivity);
                    }
                }
            });
        });
    }

    private void launchProductSelectIntent(String listenedWords) {
        Intent changeProductIntent = new Intent(this, ChangeSelectedActivity.class);
        changeProductIntent.putExtra("listenedWords", listenedWords);
        startActivity(changeProductIntent);
    }

}
