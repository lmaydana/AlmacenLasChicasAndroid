package com.example.pruebanegocio;

import android.content.Context;

public class PriceUpdateInstructionParser extends PriceChangeInstructionParser {

    public PriceUpdateInstructionParser(String stringInstruction, Context context) {
        super(stringInstruction, context);
        Double representativeNumber = this.getRepresentativeNumber();
        this.addWordToFilter(representativeNumber+"$");
    }

    @Override
    protected String getPrice(Double representativeNumber) {
        return representativeNumber.toString();
    }

    @Override
    protected String getConnector() {
        return "AND";
    }
}
