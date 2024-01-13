package com.example.pruebanegocio;

import java.util.HashMap;

public abstract class AddStructureDeterminer extends StructureDeterminer{

    private HashMap<String,String> values;

    public AddStructureDeterminer(String table, HashMap<String ,String> values) {
        super(table);
        this.values = values;
    }

    public String getStructure(){
        String queryFields = this.getQueryType() + " INTO "+table+"(";
        String queryValues = " VALUES (";
        for (String key: values.keySet()){
            queryFields += key + ", ";
            queryValues += values.get(key) + ", ";
        }
        queryFields = queryFields.substring(0, queryFields.length()-2) + ")";
        queryValues = queryValues.substring(0, queryValues.length()-2) + ")";
        return queryFields+queryValues;
    }

    protected abstract String getQueryType();
}
