package com.example.pruebanegocio;

import java.util.HashMap;

public class NullCondition extends Conditional{

    public NullCondition() {
        super(new HashMap<>());
    }

    @Override
    public String getCondition() {
        return "1=1";
    }
}
