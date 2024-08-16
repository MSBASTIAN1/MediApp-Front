package com.puce.androidmed.models;

public class AuthenticationResponse {
    private String message;
    private User user; // Objeto User que contiene los datos del usuario autenticado

    // Getters y setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() { // Getter para obtener el usuario autenticado
        return user;
    }

    public void setUser(User user) { // Setter para establecer el usuario autenticado
        this.user = user;
    }

    public boolean isSuccess() {
        return false;
    }

    // Clase interna User para mapear los datos del usuario
    public static class User {
        private String id;
        private String first_name;
        private String last_name;
        private String email;
        private String phone_number;

        // Getters y setters para los campos de User
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }
    }
}
