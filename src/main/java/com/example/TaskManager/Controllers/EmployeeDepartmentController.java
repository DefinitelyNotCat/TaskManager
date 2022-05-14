package com.example.TaskManager.Controllers;

import com.example.TaskManager.DataModels.Employee;
import com.example.TaskManager.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.*;

@Controller
public class EmployeeDepartmentController {
    private final TaskService taskService;
    private final UrgencyService urgencyService;
    private final StatusService statusService;
    private final DepartmentService departmentService;
    private final EmployeeService employeeService;
    private final TaskMessageService taskMessageService;
    private final FileStorageService fileStorageService;
    private final TaskViewLogService taskViewLogService;
    private final FavoritesService favoritesService;
    private final MyErrorController myErrorController;
    private final UserAccessService userAccessService;

    @Autowired
    public EmployeeDepartmentController(TaskService taskService, UrgencyService urgencyService, StatusService statusService, DepartmentService departmentService,
                                        EmployeeService employeeService, TaskMessageService taskMessageService, FileStorageService fileStorageService,
                                        TaskViewLogService taskViewLogService, FavoritesService favoritesService, MyErrorController myErrorController,
                                        UserAccessService userAccessService) {
        this.taskService = taskService;
        this.urgencyService = urgencyService;
        this.statusService = statusService;
        this.departmentService = departmentService;
        this.employeeService = employeeService;
        this.taskMessageService = taskMessageService;
        this.fileStorageService = fileStorageService;
        this.taskViewLogService = taskViewLogService;
        this.favoritesService = favoritesService;
        this.myErrorController = myErrorController;
        this.userAccessService = userAccessService;
    }


    //Добавить сотрудника
    @GetMapping(path = "/newEmployee")
    public String newEmployee(@RequestParam(value = "message", defaultValue = "") String message, Model model, Employee employee) {
        if (!employeeService.getCurrentEmployee().getUserAccess().getId().equals(3)) {
            return myErrorController.handleError(null, model);
        }

        model.addAttribute("message", message);
        model.addAttribute("user", employeeService.getCurrentEmployee());
        model.addAttribute("userAccess", userAccessService.getUserAccess());
        model.addAttribute("department", departmentService.getDepartment());

        return "addEmployee";
    }

    //Добавить сотрудника
    @PostMapping(path = "/newEmployee")
    public ModelAndView addNewEmployee(@Valid Employee employee, RedirectAttributes redirectAttributes) {

        if (employeeService.loadUserByUsername(employee.getLogin()) != null || employeeService.getEmployeeByEmail(employee.getEmail()) != null) {
            if (employeeService.loadUserByUsername(employee.getLogin()) != null)
                redirectAttributes.addAttribute("message", "Логин занят");
            else
                redirectAttributes.addAttribute("message", "Почта занята");
            return new ModelAndView("redirect:/newEmployee/");
        }

        employee.setIsActive('Y');
        employee.setNotification('N');
        employeeService.saveEmployee(employee);
        redirectAttributes.addAttribute("message", "Сотрудник добавлен");

        return new ModelAndView("redirect:/newEmployee/");
    }

    //Мой отдел
    @GetMapping(path = "/myDepartment")
    public ModelAndView myDepartment() {
        return new ModelAndView("redirect:/department/" + employeeService.getEmployeeById(employeeService.getCurrentEmployee().getId()).getDepartment().getId());
    }

    //Отдел сотрудника
    @GetMapping(path = "/department/{id}")
    public String department(@PathVariable("id") Integer id, Model model) {
        Employee user = employeeService.getCurrentEmployee();
        Map<String, Integer> graphData = new TreeMap<>();

        switch (user.getUserAccess().getId()) {
            case 1:
            case 2:
                if (!user.getDepartment().getId().equals(id))
                    return myErrorController.handleError(null, model);
                break;
        }

        for (Employee e : employeeService.getEmployeeByDepartment(departmentService.getDepartmentById(id))) {
            graphData.put(e.getFullName(), taskService.getTaskByResponsible(employeeService.getEmployeeById(e.getId())).size());
        }

        model.addAttribute("chartData", graphData);
        model.addAttribute("department", departmentService.getDepartmentById(id));
        model.addAttribute("user", employeeService.getCurrentEmployee());
        model.addAttribute("unallocatedTasks", taskService.getUnallocatedTasksByDepartment(id));
        model.addAttribute("deadlineTasks", taskService.getDeadlineTasksByDepartment(id));
        model.addAttribute("employees", employeeService.getEmployeeByDepartment(departmentService.getDepartmentById(id)));

        return "department";
    }

    //Включить\отключить уведомления на почту
    @PostMapping(path = "/Profile/toggleNotification")
    @ResponseBody
    public void postResponseController(@RequestParam(value = "employeeId") Long employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        employee.setNotification(employee.getNotificationStatus() ? 'N' : 'Y');
        employeeService.saveEmployee(employee);
    }

    //Изменить пароль
    @PostMapping(path = "/Profile/{id}/changePassword")
    @ResponseBody
    public ModelAndView changePassword(@PathVariable("id") Long id, @RequestParam(value = "password") String newPassword) {
        Employee employee = employeeService.getEmployeeById(id);
        employee.setPassword(newPassword);
        employeeService.saveEmployee(employee);
        return new ModelAndView("redirect:/Profile/" + id);
    }

    //Все отделы
    @GetMapping(path = "/allDepartments")
    public String allDepartments(Model model) {
        model.addAttribute("user", employeeService.getCurrentEmployee());
        model.addAttribute("departments", departmentService.getDepartment());
        return "departments";
    }

    //Все сотрудники
    @GetMapping(path = "/allEmployees")
    public String allEmployees(Model model) {
        model.addAttribute("user", employeeService.getCurrentEmployee());
        model.addAttribute("employees", employeeService.getAll());
        return "employees";
    }

    //Мой профиль
    @GetMapping(path = "/myProfile")
    public ModelAndView myProfile() {
        return new ModelAndView("redirect:/Profile/" + employeeService.getCurrentEmployee().getId());
    }

    //Профиль сотрудника
    @GetMapping(path = "/Profile/{id}")
    public String profile(@PathVariable("id") Long id, Model model) {
        Employee user = employeeService.getCurrentEmployee();

        switch (user.getUserAccess().getId()) {
            case 1:
                if (!user.getId().equals(id))
                    return myErrorController.handleError(null, model);
                break;
            case 2:
                if (!user.getId().equals(id) && !employeeService.getEmployeeById(id).getDepartment().getId().equals(user.getDepartment().getId()))
                    return myErrorController.handleError(null, model);
                break;
        }

        Map<String, Integer> graphData = new TreeMap<>();
        List<Object[]> list = taskMessageService.getEmployeeEffectiveness(id);

        for (Object[] l : list) {
            if (l[1] == null) {
                l[1] = 0;
            }
            graphData.put(l[0].toString().substring(0, 11), Integer.valueOf(l[1].toString()));
        }

        model.addAttribute("chartData", graphData);
        model.addAttribute("employee", employeeService.getEmployeeById(id));
        model.addAttribute("user", employeeService.getCurrentEmployee());

        return "employeeProfile";
    }
}
