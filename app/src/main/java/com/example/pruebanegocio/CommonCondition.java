package com.example.pruebanegocio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public abstract class CommonCondition extends Conditional{
    public CommonCondition(HashMap<String, ArrayList<String>> values) {
        super(values);
    }

    public String getCondition() {
        String condition = "(";
        for (String field : values.keySet()) {
            ArrayList<String> relatedWords = values.get(field);
            Iterator<String> relatedWordsIterator = relatedWords.iterator();
            condition += this.fieldCondition(field, relatedWordsIterator);
            condition += ") AND (";
        }
        return condition.substring( 0, condition.length() - 5 );
    }

    private String fieldCondition(String field, Iterator<String> relatedWordsIterator ){
        String condition = "";
        while ( relatedWordsIterator.hasNext() ) {
            String value = relatedWordsIterator.next();
            condition = this.getFieldCondition( field, value );
        }
        return condition.substring( 0, condition.length() - getConnectorAndSpaceSize() );
    }

    protected abstract int getConnectorAndSpaceSize();

    protected abstract String getFieldCondition(String field, String value);
}
