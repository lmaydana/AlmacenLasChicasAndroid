package com.example.pruebanegocio;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Conditional {

    protected HashMap<String, ArrayList<String>> values;

    public Conditional(HashMap<String, ArrayList<String>> values){

        this.values = values;
    }

    public abstract String getCondition();


}
