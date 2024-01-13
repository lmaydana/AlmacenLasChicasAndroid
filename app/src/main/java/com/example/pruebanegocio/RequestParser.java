package com.example.pruebanegocio;

import android.content.Context;

public class RequestParser extends SpecificInstructionParser {

    private String provider;

    public RequestParser(String instructionForTheCorrectParser, Context context, String provider) {
        super(instructionForTheCorrectParser, context);
        this.provider = provider;
    }

    @Override
    public Instruction getInstruction() {
        return new RequestInstruction(this.context, this.getRelevantWordsRelatedToProduct(), this.stringInstruction, this.provider);
    }
}
