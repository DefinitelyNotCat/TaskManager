package com.example.TaskManager.Service;

import com.example.TaskManager.DataModels.Urgency;
import com.example.TaskManager.Repositories.UrgencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UrgencyService {

    private final UrgencyRepository urgencyRepository;

    @Autowired
    public UrgencyService(UrgencyRepository urgencyRepository) {
        this.urgencyRepository = urgencyRepository;
    }

    public List<Urgency> getUrgency() {
        return urgencyRepository.findAll();
    }

    public Urgency getUrgencyById(Integer id) {
        return urgencyRepository.getUrgencyById(id);
    }
}
