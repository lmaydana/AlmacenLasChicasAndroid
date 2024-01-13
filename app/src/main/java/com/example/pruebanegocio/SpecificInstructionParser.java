package com.example.pruebanegocio;

import android.content.Context;

public abstract class SpecificInstructionParser {
    protected String stringInstruction;

    protected Filter filter;

    protected Context context;
    public SpecificInstructionParser( String stringInstruction, Context context){
        this.context = context;
        this.stringInstruction = stringInstruction;
        this.filter = new Filter(stringInstruction);
    }

    protected void addWordToFilter(String wordToFilter){
        this.filter.addWordToFilter(wordToFilter);
    }

    protected String[] getRelevantWordsRelatedToProduct(){
        return this.filter.getFilteredWordsInStringArray();
    }

    public abstract Instruction getInstruction();
}
