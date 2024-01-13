package com.example.pruebanegocio;

import java.util.HashMap;

public class UpdateStructureDeterminer extends StructureDeterminer{

    private HashMap<String,String> values;
    public UpdateStructureDeterminer(String table, HashMap<String,String> values) {
        super(table);
        this.values = values;
    }

    public String getStructure(){
        String structure = "UPDATE "+table+" SET ";
        for ( String key: values.keySet()) {
            structure += key + "=" + values.get(key) + " , ";
        }
        structure = structure.substring(0, structure.length()-3);
        return structure;
    }
}
