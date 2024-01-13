package com.example.pruebanegocio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class MysqlQueryParser {

    MySqlQueryPro mySqlConnection;
    public MysqlQueryParser(){
        mySqlConnection = new MySqlQueryPro("http://186.123.109.86:8888/almacen/json_de_productos.php");
    }

    public void executeQuery(StructureDeterminer structureDeterminer, Conditional conditional){
        String query = structureDeterminer.getStructure() + " WHERE " + conditional.getCondition();
        System.out.println("LA CONSULTA POO:" + query);
        mySqlConnection.mysqlQueryWithoutResponse(query);
    }

    public void executeQuery(StructureDeterminer structureDeterminer){
        this.executeQuery(structureDeterminer, new NullCondition());
    }

    public void executeQueryWithResponse(StructureDeterminer structureDeterminer, Conditional conditional, Consumer<ArrayList<HashMap<String, String>>> toDoWithObjects){
        String query = structureDeterminer.getStructure() + " WHERE " + conditional.getCondition();
        System.out.println("LA CONSULTA POO RESPUESTA:" + query);
        mySqlConnection.mysqlQuery(query, toDoWithObjects);
    }

}
