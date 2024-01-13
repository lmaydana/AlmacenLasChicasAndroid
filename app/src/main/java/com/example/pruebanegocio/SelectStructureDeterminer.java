package com.example.pruebanegocio;

import java.util.ArrayList;

public class SelectStructureDeterminer extends StructureDeterminer{

    private final ArrayList<String> requiredFields;

    public SelectStructureDeterminer(String table, ArrayList<String> requiredFields) {
        super(table);
        this.requiredFields = requiredFields;
    }

    public SelectStructureDeterminer(String table) {
        super(table);
        this.requiredFields = new ArrayList<>();
        requiredFields.add("*");
    }

    public String getStructure(){
        String structure = "SELECT ";
        for ( String field: requiredFields) {
            structure += field + ",";
        }
        structure = structure.substring(0, structure.length()-1);
        structure += "FROM "+table;
        return structure;
    }
}
