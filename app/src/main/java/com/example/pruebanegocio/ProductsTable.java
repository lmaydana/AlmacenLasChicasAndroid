package com.example.pruebanegocio;

import android.app.Activity;
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class ProductsTable extends ObjectTable{

    public ProductsTable(){

    }

    public void addOrReplace(String productBarcode, String productName, String productPrice, String productPercentage, String productDescription){
        MysqlQueryParser mysqlQueryParser = new MysqlQueryParser();
        HashMap<String, String> valuesToAdd = new HashMap<>();
        valuesToAdd.put("codigo", productBarcode);
        valuesToAdd.put("nombre", productName);
        valuesToAdd.put("precio", productPrice);
        valuesToAdd.put("porcentaje", productPercentage);
        valuesToAdd.put("descripcion", productName + productDescription);

        mysqlQueryParser.executeQuery(new ReplaceStructureDeterminer("productos", valuesToAdd));
    }

    public void doWithRelatedProductsTo(ArrayList<String> relatedWords, Consumer<ArrayList<HashMap<String,String>>> functionToDoWithProducts) {
        MysqlQueryParser mysqlQueryParser = new MysqlQueryParser();
        HashMap<String, ArrayList<String>> values = new HashMap<>();
        values.put("descripcion", relatedWords);
        mysqlQueryParser.executeQueryWithResponse(new SelectStructureDeterminer("productos"), new SearcherConditional(values, "OR"), functionToDoWithProducts);
    }

    public void modifyProductPriceWhereNameLike(String priceString, ArrayList<String> keyWords) {
        this.updateProduct("precio", priceString, "nombre", keyWords);
    }

    public void modifyProductFieldWhereCodeEquals(String field, String value, String code) {
        ArrayList<String> keyWords = new ArrayList<>();
        keyWords.add(code);
        this.updateProduct(field, value, "codigo", keyWords);
    }

    private void updateProduct(String fieldToChange, String value, String conditionalField, ArrayList<String> keyWords){
        MysqlQueryParser mysqlQueryParser = new MysqlQueryParser();
        HashMap<String, String> valuesToChange = new HashMap<>();
        valuesToChange.put(fieldToChange, value);
        HashMap<String, ArrayList<String>> conditionKeys = new HashMap<>();
        conditionKeys.put(conditionalField, keyWords);
        mysqlQueryParser.executeQuery(new UpdateStructureDeterminer("productos", valuesToChange), new LiteralConditional(conditionKeys));
    }

    public void loadTable(TableLayout table, ArrayList<String> relatedWords, Activity activity) {
        this.doWithRelatedProductsTo(relatedWords, relatedProducts-> {
            TableLoader tableLoader = new ProductsTableLoader(table, activity);
            tableLoader.loadTable(relatedProducts);
        });
    }

    protected TableLoader getTableLoader( TableLayout table, Activity activity ){
        return new ProductsTableLoader(table, activity);
    }

}
