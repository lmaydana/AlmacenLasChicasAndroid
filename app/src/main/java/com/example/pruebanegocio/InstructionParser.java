package com.example.pruebanegocio;

import java.util.ArrayList;
import java.util.Arrays;

public class InstructionParser {

    String instruction;

    MySqlConnection connection;

    ArrayList<String> innecesaryWordsForProductName;

    public InstructionParser(String instruction, MySqlConnection connection){
        this.instruction = instruction;
        this.connection = connection;

        this.innecesaryWordsForProductName = new ArrayList<>();
        innecesaryWordsForProductName.add("$"+this.getPriceOrPorcentage());
        innecesaryWordsForProductName.add(this.getPriceOrPorcentage()+" pesos");
        innecesaryWordsForProductName.add(this.getPriceOrPorcentage()+"%");
        innecesaryWordsForProductName.add(this.getPriceOrPorcentage()+" porciento");
        innecesaryWordsForProductName.add(" en un ");
        innecesaryWordsForProductName.add(" en ");
        innecesaryWordsForProductName.add(" el ");
        innecesaryWordsForProductName.add(" la ");
        innecesaryWordsForProductName.add(" a ");
        innecesaryWordsForProductName.add(" un ");
        innecesaryWordsForProductName.add(" una ");
    }

    public String getCorrectInstruction(){
        String query = "";
        String order = this.instruction.split(" ")[0];
        ArrayList<String> specialCharacterForms = new ArrayList<>();
        switch (order) {
            case "cambiar":
                specialCharacterForms.add("$");
                specialCharacterForms.add("pesos");
                query = this.updateProductPrice(order, specialCharacterForms, this.getPriceOrPorcentage(), "AND");
                break;
            case "aumentar":
                specialCharacterForms.add("%");
                specialCharacterForms.add("porcentaje");
                Double porcentage = 1 + Double.parseDouble(this.getPriceOrPorcentage())/100;
                query = this.updateProductPrice(order, specialCharacterForms, "precio*"+porcentage, "OR");
        }

       return query;
    }

    private String updateProductPrice(String order, ArrayList<String> specialCharacterForms, String newPrice, String connector){
        String query = "";
        if( this.instructionContainsSomeoneOfTheseWords(specialCharacterForms) ){
            ArrayList<String> separatedProductName = this.getProductName(order);
            query = "UPDATE productos SET precio = " + newPrice + " WHERE ";
            for (String partOfProducName: separatedProductName){
                System.out.println("Parte del nombre: " + partOfProducName);
                query += "nombre LIKE '%" + partOfProducName.toLowerCase()+ "%' "+connector+" ";
            }
            query = query.substring(0, query.length()-4);
            System.out.println("La consulta quedo como: " + query);
        }
        return query;
    }

    private boolean instructionContainsSomeoneOfTheseWords(ArrayList<String> words){
        boolean contain = false;
        for (String word: words){
            if( this.instruction.contains(word) ){
                contain = true;
            }
        }
        return contain;
    }

    private String getPriceOrPorcentage(){

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
        instructionCopy = instructionCopy.replace(action, "");
        for (String word: innecesaryWordsForProductName) {
            instructionCopy = instructionCopy.replaceAll(word, "");
            instructionCopy = instructionCopy.replace(word, "");
            System.out.println("Palabra a cambiar: "+word+", Resultado:"+instructionCopy);
        }

        ArrayList<String> partsOfTheProductName = new ArrayList<>(Arrays.asList(instructionCopy.split(" ")));
        partsOfTheProductName.remove("");
        return partsOfTheProductName;
    }
}
