package com.example.pruebanegocio;

import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class CrudActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_product);
        Button addProductButton = findViewById(R.id.addProduct);
        addProductButton.setOnClickListener(new BarCodeEvent(this, result->{
            String barcode = result.getContents();
            System.out.println("EL CODIGO: "+ barcode);
        }));

        Button microButton = findViewById(R.id.microButton);
        microButton.setOnClickListener(new MicroEvent(this, result -> {
            ArrayList<String> listenedWordsArray = result.getData() != null ? result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) : new ArrayList<>();
            String listenedWords = listenedWordsArray.get(0);
            System.out.println("Palabras escuchadas:" + listenedWords);
            MySqlConnection mySqlConnection = new MySqlConnection();
            InstructionParser parser = new InstructionParser(listenedWords, mySqlConnection);
            String query = parser.getCorrectInstruction();

            /*ArrayList<HashMap<String,String>> productsThatWillBeModified = parser.getProductsThatWillBeModified();
            if( productsThatWillBeModified.size() > 1 ){
                ArrayList<String> codesForTheProductsToModify = lauchProductSelectIntent();
                String where = getWhere(codesForTheProductsToModify);
                parser.setWhere(where);
            }else {
                mySqlConnection.mysqlQueryWithoutResponse(query);
            }*/
        }));
    }

    private String getWhere(ArrayList<String> codesForTheProductsToModify) {
        return "";
    }

    private ArrayList<String> lauchProductSelectIntent() {
        return null;
    }


}
