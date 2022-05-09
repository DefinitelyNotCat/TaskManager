package com.example.TaskManager.Service;

import com.example.TaskManager.DataModels.Favorites;
import com.example.TaskManager.Repositories.FavoritesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoritesService {
    private final FavoritesRepository favoritesRepository;

    @Autowired
    public FavoritesService(FavoritesRepository favoritesRepository) {
        this.favoritesRepository = favoritesRepository;
    }

    public List<Favorites> getFavoritesByEmployee(Long id) {
        return favoritesRepository.findFavoritesByEmployee(id);
    }

    public Favorites getFavoritesByEmployeeAndTask(Long employeeId, Long taskId) {
        return favoritesRepository.findFavoritesByEmployeeAndTask(employeeId, taskId);
    }

    public void addToFavorites(Favorites favorites) {
        favoritesRepository.save(favorites);
    }

    public void removeFromFavorites(Favorites favorites) {
        favoritesRepository.delete(favorites);
    }
}
