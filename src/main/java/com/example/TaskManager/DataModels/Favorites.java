package com.example.TaskManager.DataModels;

import javax.persistence.*;

@Entity(name = "Favorites")
@Table(name = "FAVORITES")
public class Favorites {
    @EmbeddedId
    private FavoritesCompositePK favoritesId;

    public Favorites() {
    }

    public Favorites(FavoritesCompositePK favoritesId) {
        this.favoritesId = favoritesId;
    }

    public FavoritesCompositePK getFavoritesId() {
        return favoritesId;
    }

    public void setFavoritesId(FavoritesCompositePK favoritesId) {
        this.favoritesId = favoritesId;
    }
}

