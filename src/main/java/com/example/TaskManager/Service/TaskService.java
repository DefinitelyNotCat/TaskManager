package com.example.TaskManager.Service;

import com.example.TaskManager.DataModels.Employee;
import com.example.TaskManager.DataModels.Task;
import com.example.TaskManager.Repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    public Task getTasksById(Long id) {
        return taskRepository.findTaskById(id);
    }

    public List<Task> getTasksByFilter(String id, String urgency, String status, String proponentDepartment, String responsibleDepartment) {
        return taskRepository.findByFilter(id, urgency, status, proponentDepartment, responsibleDepartment);
    }

    public Task getLatestTask() {
        return taskRepository.findLatestTask();
    }

    public List<Task> getUnallocatedTasksByDepartment(Integer id) {
        return taskRepository.findUnallocatedTasksByDepartment(id);
    }

    public List<Task> getDeadlineTasksByDepartment(Integer id) {
        return taskRepository.findDeadlineTasksByDepartment(id);
    }

    public List<Task> getTaskByResponsible(Employee employee) {
        return taskRepository.findTasksByResponsibleOrderByLastAnswerDesc(employee);
    }

    public List<Task> getTaskByProponent(Employee employee) {
        return taskRepository.findTasksByProponentOrderByLastAnswerDesc(employee);
    }

    public void addNewTask(Task task) {
        taskRepository.save(task);
    }

    public Page<Task> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    public void deleteTask(Long taskId) {
        boolean exists = taskRepository.existsById(taskId);
        if (!exists) {
            throw new IllegalStateException("task with id" + taskId + " does not exist");
        }
        taskRepository.deleteById(taskId);
    }

    @Transactional
    public void updateTaskLastAnswer(Long taskId, Date date) {
        Task task = taskRepository.findTaskById(taskId);
        task.setLastAnswer(date);
    }

    @Transactional
    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    @Transactional
    public void updateTask(Long taskId, String name) {
        Task task = taskRepository.findById(taskId).orElseThrow(() ->
                new IllegalStateException("task with id" + taskId + "does not exist"));

        if (name != null && name.length() > 0 && !Objects.equals(task.getName(), name)) {
            task.setName(name);
        }
    }
}
