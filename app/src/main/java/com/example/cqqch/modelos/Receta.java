package com.example.cqqch.modelos;

import android.os.Parcel;
import android.os.Parcelable;

public class Receta implements Parcelable {

    private String id;          // ID único de la receta
    private String name;
    private String category;
    private String ingredients;
    private int preparationTime; // Tiempo en minutos
    private double rating;       // Puntuación
    private String price;        // Precio medio
    private String description;  // Descripción de la receta
    private boolean isFavorite;  // Si es favorita

    public Receta() {
        // Constructor vacío requerido por Firebase
    }

    // Constructor completo con ID
    public Receta(String id, String name, String category, String ingredients, int preparationTime,
                  double rating, String price, String description, boolean isFavorite) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.ingredients = ingredients;
        this.preparationTime = preparationTime;
        this.rating = rating;
        this.price = price;
        this.description = description;
        this.isFavorite = isFavorite;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFavorite() {
        return isFavorite;
    }
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    // Parcelable
    protected Receta(Parcel in) {
        id = in.readString();
        name = in.readString();
        category = in.readString();
        ingredients = in.readString();
        preparationTime = in.readInt();
        rating = in.readDouble();
        price = in.readString();
        description = in.readString();
        isFavorite = in.readByte() != 0;
    }

    public static final Creator<Receta> CREATOR = new Creator<Receta>() {
        @Override
        public Receta createFromParcel(Parcel in) {
            return new Receta(in);
        }

        @Override
        public Receta[] newArray(int size) {
            return new Receta[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(category);
        parcel.writeString(ingredients);
        parcel.writeInt(preparationTime);
        parcel.writeDouble(rating);
        parcel.writeString(price);
        parcel.writeString(description);
        parcel.writeByte((byte) (isFavorite ? 1 : 0));
    }
}
