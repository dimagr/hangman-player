package com.netomedia.exam.hangman.player;

import com.netomedia.exam.hangman.model.CharsMap;
import com.netomedia.exam.hangman.model.ServerResponse;
import com.netomedia.exam.hangman.server.HangmanServer;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HangmanPlayer {

    private static HangmanServer server = new HangmanServer();

    /**
     * This is the entry point of your Hangman Player
     * To start a new game call server.startNewGame()
     */
    public static void main(String[] args) throws Exception {
        ServerResponse serverResponse = server.startNewGame();

        BufferedReader br = prepareBufferedReader();
        List<String> words = extractWordByLength(serverResponse, br);

        while(!serverResponse.isGameEnded() && serverResponse.getHangman().contains("_")){

            prepareCounterMap(words);

            Character mostCommon = CharsMap.extractMostCommon();

            serverResponse = server.guess(serverResponse.getToken(), String.valueOf(mostCommon));

            CharsMap.addToEliminated(mostCommon);

            if(serverResponse.isCorrect()){
                words = newWordList(words, serverResponse.getHangman());
            }
            CharsMap.clearMap();

        }
        System.out.println(serverResponse.getHangman());

    }

    @NotNull
    private static BufferedReader prepareBufferedReader(){
        InputStream is = HangmanPlayer.class.getClassLoader().getResourceAsStream("dictionary.txt");
        InputStreamReader dd = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(dd);
        return br;
    }

    private static void prepareCounterMap(List<String> words){
        for(String word : words){
            List<Character> charactersInWord = new ArrayList<>();
            for(int i = 0; i < word.length(); i++){
                if(charactersInWord.contains(word.charAt(i)) || CharsMap.isEliminatedMapContains(word.charAt(i))){
                    continue;
                }
                CharsMap.addCharCounterToMap(word.charAt(i));
                charactersInWord.add(word.charAt(i));
            }
        }
    }

    private static List<String> newWordList(List<String> words, String hangman){
        List<String> newWords = new ArrayList<>();
        for(String word : words){
            boolean flag = true;
            for(int i = 0; i < word.length(); i ++){
                if(!Character.isLetter(hangman.charAt(i))){
                    continue;
                }
                if(hangman.charAt(i) != word.charAt(i)){
                    flag = false;
                    break;
                }
            }
            if(flag){
                newWords.add(word);
            }
        }
        return newWords;
    }

    private static void addToCorrectArray(ServerResponse serverResponse, Character[] correctWord){
        if(serverResponse.isCorrect()){
            for(int i = 0; i < serverResponse.getHangman().length(); i++){
                if(Character.isLetter(serverResponse.getHangman().charAt(i))){
                    correctWord[i] = serverResponse.getHangman().charAt(i);
                }
            }
        }
    }

    @org.jetbrains.annotations.NotNull
    private static List<String> extractWordByLength(ServerResponse serverResponse, BufferedReader br) throws IOException{
        List<String> words = new ArrayList<>();
        String word;
        while ((word = br.readLine()) != null) {

            if(word.length() == serverResponse.getHangman().length()){
                words.add(word);
            }
        }
        return words;
    }
}
