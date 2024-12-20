package com.sni.hairsalon.utils;

import java.util.stream.IntStream;

public class ValidationUtils {
    
    public static boolean isAlphaWithSpaces(String str){
        if(str == null || str.isEmpty()){
            return false;
        }

        return str.matches("[a-zA-Z\\s]+$");
    }

 /*    public static boolean isAlphaUniCode(String str){
        if(str == null || str.isEmpty()){
            return false;
        }
        
        IntStream strValue = str.codePoints()
        .map(codePoint->Character.isLetter(codePoint));


        return true;
    }*/
}
