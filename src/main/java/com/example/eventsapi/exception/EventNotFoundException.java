package com.example.eventsapi.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String message) {
        super(message);
    }

    public EventNotFoundException(Long id) {
        super("Evento com ID " + id + " não encontrado");
    }
}