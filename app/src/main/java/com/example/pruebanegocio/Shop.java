package com.example.pruebanegocio;

public class Shop {

    public Shop(){

    }

    public void addProduct( String barcode, String name, String price, String percentage, String description ){
        Product newProduct = new Product(barcode, name, Double.parseDouble(price), Double.parseDouble(percentage), description);
    }
}
