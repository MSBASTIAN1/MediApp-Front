package com.puce.androidmed.models;

import java.util.List;

public class ReminderResponse {

    private String message;
    private List<Reminder> result;

    // Constructor
    public ReminderResponse(String message, List<Reminder> result) {
        this.message = message;
        this.result = result;
    }

    // Getters y Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Reminder> getResult() {
        return result;
    }

    public void setResult(List<Reminder> result) {
        this.result = result;
    }
}
