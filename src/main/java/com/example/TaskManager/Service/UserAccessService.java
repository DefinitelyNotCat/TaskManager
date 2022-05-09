package com.example.TaskManager.Service;

import com.example.TaskManager.DataModels.UserAccess;
import com.example.TaskManager.Repositories.UserAccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAccessService {
    private final UserAccessRepository userAccessRepository;

    @Autowired
    public UserAccessService(UserAccessRepository userAccessRepository) {
        this.userAccessRepository = userAccessRepository;
    }

    public List<UserAccess> getUserAccess() {
        return userAccessRepository.findAll();
    }

    public UserAccess getUserAccessById(Integer id) {
        return userAccessRepository.findUserAccessById(id);
    }
}
