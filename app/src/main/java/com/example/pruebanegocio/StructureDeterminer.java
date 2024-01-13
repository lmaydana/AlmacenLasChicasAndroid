package com.example.pruebanegocio;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class StructureDeterminer {

    protected String table;

    public StructureDeterminer(String table){
        this.table = table;
    }

    public abstract String getStructure();
}
