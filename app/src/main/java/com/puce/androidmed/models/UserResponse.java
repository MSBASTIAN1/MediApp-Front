package com.puce.androidmed.models;

import java.util.List;

/**
 * Modelo que representa la respuesta del servidor al registrar un usuario.
 */
public class UserResponse {
    private String message;

    private List<User> users;

    private List<Admin> admins;
    // Getter y Setter


    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Admin> admins) {
        this.admins = admins;
    }
}
