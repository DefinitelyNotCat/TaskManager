package com.example.TaskManager.Service;

import com.example.TaskManager.DataModels.TaskMessage;
import com.example.TaskManager.Repositories.TaskMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskMessageService {
    private final TaskMessageRepository taskMessageRepository;

    @Autowired
    public TaskMessageService(TaskMessageRepository taskMessageRepository) {
        this.taskMessageRepository = taskMessageRepository;
    }

    public List<Object[]> getEmployeeEffectiveness(Long id) {
        return taskMessageRepository.getEmployeeEffectiveness(id);
    }

    public List<TaskMessage> getTaskMessage() {
        return taskMessageRepository.findAll();
    }

    public List<TaskMessage> getTaskMessageByTaskId(Long id) {
        return taskMessageRepository.findTaskMessagesByTaskId(id);
    }

    public void addNewTaskMessage(TaskMessage taskMessage) {
        taskMessageRepository.save(taskMessage);
    }

    public TaskMessage getTaskMessageById(Long id) {
        return taskMessageRepository.findTaskMessageById(id);
    }
}
