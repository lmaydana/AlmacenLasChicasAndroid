package com.example.pruebanegocio;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductsTableLoader extends TableLoader{
    public ProductsTableLoader(TableLayout table, Activity activity) {
        super(table, activity);
    }

    @Override
    protected View getField(HashMap<String, String> product, String key) {
        EditText field = new TextInputEditText(activity);
        field.setBackgroundResource(android.R.color.transparent);
        field.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
        field.setGravity(Gravity.CENTER_VERTICAL);
        field.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        field.setText(product.get(key));
        field.setTextColor(Color.parseColor("#ffffff"));
        field.setFocusable(false);
        this.setEditTextEvent( product.get("code"), field, key );
        return field;
    }
    private void setEditTextEvent(String code, EditText field, String key){
        if ( key.equals("codigo")){
            return;
        }
        field.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {}

            @Override
            public void onDoubleClick(View v) {
                field.setFocusableInTouchMode(true);
                field.requestFocusFromTouch();
            }
        });

        String[] initialText = {""};
        field.setOnFocusChangeListener((view, hasFocus) -> {
            if( hasFocus ){
                initialText[0] = field.getText().toString();
            }else{
                if(!field.getText().toString().equals(initialText[0])){
                    Intent updaterActivity = new Intent( this.activity, AcceptUpdateActivity.class );
                    updaterActivity.putExtra("field", key);
                    updaterActivity.putExtra("value", field.getText().toString());
                    updaterActivity.putExtra("code", code);
                    this.activity.startActivity(updaterActivity);
                }
            }

        });
    }
}
