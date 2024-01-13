package com.example.pruebanegocio;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class AcceptUpdateActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_update);
        this.setUpdater();
    }

    private void setUpdater() {
        Bundle extras = this.getIntent().getExtras();
        String field = extras.getString("field");
        String value = extras.getString("value");
        String code = extras.getString("code");

        Button acceptBtn = findViewById(R.id.acceptButton);
        Button cancelButton = findViewById(R.id.cancelButton);
        acceptBtn.setOnClickListener(view -> {
            ProductsTable productsTable = new ProductsTable();
            productsTable.modifyProductFieldWhereCodeEquals( field, value, code );
            finish();
        });

        cancelButton.setOnClickListener(view -> finish());
    }

}
