package com.example.pruebanegocio;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class CrudActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_product);
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

    private void lauchProductSelectIntent(String listenedWords) {
        Intent changeProductIntent = new Intent(this, ChangeSelectedActivity.class);
        changeProductIntent.putExtra("listenedWords", listenedWords);
        startActivity(changeProductIntent);
    }





}
