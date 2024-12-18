package com.sni.hairsalon.generator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class GeneratorOfId implements IdentifierGenerator{
    
    private long generationId(){
        LocalDateTime now; 
        long seed;

        now = LocalDateTime.now();  
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmssSSS");
        String nowString = now.format(formatter);
        long nowValue = Long.parseLong(nowString);  
        seed = System.currentTimeMillis();
        Random rand = new Random(seed);
        long randomNumber = rand.nextLong();

        while(randomNumber < 0){
            randomNumber = rand.nextLong();
        }
        return nowValue + randomNumber;
    }

    
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj){
        return generationId();
      
    } 
}
