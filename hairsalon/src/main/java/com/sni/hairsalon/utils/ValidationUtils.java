package com.sni.hairsalon.utils;

import java.util.regex.Pattern;

public class ValidationUtils {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-z0-9+_.-]+@(.+)");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[\\d{9}]");

    public static boolean isAlphaWithSpaces(String str){
        if(str == null || str.isEmpty()){
            return false;
        }
        return str.matches("[a-zA-Z\\s]+$");
    }

    public static boolean isValidPhone(String phone){
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isValidEmail(String email){
        return email != null && EMAIL_PATTERN.matcher(email).matches();
        }

    public static boolean isLetter(String str){
        for(Character c : str.toCharArray()){
            if(!Character.isAlphabetic(c)){
                return false;
            }
        }

        return true;
    }

    public static boolean isNumber(String number){
        if(number ==null || number.isEmpty()){
            return false;
        }
        for(Character c : number.toCharArray()){
            if(!Character.isDigit(c)){
                return false;
            }
        }

        return true;
    }
}