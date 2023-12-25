package com.example.pruebanegocio;

public class BadOrderException extends Exception{
    public BadOrderException(String message){
        super(message);
    }
}
