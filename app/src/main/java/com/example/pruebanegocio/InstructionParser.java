package com.example.pruebanegocio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class InstructionParser {

    String instruction;

    String where;

    String order;

    ArrayList<String> innecesaryWordsForProductName;

    public InstructionParser(String instruction) {
        this.instruction = instruction;
        this.order = this.instruction.split(" ")[0];
        this.innecesaryWordsForProductName = new ArrayList<>();
        innecesaryWordsForProductName.add("$" + this.getPriceOrPorcentage());
        innecesaryWordsForProductName.add(this.getPriceOrPorcentage() + "%");
        innecesaryWordsForProductName.add("porciento");
        innecesaryWordsForProductName.add("pesos");
        innecesaryWordsForProductName.add("" + this.getPriceOrPorcentage());
        innecesaryWordsForProductName.add("en");
        innecesaryWordsForProductName.add("el");
        innecesaryWordsForProductName.add("la");
        innecesaryWordsForProductName.add("a");
        innecesaryWordsForProductName.add("un");
        innecesaryWordsForProductName.add("una");
        this.where = this.getQueryWhere(this.order);
    }

    public String getCorrectInstruction() {
        String newPrice = "";

        switch (order){
            case "cambiar":
                newPrice = this.getPriceOrPorcentage();
                break;
            case "aumentar":
                Double porcentage = 1 + Double.parseDouble(this.getPriceOrPorcentage()) / 100;
                newPrice = "precio*" + porcentage;
        }

        System.out.println("La consulta es: " + "UPDATE productos SET precio = " + newPrice + " WHERE " + this.where);

        return "UPDATE productos SET precio = " + newPrice + " WHERE " + this.where;
    }

    public void setWhere(String where){
        this.where = where;
    }



    public ArrayList<HashMap<String, String>> getProductsThatPossiblyWillBeModified(){
        String order = this.instruction.split(" ")[0];
        MySqlConnection mySqlConnection = new MySqlConnection();
        ArrayList<HashMap<String, String>> obtainedObjects = mySqlConnection.mysqlQueryToArrayListOfObjects("SELECT * FROM productos WHERE " + this.getQueryWhere(order));
        return obtainedObjects;
    }

    private String getCorrectQuery(ArrayList<String> specialCharacterForms, String newPrice) {
        String query = "";

        query = "UPDATE productos SET precio = " + newPrice + " WHERE " + this.where;
        System.out.println("La consulta quedo como: " + query);

        return query;
    }

    private String getQueryWhere(String order){
        HashMap<String, String> connectors = new HashMap<>();
        connectors.put("cambiar", "AND");
        connectors.put("aumentar", "OR");
        String where = "";
        ArrayList<String> separatedProductName = this.getProductName(order);
        for (String partOfProducName : separatedProductName) {
            System.out.println("Parte del nombre: " + partOfProducName);
            where += "nombre LIKE '%" + partOfProducName.toLowerCase() + "%' " + connectors.get(order) + " ";
        }
        where = where.substring(0, where.length() - 4);
        return where;
    }

    public String getPriceOrPorcentage(){

        int lastPriceIndex = this.instruction.length()-1;

        while (!Character.isDigit(this.instruction.charAt(lastPriceIndex))){
            lastPriceIndex--;
        }

        int firstPriceIndex = lastPriceIndex;

        while ( Character.isDigit(this.instruction.charAt(firstPriceIndex)) ){
            firstPriceIndex--;
        }
        return this.instruction.substring(firstPriceIndex+1, lastPriceIndex+1);
    }

    private ArrayList<String> getProductName(String action){

        String instructionCopy = String.copyValueOf(this.instruction.toCharArray());
        ArrayList<String> partsOfTheProductName = new ArrayList<>(Arrays.asList(instructionCopy.split(" ")));
        partsOfTheProductName.remove(action);
        for (String word: innecesaryWordsForProductName) {
            partsOfTheProductName.remove(word);
            System.out.println("Array a String: " + partsOfTheProductName.toString() + ", Palabra borrada: " + word);
        }

        partsOfTheProductName.remove("");
        return partsOfTheProductName;
    }
}
