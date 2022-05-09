package com.example.TaskManager.Service;

import com.example.TaskManager.DataModels.Status;
import com.example.TaskManager.Repositories.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {
    private final StatusRepository statusRepository;

    @Autowired
    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public List<Status> getStatus() {
        return statusRepository.findAll();
    }

    public Status getStatusById(Integer id) {
        return statusRepository.getStatusById(id);
    }
}
