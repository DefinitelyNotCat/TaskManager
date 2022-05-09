package com.example.TaskManager.Repositories;

import com.example.TaskManager.DataModels.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {
    Status getStatusById(Integer id);
}
