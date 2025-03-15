package com.sni.hairsalon.generator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class GeneratorOfId implements IdentifierGenerator{
    
   private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmssSSS");
    private static final int RANDOM_BOUND = 1000; // Adjust as needed

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        return generateId();
    }

    private long generateId() {
        // Get the current timestamp
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(FORMATTER);
        long timestampValue = Long.parseLong(timestamp);

        // Generate a random number using a thread-safe random generator
        int randomNumber = ThreadLocalRandom.current().nextInt(RANDOM_BOUND);

        // Combine timestamp and random number
        return timestampValue * RANDOM_BOUND + randomNumber;
    }
}
