package com.example.TaskManager.DataModels;

import javax.persistence.*;

@Entity(name = "Files")
@Table(name = "FILES")
public class Files {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_ID", columnDefinition = "NUMBER()", nullable = false)
    private Long id;
    @Column(name = "F_NAME", columnDefinition = "VARCHAR2(120)", nullable = false)
    private String name;
    @Column(name = "F_TYPE", columnDefinition = "VARCHAR2(300)", nullable = false)
    private String type;

    @Lob
    @Column(name = "F_DATA", columnDefinition = "BLOB", nullable = false)
    private byte[] data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_TM_ID")
    private TaskMessage taskMessage;

    public Files() {
    }

    public Files(Long id, String name, String type, byte[] data, TaskMessage taskMessage) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.data = data;
        this.taskMessage = taskMessage;
    }

    public Files(String name, String type, byte[] data, TaskMessage taskMessage) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.taskMessage = taskMessage;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public TaskMessage getTaskMessage() {
        return taskMessage;
    }

    public void setTaskMessage(TaskMessage taskMessage) {
        this.taskMessage = taskMessage;
    }
}
