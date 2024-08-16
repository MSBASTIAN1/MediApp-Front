package com.puce.androidmed.models;

public class Reminder {
    private String id;
    private String description;
    private String user_id;
    private String medicament_id;
    private String date;
    private String time;
    private String frequency;
    private String quantity;
    private String status;

    // Constructor


    public Reminder(String id, String description, String user_id, String medicament_id, String date, String time, String frequency, String quantity, String status) {
        this.id = id;
        this.description = description;
        this.user_id = user_id;
        this.medicament_id = medicament_id;
        this.date = date;
        this.time = time;
        this.frequency = frequency;
        this.quantity = quantity;
        this.status = status;
    }

    // Getters y setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMedicament_id() {
        return medicament_id;
    }

    public void setMedicament_id(String medicament_id) {
        this.medicament_id = medicament_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
