package com.sni.hairsalon.generator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class GeneratorOfId implements IdentifierGenerator{
    
   private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmssSSS");
   private static final int RANDOM_BOUND = 10000; // Adjust as needed
   private static final AtomicInteger SEQUENCE = new AtomicInteger(0);
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        return generateId();
    }

    private long generateId() {
   // Get the current timestamp
   LocalDateTime now = LocalDateTime.now();
   String timestamp = now.format(FORMATTER);
   long timestampValue = Long.parseLong(timestamp);
   
   // Generate a random number
   int randomNumber = ThreadLocalRandom.current().nextInt(RANDOM_BOUND);
   
   // Add a sequence counter that resets after reaching RANDOM_BOUND
   int sequence = SEQUENCE.getAndUpdate(current -> (current + 1) % RANDOM_BOUND);
   
   // Combine timestamp, random number, and sequence
   return timestampValue * RANDOM_BOUND + randomNumber + sequence;
    }
}
