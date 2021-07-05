package com.tunan.java.io.stream;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * 统计不同字节出现的次数
 */
public class ByteCounter19 {

    static String filename = "tunan-java/data/word.txt";

    public static void main(String[] args) {

        try {
            byte[] read = BinaryFile.read(filename);
            HashSet<Byte> hashSet = new HashSet<>();
            for (byte b : read) {
                hashSet.add(b);
            }

            ArrayList<Byte> arrayList = new ArrayList<>();
            for (byte b : read) {
                arrayList.add(b);
            }

            HashMap<Byte, Integer> hashMap = new HashMap<>();
            for (Byte aByte : hashSet) {
                int count = 0;
                for (Byte bByte : arrayList) {
                    if(aByte.equals(bByte)){
                        count++;
                    }
                }

                hashMap.put(aByte,count);
            }
            System.out.println(hashMap);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
