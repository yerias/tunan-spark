package com.tunan.java.io.stream;

import com.tunan.java.io.util.TextFile;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * 统计不同字符出现的次数
 */
public class CharCounter {

    public static void main(String[] args) {

        String fileName = "C:/Users/Administrator/Desktop/a.txt";

        char[] chars = null;
        try {
            chars = TextFile.read(fileName).toCharArray();

            TreeSet<Character> charSet = new TreeSet<>();

            for (char c : chars) {
                charSet.add(c);
            }

            ArrayList<Character> charList = new ArrayList<>();
            for (char c : chars) {
                charList.add(c);
            }

            HashMap<Character, Integer> charMap = new HashMap<>();

            for (Character character : charSet) {
                int count = 0;
                for (Character c : charList) {
                    if(character.equals(c)){
                        count++;
                    }
                    charMap.put(character,count);
                }
            }

            System.out.println(charMap);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
