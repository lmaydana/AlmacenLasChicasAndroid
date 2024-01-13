package com.example.pruebanegocio;

import android.content.Context;

public abstract class PriceChangeInstructionParser extends SpecificInstructionParser {

    public PriceChangeInstructionParser(String stringInstruction, Context context){
        super(stringInstruction, context);
    }

    protected Double getRepresentativeNumber() {

        int lastPriceIndex = stringInstruction.length()-1;

        while ( !Character.isDigit(stringInstruction.charAt(lastPriceIndex)) && lastPriceIndex > 0){
            lastPriceIndex--;
        }

        if( lastPriceIndex == 0 ){
            throw new RuntimeException("No se encontró un número que cuantifique la cantidad del cambio.");
        }

        int firstPriceIndex = lastPriceIndex;

        while ( this.isADoubleNumber(firstPriceIndex) ){
            firstPriceIndex--;
        }
        return Double.parseDouble(stringInstruction.substring(firstPriceIndex+1, lastPriceIndex+1));
    }

    private boolean isADoubleNumber(int index){
        return (Character.isDigit(stringInstruction.charAt(index)) || stringInstruction.charAt(index) == '.');
    }

    public Instruction getInstruction() {
        Double representativeNumber = this.getRepresentativeNumber();
        String[] relatedWords = this.getRelevantWordsRelatedToProduct();
        String priceString = this.getPrice(representativeNumber);
        String connector = this.getConnector();
        return new PriceChangeInstruction(relatedWords, priceString, connector, context);
    }

    protected abstract String getPrice(Double representativeNumber);

    protected abstract String getConnector();
}
