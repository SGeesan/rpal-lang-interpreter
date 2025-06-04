package com.rpal.cse;

/*
 * Class to generate Exceptions that happen during CSE evaluation phase
 */
public class CSE_Exception extends RuntimeException {
    
    public CSE_Exception (String message) {
        super(message);
    }
}
