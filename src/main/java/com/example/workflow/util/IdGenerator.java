package com.example.workflow.util;


import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

public class IdGenerator {


    public static String generateId(){

        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 30;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        generatedString = generatedString.concat(String.valueOf(System.currentTimeMillis()));

        return generatedString;
    }


}
