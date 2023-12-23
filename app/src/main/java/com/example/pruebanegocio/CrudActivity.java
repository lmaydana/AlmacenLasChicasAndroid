package com.example.pruebanegocio;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class CrudActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_product);
        Button addProductButton = findViewById(R.id.addProduct);
        addProductButton.setOnClickListener(new BarCodeEvent(this));
    }


}
