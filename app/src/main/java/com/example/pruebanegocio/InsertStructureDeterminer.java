package com.example.pruebanegocio;

import java.util.HashMap;

public class InsertStructureDeterminer extends AddStructureDeterminer{
    public InsertStructureDeterminer(String table, HashMap<String, String> values) {
        super(table, values);
    }

    @Override
    protected String getQueryType() {
        return "SELECT";
    }
}
