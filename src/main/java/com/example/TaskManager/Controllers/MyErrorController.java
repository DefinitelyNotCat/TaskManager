package com.example.TaskManager.Controllers;

import com.example.TaskManager.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class MyErrorController implements ErrorController {
    private final EmployeeService employeeService;

    @Autowired
    public MyErrorController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = null;
        model.addAttribute("user", employeeService.getCurrentEmployee());

        if (request != null)
            status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value() || statusCode == HttpStatus.BAD_REQUEST.value()) {
                model.addAttribute("header", "404");
                model.addAttribute("message", "Страница не найдена");
            } else {
                model.addAttribute("header", "Ошибка");
                model.addAttribute("message", "Что-то пошло не так");
            }
            return "error";
        }

        model.addAttribute("header", "403");
        model.addAttribute("message", "Отказано в доступе");
        return "error";
    }
}
