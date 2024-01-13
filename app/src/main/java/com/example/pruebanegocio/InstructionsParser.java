package com.example.pruebanegocio;

import android.content.Context;

public class InstructionsParser {
    private String instruction;

    private Context context;

    private SpecificInstructionParser correctInstructionParser;

    public InstructionsParser(String instruction, Context context){
        this(instruction, context, "");
    }

    public InstructionsParser(String instruction, Context context, String extra){
        this.instruction = instruction;
        this.context = context;
        String order = this.instruction.split(" ")[0];
        String instructionForTheCorrectParser;
        if( this.instruction.length() > order.length() ){
            instructionForTheCorrectParser = instruction.substring(order.length(), instruction.length());
            switch (order.toLowerCase()){
                case "cambiar":
                    correctInstructionParser = new PriceUpdateInstructionParser(instructionForTheCorrectParser, context);
                case "aumentar":
                    correctInstructionParser = new IncreasePriceInPercentageParser(instructionForTheCorrectParser, context);
                case "pedir":
                    correctInstructionParser = new RequestParser(instructionForTheCorrectParser, context, extra);
                default:
                    correctInstructionParser = new NullInstructionParser("La orden dada no existe", context);
            }
        }else{
            this.correctInstructionParser = new NullInstructionParser("Faltan datos en la instruccion", context);
        }
    }

    public Instruction getInstruction(){
        return this.correctInstructionParser.getInstruction();
    }


}
