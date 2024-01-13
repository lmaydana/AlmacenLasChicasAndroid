package com.example.pruebanegocio;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class NullInstructionParser extends SpecificInstructionParser {

    String message;
    public NullInstructionParser(String message, Context context) {
        super("", context);
    }


    @Override
    public Instruction getInstruction() {
        Toast.makeText(this.context, this.message, Toast.LENGTH_SHORT).show();
        throw new RuntimeException(message);
    }
}
