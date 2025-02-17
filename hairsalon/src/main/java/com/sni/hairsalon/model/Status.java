package com.sni.hairsalon.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum Status{
    PENDING(1),
    REQUESTED(2),
    CONFIRMED(3),
    CHECK_IN(4),
    IN_PROGRESS(5),
    COMPLETED(5),
    CANCELLED_BY_CLIENT(6),
    CANCELLED_BY_PROVIDER(7),
    RESCHEDULED(8),
    NO_SHOW(9);

    private int code;

    private static final Map<Integer, Status> STATUS_CODE_MAP = new HashMap<>();

    static{
        for(Status status : values()){
            STATUS_CODE_MAP.put(status.getCode(), status);
        }
    }

   private Status(int code){
       this.code = code;
    }

    public int getCode(){
        return code;
    }

    public static int fromEnum(Status status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        return status.getCode();
    }

    public static Status toEnum(int code) {
        Status status = STATUS_CODE_MAP.get(code);
        if (status == null) {
            throw new IllegalArgumentException("Invalid status code: " + code);
        }
        return status;
    }

    public List<Integer> getAllCode(){
        return  Arrays.stream(values())
                .map(Status::getCode)
                .collect(Collectors.toList());
    }

    public boolean isValidCode(int code){
        return STATUS_CODE_MAP.containsKey(code);
    }
} 