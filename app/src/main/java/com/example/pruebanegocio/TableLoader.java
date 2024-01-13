package com.example.pruebanegocio;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public abstract class TableLoader {

    protected Activity activity;

    private TableLayout table;

    public TableLoader(TableLayout table, Activity activity) {
        this.table = table;
        this.activity = activity;

    }

    public void loadTable(ArrayList<HashMap<String,String>> relatedEntities){
        ArrayList<String> colors = new ArrayList<>();
        colors.add("#0E0C0C");
        colors.add("#CC100F0F");
        table.removeAllViews();
        int colorIndex = 0;
        for (HashMap<String, String> entity: relatedEntities){
            this.addNewRow(entity, colors.get(colorIndex%2));
            colorIndex++;
        }
    }

    private void addNewRow(HashMap<String, String> entity, String color) {
        TableRow newRow = new TableRow(activity);
        newRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        newRow.setBackgroundColor(Color.parseColor(color));
        Set<String> keys = entity.keySet();
        for (String key: keys){
            View field = this.getField(entity, key);
            newRow.addView(field);
        }

        table.addView(newRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }

    protected abstract View getField(HashMap<String, String> entity, String key);


}
