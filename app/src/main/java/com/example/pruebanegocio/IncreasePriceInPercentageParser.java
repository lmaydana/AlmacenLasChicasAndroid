package com.example.pruebanegocio;

import android.content.Context;

public class IncreasePriceInPercentageParser extends PriceChangeInstructionParser {
    public IncreasePriceInPercentageParser(String instructionForTheCorrectParser, Context context) {
        super(instructionForTheCorrectParser, context);
        Double representativeNumber = this.getRepresentativeNumber();
        this.addWordToFilter(representativeNumber+"%");
    }

    @Override
    protected String getPrice(Double representativeNumber) {
        Double percentage = 1 + representativeNumber / 100;
        return "precio*" + percentage;
    }

    @Override
    protected String getConnector() {
        return "OR";
    }
}
