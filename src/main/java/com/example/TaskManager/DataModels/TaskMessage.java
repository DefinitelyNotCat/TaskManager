package com.example.TaskManager.DataModels;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity(name = "TaskMessages")
@Table(name = "TASK_MESSAGES")
public class TaskMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TM_ID", columnDefinition = "NUMBER", nullable = false)
    private Long id;
    @Column(name = "TM_MESSAGE", columnDefinition = "VARCHAR2(2000)", nullable = false)
    private String message;
    @Column(name = "TM_ACTION", columnDefinition = "VARCHAR2(600)", nullable = false)
    private String action;
    @Column(name = "TM_DATE", columnDefinition = "DATE", nullable = false)
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TM_TK_ID")
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TM_EME_ID")
    private Employee employee;

    @OneToMany(targetEntity= Files.class, mappedBy="taskMessage",cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Files> filesSet;

    public TaskMessage() {
    }

    public TaskMessage(Long id, String message, String action, Date date, Task task, Employee employee, Set<Files> filesSet) {
        this.id = id;
        this.message = message;
        this.action = action;
        this.date = date;
        this.task = task;
        this.employee = employee;
        this.filesSet = filesSet;
    }

    public TaskMessage(String message, String action, Date date, Task task, Employee employee, Set<Files> filesSet) {
        this.message = message;
        this.action = action;
        this.date = date;
        this.task = task;
        this.employee = employee;
        this.filesSet = filesSet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    public Set<Files> getFiles() {
        return filesSet;
    }

    public void setFiles(Set<Files> filesSet) {
        this.filesSet = filesSet;
    }

}
