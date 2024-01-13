package com.example.pruebanegocio;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestsTableLoader extends TableLoader {
    public RequestsTableLoader(TableLayout table, Activity activity) {
        super(table, activity);
    }

    @Override
    protected View getField(HashMap<String, String> request, String key) {
        HashMap<String,Float> weights = new HashMap<>();
        weights.put("pedido", 3.0f);
        weights.put("producto_asociado", 3.0f);
        weights.put("proveedor", 3.0f);
        weights.put("precio", 1.0f);
        HashMap<String, TextView> rowView = new HashMap<>();
        AutoCompleteTextView providers = this.getProvidersView(request);
        rowView.put("proveedor", providers);
        TextView field = rowView.getOrDefault(key, new TextView(this.activity));
        field.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, weights.get(key)));
        field.setGravity(Gravity.CENTER_VERTICAL);
        field.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        field.setText(request.get(key));
        field.setTextColor(Color.parseColor("#ffffff"));
        return field;
    }

    private AutoCompleteTextView getProvidersView(HashMap<String, String> request){
        AutoCompleteTextView providers = new AutoCompleteTextView(this.activity);
        this.fillListWithQuery(providers, "SELECT nombre_proveedor FROM proveedores", "nombre_proveedor");
        providers.setOnItemClickListener((adapterView, view, i, l) -> {
            MySqlConnection mySqlConnection = new MySqlConnection();
            mySqlConnection.mysqlQueryWithoutResponse("UPDATE pedidos SET proveedor = '"+adapterView.getAdapter().getItem(i)+"' WHERE pedido ='"+request.get("pedido")+"'");//Seguir con el where
        });
        return providers;
    }

    private void fillListWithQuery(AutoCompleteTextView list, String query, String field) {
        MySqlConnection mySqlConnection = new MySqlConnection();
        ArrayList<HashMap<String, String>> entities = mySqlConnection.mysqlQueryToArrayListOfObjects(query);
        ArrayList<String> entitiesField = this.getEntitiesFieldInArrayList(entities, field);
        ArrayAdapter<String> entitiesAdapter = new ArrayAdapter<>(this.activity, android.R.layout.simple_dropdown_item_1line, entitiesField);
        list.setAdapter(entitiesAdapter);
    }

    private ArrayList<String> getEntitiesFieldInArrayList( ArrayList<HashMap<String, String>> entities, String field ) {
        ArrayList<String> entitiesField = new ArrayList<>();
        for ( HashMap<String, String> entity : entities){
            entitiesField.add(entity.get(field));
        }
        return entitiesField;
    }
}
