package com.example.TaskManager.DataModels;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "Task")
@Table(name = "TASK")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TK_ID", columnDefinition = "NUMBER", nullable = false)
    private Long id;
    @Column(name = "TK_NAME", columnDefinition = "VARCHAR2(150)", nullable = false)
    private String name;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    @Column(name = "TK_DATE_OF_CREATION", columnDefinition = "DATE", nullable = false)
    private Date dateOfCreation;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    @Column(name = "TK_LAST_ANSWER", columnDefinition = "DATE", nullable = false)
    private Date lastAnswer;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "TK_DEADLINE", columnDefinition = "DATE", nullable = true)
    private Date deadline;
    @Column(name = "TK_CLOSED", columnDefinition = "VARCHAR2(1 BYTE)", nullable = false)
    private char closed;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TK_STATUS")
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TK_URGENCY")
    private Urgency urgency;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "TK_RESPONSIBLE")
    private Employee responsible;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "TK_PROPONENT")
    private Employee proponent;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "TK_PR_DEP")
    private Department proponentDepartment;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "TK_RES_DEP")
    private Department responsibleDepartment;

    public Task() {
    }

    public Task(Long id, String name, Date dateOfCreation, Date lastAnswer, Date deadline, char closed, Status status, Urgency urgency, Employee responsible, Employee proponent, Department proponentDepartment, Department responsibleDepartment) {
        this.id = id;
        this.name = name;
        this.dateOfCreation = dateOfCreation;
        this.lastAnswer = lastAnswer;
        this.deadline = deadline;
        this.closed = closed;
        this.status = status;
        this.urgency = urgency;
        this.responsible = responsible;
        this.proponent = proponent;
        this.proponentDepartment = proponentDepartment;
        this.responsibleDepartment = responsibleDepartment;
    }



    public Task(String name, Date dateOfCreation, Date lastAnswer, Date deadline, char closed, Status status, Urgency urgency, Employee responsible, Employee proponent, Department proponentDepartment, Department responsibleDepartment) {
        this.name = name;
        this.dateOfCreation = dateOfCreation;
        this.lastAnswer = lastAnswer;
        this.deadline = deadline;
        this.closed = closed;
        this.status = status;
        this.urgency = urgency;
        this.responsible = responsible;
        this.proponent = proponent;
        this.proponentDepartment = proponentDepartment;
        this.responsibleDepartment = responsibleDepartment;
    }

    public Task(String name, Date dateOfCreation, Date lastAnswer, Date deadline, char closed, Status status, Urgency urgency, Employee proponent, Department proponentDepartment, Department responsibleDepartment) {
        this.name = name;
        this.dateOfCreation = dateOfCreation;
        this.lastAnswer = lastAnswer;
        this.deadline = deadline;
        this.closed = closed;
        this.status = status;
        this.urgency = urgency;
        this.proponent = proponent;
        this.proponentDepartment = proponentDepartment;
        this.responsibleDepartment = responsibleDepartment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public Date getLastAnswer() {
        return lastAnswer;
    }

    public void setLastAnswer(Date lastAnswer) {
        this.lastAnswer = lastAnswer;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public boolean getClosed() {
        if (closed == 'N') {
            return false;
        }
        return true;
    }

    public void setClosed(char closed) {
        this.closed = closed;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Urgency getUrgency() {
        return urgency;
    }

    public void setUrgency(Urgency urgency) {
        this.urgency = urgency;
    }

    public Employee getResponsible() {
        return responsible;
    }

    public void setResponsible(Employee responsible) {
        this.responsible = responsible;
    }

    public Employee getProponent() {
        return proponent;
    }

    public void setProponent(Employee proponent) {
        this.proponent = proponent;
    }

    public Department getProponentDepartment() {
        return proponentDepartment;
    }

    public void setProponentDepartment(Department proponentDepartment) {
        this.proponentDepartment = proponentDepartment;
    }

    public Department getResponsibleDepartment() {
        return responsibleDepartment;
    }

    public void setResponsibleDepartment(Department responsibleDepartment) {
        this.responsibleDepartment = responsibleDepartment;
    }
}


