package com.example.pruebanegocio;

import android.app.Activity;
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class RequestsTable {

    public void add(String request, String relatedProduct, String provider, String price ){
        MysqlQueryParser mysqlQueryParser = new MysqlQueryParser();
        HashMap<String, String> values = new HashMap<>();
        values.put("pedido", request);
        values.put("producto_asociado", relatedProduct);
        values.put("proveedor", provider);
        values.put("precio", price);
        mysqlQueryParser.executeQuery(new InsertStructureDeterminer("pedidos", values));
    }

    public void loadTable(TableLayout table, ArrayList<String> relatedWords, Activity activity) {
        this.doWithRelatedProductsTo(relatedWords, relatedProducts-> {
            TableLoader tableLoader = new RequestsTableLoader(table, activity);
            tableLoader.loadTable(relatedProducts);
        });
    }

    public void doWithRelatedProductsTo(ArrayList<String> relatedWords, Consumer<ArrayList<HashMap<String,String>>> functionToDoWithProducts) {
        MysqlQueryParser mysqlQueryParser = new MysqlQueryParser();
        HashMap<String, ArrayList<String>> values = new HashMap<>();
        values.put("descripcion", relatedWords);
        mysqlQueryParser.executeQueryWithResponse(new SelectStructureDeterminer("productos"), new SearcherConditional(values, "OR"), functionToDoWithProducts);
    }

}
