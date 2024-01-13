package com.example.pruebanegocio;

import java.util.HashMap;

public class ReplaceStructureDeterminer extends AddStructureDeterminer{
    public ReplaceStructureDeterminer(String table, HashMap<String, String> values) {
        super(table, values);
    }

    @Override
    protected String getQueryType() {
        return "REPLACE";
    }
}
