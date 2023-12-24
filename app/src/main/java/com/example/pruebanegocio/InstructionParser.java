package com.example.pruebanegocio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class InstructionParser {

    String instruction;

    String where;

    String order;

    ArrayList<String> innecesaryWordsForProductDescription;

    public InstructionParser(String instruction) {
        this.instruction = instruction;
        this.order = this.instruction.split(" ")[0];
        this.innecesaryWordsForProductDescription = new ArrayList<>();
        innecesaryWordsForProductDescription.add("$" + this.getPriceOrPorcentage());
        innecesaryWordsForProductDescription.add(this.getPriceOrPorcentage() + "%");
        innecesaryWordsForProductDescription.add("porciento");
        innecesaryWordsForProductDescription.add("pesos");
        innecesaryWordsForProductDescription.add("" + this.getPriceOrPorcentage());
        innecesaryWordsForProductDescription.add("en");
        innecesaryWordsForProductDescription.add("el");
        innecesaryWordsForProductDescription.add("la");
        innecesaryWordsForProductDescription.add("a");
        innecesaryWordsForProductDescription.add("un");
        innecesaryWordsForProductDescription.add("una");
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

    private String getQueryWhere(String order){
        HashMap<String, String> connectors = new HashMap<>();
        connectors.put("cambiar", "AND");
        connectors.put("aumentar", "OR");
        String where = "";
        ArrayList<String> separatedProductDescription = this.getProductDescription(order);
        for (String partOfProductDescription : separatedProductDescription) {
            System.out.println("Parte de la descripcion: " + partOfProductDescription);
            where += "descripcion LIKE '%" + partOfProductDescription.toLowerCase() + "%' " + connectors.get(order) + " ";
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

    private ArrayList<String> getProductDescription(String action){

        String instructionCopy = String.copyValueOf(this.instruction.toCharArray());
        ArrayList<String> partsOfTheProductDescription = new ArrayList<>(Arrays.asList(instructionCopy.split(" ")));
        partsOfTheProductDescription.remove(action);
        for (String word: innecesaryWordsForProductDescription) {
            partsOfTheProductDescription.remove(word);
            System.out.println("Array a String: " + partsOfTheProductDescription + ", Palabra borrada: " + word);
        }

        partsOfTheProductDescription.remove("");
        return partsOfTheProductDescription;
    }
}
