package com.example.TaskManager.DataModels;

import javax.persistence.*;

@Entity(name = "Urgency")
@Table(name = "URGENCY")
public class Urgency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UR_ID", columnDefinition = "NUMBER(1,0)", nullable = false)
    private Integer id;
    @Column(name = "UR_NAME", columnDefinition = "VARCHAR2(60)", nullable = false)
    private String name;

    public Urgency() {
    }

    public Urgency(Integer id, String name) {
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
