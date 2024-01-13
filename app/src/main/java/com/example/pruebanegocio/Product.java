package com.example.pruebanegocio;

import android.util.Log;

public class Product {

    private String barcode;

    private String name;

    private Double price;

    private Double percentage;

    private String description;

    public Product(String barcode, String name, Double price, Double percentage, String description) {
        this.barcode = barcode;
        MySqlQueryPro mySqlQueryPro = new MySqlQueryPro("http://186.123.109.86:8888/almacen/json_de_productos.php");
        mySqlQueryPro.mysqlQuery("INSERT INTO productos (codigo, nombre, precio, porcentaje, description) VALUES ("+barcode+", '"+name+"', "+price+", "+percentage+", '"+description+"')", (result)->{
        });

    }

    public String getBarcode() {
        return barcode;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Double getPercentage() {
        return percentage;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
