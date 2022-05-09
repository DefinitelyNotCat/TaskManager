package com.example.TaskManager.Configs;

import com.example.TaskManager.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final EmployeeService employeeService;
    private final SecurityConfiguration securityConfiguration;

    @Autowired
    public WebSecurityConfig(EmployeeService employeeService, SecurityConfiguration securityConfiguration) {
        this.employeeService = employeeService;
        this.securityConfiguration = securityConfiguration;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin().defaultSuccessUrl("/tasks/all", true)
                .and()
                .csrf().disable().cors()
                .and()
                .authorizeRequests()
                    .antMatchers("/styles/**").permitAll()
                    //.antMatchers("/newEmployee").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                .and()
                    .logout()
                    .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(employeeService)
                .passwordEncoder(securityConfiguration.getPasswordEncoder());
    }
}
