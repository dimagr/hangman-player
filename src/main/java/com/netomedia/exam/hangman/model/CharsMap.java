package com.netomedia.exam.hangman.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CharsMap {

    private static Map<Character,Integer> eliminatedMap = new HashMap<>();

    private static Map<Character, Integer> map = new HashMap<>();

    public static Map<Character, Integer> getMap(){
        return map;
    }

    public static void addCharCounterToMap(Character character){
        if(!map.containsKey(character)){
            map.put(character,1);
        }else{
            Integer integer = map.get(character);
            map.put(character,integer+1);
        }
    }

    public static Character extractMostCommon(){
        int maxValueInMap = (Collections.max(map.values()));  // This will return max value in the Hashmap
        for(Map.Entry<Character, Integer> entry : map.entrySet()){
            if(entry.getValue() == maxValueInMap){
                return entry.getKey();
            }
        }
        return null;
    }

    public static void addToEliminated(Character character){
        if(!eliminatedMap.containsKey(character)){
            eliminatedMap.put(character,1);
        }
    }

    public static boolean isEliminatedMapContains(Character character){
        return eliminatedMap.containsKey(character);
    }

    public static void clearMap(){
        map.clear();
    }

}
