package com.example.TaskManager.Repositories;

import com.example.TaskManager.DataModels.Files;
import com.example.TaskManager.DataModels.TaskMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileDBRepository extends JpaRepository<Files, String> {
    List<Files> findAllByTaskMessage(TaskMessage taskMessage);
    Files findById(Long id);
}
