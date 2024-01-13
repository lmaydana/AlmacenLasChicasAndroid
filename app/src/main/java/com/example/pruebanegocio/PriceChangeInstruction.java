package com.example.pruebanegocio;

import android.content.Context;
import android.content.Intent;

public class PriceChangeInstruction implements Instruction{

    private String[] relatedWords;

    private String priceString;

    private String connector;

    private Context context;

    public PriceChangeInstruction(String[] relatedWords, String priceString, String connector, Context context){
        this.relatedWords = relatedWords;
        this.priceString = priceString;
        this.connector = connector;
        this.context = context;
    }

    @Override
    public void execute() {
        Intent changeProductIntent = new Intent(this.context, ChangeSelectedActivity.class);
        changeProductIntent.putExtra("relatedWords", this.relatedWords);
        changeProductIntent.putExtra("priceString", this.priceString);
        changeProductIntent.putExtra("connector", this.connector);
        context.startActivity(changeProductIntent);
    }
}
