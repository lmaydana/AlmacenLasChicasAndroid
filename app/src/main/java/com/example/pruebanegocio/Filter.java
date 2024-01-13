package com.example.pruebanegocio;

import java.util.ArrayList;
import java.util.Arrays;

public class Filter {
    private String wordsToFilter;

    private ArrayList<String> wordsToDelete;
    public Filter(String wordsToFilter){
        wordsToDelete = new ArrayList<>();
        wordsToDelete.add("porciento");
        wordsToDelete.add("pesos");
        wordsToDelete.add("%");
        wordsToDelete.add("$");
        wordsToDelete.add("en");
        wordsToDelete.add("el");
        wordsToDelete.add("la");
        wordsToDelete.add("a");
        wordsToDelete.add("un");
        wordsToDelete.add("una");
        wordsToDelete.add("lo");
        wordsToDelete.add("de");
        wordsToDelete.add("por");
        wordsToDelete.add("x");
        wordsToDelete.add("*");
        wordsToDelete.add("g");
        wordsToDelete.add("gramos");
        wordsToDelete.add("mililitros");
        wordsToDelete.add("litro");
        wordsToDelete.add("litros");
        wordsToDelete.add("L");
        wordsToDelete.add("ML");
        wordsToDelete.add("kilogramos");
        wordsToDelete.add("KG");
        this.wordsToFilter = wordsToFilter;

    }

    public ArrayList<String> getFilteredWords() {
        ArrayList<String> filteredWords = new ArrayList<>(Arrays.asList(wordsToFilter.split(" ")));
        filteredWords.removeAll(filteredWords);
        return filteredWords;
    }

    public String[] getFilteredWordsInStringArray(){
        ArrayList<String> filteredWordsArrayList = this.getFilteredWords();
        String[] filteredWordsArray = new String[filteredWordsArrayList.size()];
        for (int i = 0; i < filteredWordsArrayList.size(); i++) {
            filteredWordsArray[i] = filteredWordsArrayList.get(i);
        }
        return filteredWordsArray;
    }

    public void addWordToFilter(String wordToFilter){
        wordsToDelete.add(wordToFilter);
    }
}
