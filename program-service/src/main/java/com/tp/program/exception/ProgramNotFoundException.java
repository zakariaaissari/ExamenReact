package com.tp.program.exception;

public class ProgramNotFoundException extends RuntimeException {

    public ProgramNotFoundException(Long id) {
        super("Program not found with id: " + id);
    }

    public ProgramNotFoundException(String message) {
        super(message);
    }
}
