package com.example.TaskManager.Repositories;

import com.example.TaskManager.DataModels.UserAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccessRepository extends JpaRepository<UserAccess, Integer> {
    UserAccess findUserAccessById(Integer id);
}
