package com.example.TaskManager.DataModels;

import javax.persistence.*;

@Entity(name = "Status")
@Table(name = "STATUS")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ST_ID", columnDefinition = "NUMBER(1,0)", nullable = false)
    private Integer id;
    @Column(name = "ST_NAME", columnDefinition = "VARCHAR2(60)", nullable = false)
    private String name;

    public Status() {
    }

    public Status(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


