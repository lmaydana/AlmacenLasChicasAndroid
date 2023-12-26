package com.example.pruebanegocio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class InstructionParser {

    private String where;

    private ArrayList<String> orders;

    private ArrayList<String> innecesaryWordsForProductDescription;

    public InstructionParser()  {
        this.orders = new ArrayList<>();
        this.orders.add("cambiar");
        this.orders.add("aumentar");
        this.orders.add("pedir");
        this.innecesaryWordsForProductDescription = new ArrayList<>();
        innecesaryWordsForProductDescription.add("porciento");
        innecesaryWordsForProductDescription.add("pesos");
        innecesaryWordsForProductDescription.add("en");
        innecesaryWordsForProductDescription.add("el");
        innecesaryWordsForProductDescription.add("la");
        innecesaryWordsForProductDescription.add("a");
        innecesaryWordsForProductDescription.add("un");
        innecesaryWordsForProductDescription.add("una");
        innecesaryWordsForProductDescription.add("lo");
        innecesaryWordsForProductDescription.add("de");
    }

    public String getCorrectInstruction(String instruction) throws BadOrderException {

        String order = this.getOrder(instruction);
        this.addInnecessaryWords(instruction);

        String newPrice = this.getPriceOrPorcentage(instruction);

        switch (order.toLowerCase()){
            case "cambiar":
                break;
            case "aumentar":
                Double porcentage = 1 + Double.parseDouble(newPrice) / 100;
                newPrice = "precio*" + porcentage;
                break;
        }

        System.out.println("La consulta es: " + "UPDATE productos SET precio = " + newPrice + " WHERE " + this.where + " COLLATE utf8_bin");

        return "UPDATE productos SET precio = " + newPrice + " WHERE " + this.where + " COLLATE utf8_bin";
    }

    private String getOrder(String instruction) throws BadOrderException {
        String order = instruction.split(" ")[0];
        if( !this.orders.contains(order) ){
            throw new BadOrderException("La orden dada no existe");
        }
        return order;
    }

    public void setWhere(String where){
        this.where = where + " COLLATE utf8_bin";
    }

    private void addInnecessaryWords(String instruction) throws BadOrderException {
        innecesaryWordsForProductDescription.add("$" + this.getPriceOrPorcentage(instruction));
        innecesaryWordsForProductDescription.add(this.getPriceOrPorcentage(instruction) + "%");
        innecesaryWordsForProductDescription.add("" + this.getPriceOrPorcentage(instruction));
    }

    public ArrayList<HashMap<String, String>> getProductsThatPossiblyWillBeModified(String instruction) throws BadOrderException {
        this.addInnecessaryWords(instruction);
        MySqlConnection mySqlConnection = new MySqlConnection();
        System.out.println("Consulta de productos a modificar:" + "SELECT * FROM productos WHERE " + this.getQueryWhere(instruction));
        ArrayList<HashMap<String, String>> obtainedObjects = mySqlConnection.mysqlQueryToArrayListOfObjects("SELECT * FROM productos WHERE " + this.getQueryWhere(instruction));
        return obtainedObjects;
    }

    private String getQueryWhere(String instruction) throws BadOrderException {
        HashMap<String, String> connectors = new HashMap<>();
        String order = this.getOrder(instruction);
        connectors.put("cambiar", "AND");
        connectors.put("aumentar", "OR");
        String where = "";
        ArrayList<String> separatedProductDescription = this.getProductDescription(instruction);
        for (String partOfProductDescription : separatedProductDescription) {
            //System.out.println("Parte de la descripcion: " + partOfProductDescription);
            where += "UPPER(descripcion) LIKE UPPER('%" + partOfProductDescription.toLowerCase() + "%') " + connectors.get(order) + " ";
        }
        where = where.substring(0, where.length() - 4);
        where = where + " COLLATE utf8_bin";
        return where;
    }

    private String getPriceOrPorcentage(String instruction) throws BadOrderException {

        int lastPriceIndex = instruction.length()-1;

        while (!Character.isDigit(instruction.charAt(lastPriceIndex)) && lastPriceIndex > 0){
            lastPriceIndex--;
        }

        if( lastPriceIndex == 0 ){
            throw new BadOrderException("No se encontro un numero que cuantifique la cantidad del cambio");
        }

        int firstPriceIndex = lastPriceIndex;

        while ( Character.isDigit(instruction.charAt(firstPriceIndex)) ){
            firstPriceIndex--;
        }
        return instruction.substring(firstPriceIndex+1, lastPriceIndex+1);
    }

    private ArrayList<String> getProductDescription(String instruction) throws BadOrderException {
        String order = this.getOrder(instruction);
        String instructionCopy = String.copyValueOf(instruction.toCharArray());
        ArrayList<String> partsOfTheProductDescription = new ArrayList<>(Arrays.asList(instructionCopy.split(" ")));
        partsOfTheProductDescription.remove(order);
        for (String word: innecesaryWordsForProductDescription) {
            partsOfTheProductDescription.remove(word);
            //System.out.println("Array a String: " + partsOfTheProductDescription + ", Palabra borrada: " + word);
        }

        partsOfTheProductDescription.remove("");
        return partsOfTheProductDescription;
    }
}
