package com.puce.androidmed.models;

/**
 * Modelo que representa una medicina con todos sus atributos.
 */
public class Medicine {
    private String id;
    private String title;             // Nombre de la medicina
    private String description;       // Descripción de la medicina
    private String first_effects;      // Efectos primarios
    private String side_effects;       // Efectos secundarios
    private String image;             // Imagen de la medicina
    private String recommended_dose;  // Dosis recomendada

    // Getters y Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFirstEffects() {
        return first_effects;
    }

    public void setFirstEffects(String first_effects) {
        this.first_effects = first_effects;
    }

    public String getSideEffects() {
        return side_effects;
    }

    public void setSideEffects(String side_effects) {
        this.side_effects = side_effects;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRecommendedDose() {
        return recommended_dose;
    }

    public void setRecommendedDose(String recommended_dose) {
        this.recommended_dose = recommended_dose;
    }
}
