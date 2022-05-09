package com.example.TaskManager.DataModels;

import javax.persistence.*;

@Entity(name = "Department")
@Table(name = "DEPARTMENT")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DP_ID", columnDefinition = "NUMBER(2,0)", nullable = false)
    private Integer id;
    @Column(name = "DP_NAME", columnDefinition = "VARCHAR2(150)", nullable = false)
    private String name;

    public Department() {
    }

    public Department(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Department(String name) {
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
