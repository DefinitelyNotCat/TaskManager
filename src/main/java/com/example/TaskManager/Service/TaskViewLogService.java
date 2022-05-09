package com.example.TaskManager.Service;

import com.example.TaskManager.Repositories.TaskViewLogRepository;
import com.example.TaskManager.DataModels.TaskViewLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskViewLogService {
    private final TaskViewLogRepository taskViewLogRepository;

    @Autowired
    public TaskViewLogService(TaskViewLogRepository taskViewLogRepository) {
        this.taskViewLogRepository = taskViewLogRepository;
    }

    public List<TaskViewLog> getTaskViewLogByTaskId(Long id) {
        return taskViewLogRepository.getTaskViewLogByTaskId(id);
    }

    public void save(TaskViewLog taskViewLog) {
        taskViewLogRepository.save(taskViewLog);
    }
}
