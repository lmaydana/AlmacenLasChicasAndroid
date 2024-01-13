package com.example.pruebanegocio;

import android.content.Context;
import android.content.Intent;

public class RequestInstruction implements Instruction{

    private Context context;

    private String[] relatedWords;

    private String request;

    private String provider;

    public RequestInstruction(Context context, String[] relatedWords, String request, String provider){
        this.context = context;
        this.relatedWords = relatedWords;
        this.request = request;
        this.provider = provider;
    }
    @Override
    public void execute() {
        Intent requestProductBind = new Intent(this.context, RequestBindingProductActivity.class);
        requestProductBind.putExtra("relatedWords", this.relatedWords);
        requestProductBind.putExtra("request", this.request);
        requestProductBind.putExtra("provider", this.provider);
        this.context.startActivity(requestProductBind);
    }
}
