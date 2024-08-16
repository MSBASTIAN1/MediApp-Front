package com.puce.androidmed.models;

public class Recommendation {
    private String description;
    private String user_id;
    private String email;

    // Constructor
    public Recommendation(String description, String user_id, String email) {
        this.description = description;
        this.user_id = user_id;
        this.email = email;
    }

    // Getters y setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
