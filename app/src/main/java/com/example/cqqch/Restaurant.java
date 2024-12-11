package com.example.cqqch;

public class Restaurant {
    private String name;
    private String address;
    private String category;
    private String price;
    private double rating; // Nota
    private String comment; // Comentario
    private boolean isFavorite;
    private boolean canGo; // Respuesta a "Se puede ir?"
    private boolean canOrder; // Respuesta a "Se puede pedir?"

    public Restaurant(String name, String address, String category, String price, double rating, String comment, boolean isFavorite, boolean canGo, boolean canOrder) {
        this.name = name;
        this.address = address;
        this.category = category;
        this.price = price;
        this.rating = rating;
        this.comment = comment;
        this.isFavorite = isFavorite;
        this.canGo = canGo;
        this.canOrder = canOrder;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }

    public double getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isCanGo() {
        return canGo;
    }

    public boolean isCanOrder() {
        return canOrder;
    }
}
