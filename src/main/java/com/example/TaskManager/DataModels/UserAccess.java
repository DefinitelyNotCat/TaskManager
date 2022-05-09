package com.example.TaskManager.DataModels;

import javax.persistence.*;

@Entity(name = "UserAccess")
@Table(name = "USER_ACCESS")
public class UserAccess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UA_ID", columnDefinition = "NUMBER(2,0)", nullable = false)
    private Integer id;
    @Column(name = "UA_NAME", columnDefinition = "VARCHAR2(60)", nullable = false)
    private String name;

    public UserAccess() {
    }

    public UserAccess(Integer id, String name) {
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

