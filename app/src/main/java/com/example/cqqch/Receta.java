package com.example.cqqch;

public class Receta {
    private String name;
    private String category;
    private String ingredients;
    private int preparationTime; // Tiempo en minutos
    private double rating; // Puntuación
    private String price; // Precio medio
    private String description; // Descripción de la receta
    private boolean isFavorite; // Si es favorita

    public Receta(String name, String category, String ingredients, int preparationTime, double rating, String price, String description, boolean isFavorite) {
        this.name = name;
        this.category = category;
        this.ingredients = ingredients;
        this.preparationTime = preparationTime;
        this.rating = rating;
        this.price = price;
        this.description = description;
        this.isFavorite = isFavorite;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getIngredients() {
        return ingredients;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public double getRating() {
        return rating;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
