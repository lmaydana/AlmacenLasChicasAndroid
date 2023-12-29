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
        String query = getIntent().getExtras().getString("query");
        Button acceptBtn = findViewById(R.id.acceptButton);
        Button cancelButton = findViewById(R.id.cancelButton);
        acceptBtn.setOnClickListener(view -> {
            MySqlConnection mySqlConnection = new MySqlConnection();
            mySqlConnection.mysqlQueryWithoutResponse(query);
            finish();
        });

        cancelButton.setOnClickListener(view -> finish());
    }
}
