package com.tunan.java.filter;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalDemo {

    public static void main(String[] args) {

        double a = 4.4;
        double b = 1.3;

        System.out.println(a/b);

        BigDecimal A = BigDecimal.valueOf(a);
        BigDecimal B = BigDecimal.valueOf(b);

        System.out.println(A.divide(B,2, RoundingMode.HALF_UP));

    }


}
