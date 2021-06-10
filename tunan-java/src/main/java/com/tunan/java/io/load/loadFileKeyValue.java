package com.tunan.java.io.load;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.function.Consumer;

public class loadFileKeyValue {

    public static void main(String[] args) {

        Properties prop = new Properties();

        HashMap<String, String> map = new HashMap<>();

        try {
            prop.load(new FileReader("tunan-java/conf/properties.txt"));
            prop.stringPropertyNames().forEach(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    map.put(s,prop.getProperty(s));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterator<String> iterator = map.keySet().iterator();
        while(iterator.hasNext()){
            String key = iterator.next();
            System.out.println(key+" "+map.get(key));
        }


    }
}
