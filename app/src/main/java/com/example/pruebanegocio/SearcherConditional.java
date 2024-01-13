package com.example.pruebanegocio;

import java.util.ArrayList;
import java.util.HashMap;

public class SearcherConditional extends CommonCondition{


    String connector;

    public SearcherConditional(HashMap<String, ArrayList<String>> values, String connector) {
        super(values);
        this.connector = connector;
    }

    @Override
    protected int getConnectorAndSpaceSize() {
        return connector.length() + 2;
    }

    @Override
    protected String getFieldCondition(String field, String value) {
        return "(" + field + " LIKE '%_" + value + "' OR " + field + " LIKE '%_" + value + "_%' OR " + field + " LIKE '" + value + "_%') " + connector + " ";
    }
}
