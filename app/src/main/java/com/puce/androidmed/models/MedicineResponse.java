package com.puce.androidmed.models;

import java.util.List;

public class MedicineResponse {
    private String message;
    private List<Medicine> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Medicine> getResult() {
        return result;
    }

    public void setResult(List<Medicine> result) {
        this.result = result;
    }
}
