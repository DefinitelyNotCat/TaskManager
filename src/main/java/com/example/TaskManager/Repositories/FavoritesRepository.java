package com.example.TaskManager.Repositories;

import com.example.TaskManager.DataModels.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorites, Long> {
    @Query(value = "SELECT F_EME_ID, F_TK_ID FROM FAVORITES f LEFT JOIN TASK t ON F.F_TK_ID = T.TK_ID WHERE F_EME_ID = ?1 ORDER BY T.TK_LAST_ANSWER DESC", nativeQuery = true)
    List<Favorites> findFavoritesByEmployee(Long id);

    @Query(value = "SELECT F_EME_ID, F_TK_ID FROM FAVORITES f LEFT JOIN TASK t ON F.F_TK_ID = T.TK_ID WHERE F_EME_ID = ?1 AND F_TK_ID = ?2 ORDER BY T.TK_LAST_ANSWER DESC", nativeQuery = true)
    Favorites findFavoritesByEmployeeAndTask(Long employeeId, Long taskId);
}
