package org.example;

import java.io.*;
import java.util.*;
import java.io.IOException;


public class Main {

    public static ArrayList<Character> alphabetGetter(ArrayList<String> fsm) {
        ArrayList<Character> result = new ArrayList<>();
        Character c;
        for (int i = 4; i < fsm.size(); i++) {
            c = fsm.get(i).charAt(2);
            if (!result.contains(c)) result.add(c);
        }
        return result;
    }

    public static void getAllWords(ArrayList<String> result, ArrayList<Character> alphabet, String base, int length) {
        String current;
        for (int i = 0; i < alphabet.size(); i++) {
            char c = alphabet.get(i);
            if (length == 1) {
                result.add(base + c);
            } else {
                getAllWords(result, alphabet, base + c, length - 1);
            }
        }
    }

    public static ArrayList<Character> statesGetter(ArrayList<String> fsm) {
        ArrayList<Character> result = new ArrayList<>();
        Character c;
        for (int i = 4; i < fsm.size(); i++) {
            c = fsm.get(i).charAt(0);
            if (!result.contains(c)) result.add(c);
            c = fsm.get(i).charAt(4);
            if (!result.contains(c)) result.add(c);
        }
        return result;
    }

    public static ArrayList<Character> finalStatesGetter(ArrayList<String> fsm) {
        ArrayList<Character> result = new ArrayList<>();
        Character c;
        for (int i = 2; i < fsm.get(3).length(); i += 2) {
            c = fsm.get(3).charAt(i);
            if (!result.contains(c)) result.add(c);
        }
        return result;
    }

    public static boolean checkerFSM(ArrayList<String> fsm, int number) {
        ArrayList<Character> alphabet = alphabetGetter(fsm);
        ArrayList<String> words = new ArrayList<>();
        getAllWords(words, alphabet, "", number);
        ArrayList<Character> states = statesGetter(fsm);
        ArrayList<Character> fstates = finalStatesGetter(fsm);
        Character startState;
        Character startState1 = fsm.get(2).charAt(0);
        int count = 0;

        for (int i = 0; i < words.size(); i++) {
            startState = startState1;
            for (int j = 0; j < words.get(i).length(); j++) {
                for (int k = 4; k < fsm.size(); k++){
                    if (startState == fsm.get(k).charAt(0) && words.get(i).charAt(j) == fsm.get(k).charAt(2)) {
                        startState = fsm.get(k).charAt(4);
                        if(fstates.contains(startState) && j + 1 == words.get(i).length()){
                            count ++;
                        }
                        break;
                    }
                }
            }
        }
        if(count == words.size()) return true;
        else return false;
    }

    public static void main(String[] args/*, int k*/) throws IOException {
        //read file
        BufferedReader reader = new BufferedReader(new FileReader("E://IdeaProjects//fsm_lab1//src//main//resources//fsm.txt"));
        String line = reader.readLine();
        ArrayList<String> fsm = new ArrayList<>();
        while (line != null) {
            fsm.add(line);
            line = reader.readLine();
        }
        reader.close();

        ArrayList<Character> alphabet = alphabetGetter(fsm);
        char[] alph = new char[alphabet.size()];
        for(int i = 0; i < alphabet.size(); i++){
            char c = alphabet.get(i);
            alph[i] = c;
        }
        System.out.println("Setted alphabet: " + Arrays.toString(alph));


        System.out.println("Enter number for words lenght: ");
        BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in));
        String number = reader1.readLine();
        //k = Integer.parseInt(number);
        int k = Integer.parseInt(number);

        ArrayList<String> words = new ArrayList<>();
        getAllWords(words,alphabet, "", k);
        System.out.println("All possible words: " + Arrays.toString(words.toArray()));

        boolean res = checkerFSM(fsm, k);
        if(res == true) System.out.println("Setted FSM can read all words out of alphabet.");
        else System.out.println("Setted FSM can NOT read all words out of alphabet.");
    }
}