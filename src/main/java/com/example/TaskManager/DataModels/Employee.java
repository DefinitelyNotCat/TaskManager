package com.example.TaskManager.DataModels;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Entity(name = "Employee")
@Table(name = "EMPLOYEE")
public class Employee implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EME_ID", columnDefinition = "NUMBER()", nullable = false)
    private Long id;
    @Column(name = "EME_FULL_NAME", columnDefinition = "VARCHAR2(100)", nullable = false)
    private String fullName;
    @Column(name = "EME_IS_ACTIVE", columnDefinition = "VARCHAR2(1)", nullable = false)
    private char isActive;
    @Column(name = "EME_EMAIL", columnDefinition = "VARCHAR2(120)", nullable = false)
    private String email;
    @Column(name = "EME_EMAIL_NOTIFICATION", columnDefinition = "VARCHAR2(1)", nullable = false)
    private char notification;
    @Column(name = "EME_LOGIN", columnDefinition = "VARCHAR2(20)", nullable = false)
    private String login;
    @Column(name = "EME_PASSWORD", columnDefinition = "VARCHAR2(800)", nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EME_ACCESS_STATUS")
    private UserAccess userAccess;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EME_DEPARTMENT")
    private Department department;

    public Employee() {
    }

    public Employee(Long id, String fullName, char isActive, String email, char notification, String login, String password, UserAccess userAccess, Department department) {
        this.id = id;
        this.fullName = fullName;
        this.isActive = isActive;
        this.email = email;
        this.notification = notification;
        this.login = login;
        this.password = password;
        this.userAccess = userAccess;
        this.department = department;
    }

    public Employee(String fullName, char isActive, String email, char notification, String login, String password, UserAccess userAccess, Department department) {
        this.fullName = fullName;
        this.isActive = isActive;
        this.email = email;
        this.notification = notification;
        this.login = login;
        this.password = password;
        this.userAccess = userAccess;
        this.department = department;
    }

    public Employee(Long id, String fullName, Department department) {
        this.id = id;
        this.fullName = fullName;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public char getIsActive() {
        return isActive;
    }

    public boolean getActiveStatus() {
        if (Character.toUpperCase(isActive) == 'Y')
            return true;
        else
            return false;
    }

    public void setIsActive(char isActive) {
        this.isActive = isActive;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public char getNotification() {
        return notification;
    }

    public boolean getNotificationStatus() {
        if (Character.toUpperCase(notification) == 'Y')
            return true;
        else
            return false;
    }

    public void setNotification(char notification) {
        this.notification = notification;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if (Character.toUpperCase(isActive) == 'Y')
            return true;
        else
            return false;
    }

    public UserAccess getUserAccess() {
        return userAccess;
    }

    public void setUserAccess(UserAccess userAccess) {
        this.userAccess = userAccess;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
