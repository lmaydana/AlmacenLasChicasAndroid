package com.example.pruebanegocio;

import java.util.ArrayList;
import java.util.HashMap;

public class LiteralConditional extends CommonCondition{
    public LiteralConditional(HashMap<String, ArrayList<String>> values) {
        super(values);
    }

    @Override
    protected int getConnectorAndSpaceSize() {
        return 5;
    }

    @Override
    protected String getFieldCondition(String field, String value) {
        return "(" + field + " = '" + value + "') OR ";
    }
}
