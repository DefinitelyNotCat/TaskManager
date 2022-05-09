package com.example.TaskManager.DataModels;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "TaskViewLog")
@Table(name = "TASK_VIEW_LOG")
public class TaskViewLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TVL_ID", columnDefinition = "NUMBER", nullable = false)
    private Long id;
    @Column(name = "TVL_DATE", columnDefinition = "DATE", nullable = false)
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TVL_TK_ID")
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TVL_EME_ID")
    private Employee employee;

    public TaskViewLog() {
    }

    public TaskViewLog(Long id, Date date, Task task, Employee employee) {
        this.id = id;
        this.date = date;
        this.task = task;
        this.employee = employee;
    }

    public TaskViewLog(Date date, Task task, Employee employee) {
        this.date = date;
        this.task = task;
        this.employee = employee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
