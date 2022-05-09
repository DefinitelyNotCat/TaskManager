package com.example.TaskManager.Repositories;

import com.example.TaskManager.DataModels.Urgency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrgencyRepository extends JpaRepository<Urgency, Integer> {
    Urgency getUrgencyById(Integer id);
}
