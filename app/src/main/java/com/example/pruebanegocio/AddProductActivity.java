package com.example.pruebanegocio;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class AddProductActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);
        Button addProductButton = findViewById(R.id.addProductBtn);
        TextInputEditText nameET = findViewById(R.id.productNameET);
        TextInputEditText priceET = findViewById(R.id.priceET);
        TextInputEditText porcentageET = findViewById(R.id.porcentageET);
        String productBarcode = getIntent().getExtras().getString("barcode");
        addProductButton.setOnClickListener(view -> {
            String productName = nameET.getText().toString();
            String productPrice = priceET.getText().toString();
            String productPorcentage = porcentageET.getText().toString();
            MySqlConnection mySqlConnection = new MySqlConnection();
            mySqlConnection.mysqlQueryWithoutResponse("INSERT INTO productos (codigo,nombre, precio, porcentaje) VALUES ("+productBarcode+",'"+productName+"',"+productPrice+","+productPorcentage+")");
            finish();
        });
    }


}
