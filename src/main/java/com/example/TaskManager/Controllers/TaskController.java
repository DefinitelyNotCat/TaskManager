package com.example.TaskManager.Controllers;

import com.example.TaskManager.DataModels.*;
import com.example.TaskManager.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/tasks")
public class TaskController {

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
    private final MailSender mailSender;

    @Autowired
    public TaskController(TaskService taskService, UrgencyService urgencyService, StatusService statusService, DepartmentService departmentService,
                          EmployeeService employeeService, TaskMessageService taskMessageService, FileStorageService fileStorageService,
                          TaskViewLogService taskViewLogService, FavoritesService favoritesService, MyErrorController myErrorController, MailSender mailSender) {
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
        this.mailSender = mailSender;
    }

    //Все задачи
    @GetMapping(path = "/all")
    public String tasksAll(@RequestParam(value = "taskId", defaultValue = "") String id,
                           @RequestParam(value = "urgency", defaultValue = "") String urgency,
                           @RequestParam(value = "status", defaultValue = "") String status,
                           @RequestParam(value = "proponentDepartment", defaultValue = "") String proponentDepartment,
                           @RequestParam(value = "responsibleDepartment", defaultValue = "") String responsibleDepartment,
                           Model model) {
        Employee user = employeeService.getCurrentEmployee();
        Integer urgencyFilter = 0;
        Integer statusFilter = 0;
        Integer proponentDepartmentFilter = 0;
        Integer responsibleDepartmentFilter = 0;
        List<Task> favorites = new ArrayList<>();

        if (!urgency.isEmpty())
            urgencyFilter = Integer.valueOf(urgency);
        if (!status.isEmpty())
            statusFilter = Integer.valueOf(status);
        if (!proponentDepartment.isEmpty())
            proponentDepartmentFilter = Integer.valueOf(proponentDepartment);
        if (!responsibleDepartment.isEmpty())
            responsibleDepartmentFilter = Integer.valueOf(responsibleDepartment);

        for (Favorites f : favoritesService.getFavoritesByEmployee(user.getId()))
            favorites.add(f.getFavoritesId().getTask());

        model.addAttribute("favorites", favorites);
        model.addAttribute("departments", departmentService.getDepartment());
        model.addAttribute("urgency", urgencyService.getUrgency());
        model.addAttribute("status", statusService.getStatus());
        model.addAttribute("tasks", taskService.getTasksByFilter(id, urgency, status, proponentDepartment, responsibleDepartment));

        model.addAttribute("urgencyFilter", urgencyFilter);
        model.addAttribute("statusFilter", statusFilter);
        model.addAttribute("proponentDepartmentFilter", proponentDepartmentFilter);
        model.addAttribute("responsibleDepartmentFilter", responsibleDepartmentFilter);
        model.addAttribute("taskId", id);
        model.addAttribute("user", employeeService.getCurrentEmployee());

        return "tasks";
    }

    //Мои задачи
    @GetMapping("/myTasks")
    public String myTasks(Model model) {
        Employee user = employeeService.getCurrentEmployee();
        List<Task> tasks = new ArrayList<>();

        for (Favorites f : favoritesService.getFavoritesByEmployee(user.getId())) {
            tasks.add(f.getFavoritesId().getTask());
        }

        model.addAttribute("myTasks", taskService.getTaskByResponsible(user));
        model.addAttribute("myFavorites", tasks);
        model.addAttribute("createdByMe", taskService.getTaskByProponent(user));
        model.addAttribute("user", employeeService.getCurrentEmployee());

        return "myTasks";
    }

    //Добавить задачу в избранное
    @PostMapping(path = "/toggleFavorites")
    @ResponseBody
    public void toggleFavorites(@RequestParam(value = "taskId") Long taskId) {
        Employee user = employeeService.getCurrentEmployee();
        List<Favorites> favoritesList = favoritesService.getFavoritesByEmployee(user.getId());
        Favorites favorites = new Favorites(new FavoritesCompositePK(user, taskService.getTasksById(taskId)));

        if (favoritesList.contains(favoritesService.getFavoritesByEmployeeAndTask(user.getId(), taskId))) {
            favoritesService.removeFromFavorites(favorites);
        } else {
            favoritesService.addToFavorites(favorites);
        }
    }

    //Сохранить файл в БД
    public void storeFile(MultipartFile file, TaskMessage taskMessage) {
        if (!file.isEmpty()) {
            try {
                fileStorageService.store(file, taskMessage);
            } catch (Exception ignored) {
            }
        }
    }

    //Отправить письмио с уведомдлением
    public void sendNotificationEmail(String responsible, Employee employee, Task task) {
        if (!responsible.isEmpty() && employeeService.getEmployeeById(Long.valueOf(responsible)).getNotificationStatus()
                && !employee.getId().equals(task.getResponsible().getId())) {
            String emailMessage = "Вы были назначены отсетственным за задачу #" + task.getId() + ";\n" +
                    "Назначил сотрудник: " + employee.getFullName() + ";\n" +
                    "Дедлайн: " + ((task.getDeadline() == null) ? "отсутствует" : new SimpleDateFormat("dd.MM.yyyy").format(task.getDeadline()) + ";");
            try {
                mailSender.send(employeeService.getEmployeeById(Long.valueOf(responsible)).getEmail(),
                        "Задача #" + task.getId().toString(), emailMessage);
            } catch (Exception ignored) {
            }
        }
    }

    //Задача
    @GetMapping(path = "/{id}")
    public String task(@PathVariable("id") Long id, Model model) {
        Employee user = employeeService.getCurrentEmployee();

        switch (user.getUserAccess().getId()) {
            case 1:
                if (!taskService.getTasksById(id).getProponent().getId().equals(user.getId()) && (taskService.getTasksById(id).getResponsible() == null || !taskService.getTasksById(id).getResponsible().getId().equals(user.getId())))
                    return myErrorController.handleError(null, model);
                break;
            case 2:
                if (!taskService.getTasksById(id).getProponent().getId().equals(user.getId()) && !taskService.getTasksById(id).getResponsibleDepartment().getId().equals(user.getDepartment().getId()))
                    return myErrorController.handleError(null, model);
                break;
        }

        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
        Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        Map<Integer, Map<Long, String>> depEmpMap = employeeService.mapDepartmentsAndEmployees();

        taskViewLogService.save(new TaskViewLog(out, taskService.getTasksById(id), employeeService.getEmployeeById(user.getId())));

        model.addAttribute("messages", taskMessageService.getTaskMessageByTaskId(id));
        model.addAttribute("task", taskService.getTasksById(id));
        model.addAttribute("departments", departmentService.getDepartment());
        model.addAttribute("urgency", urgencyService.getUrgency());
        model.addAttribute("status", statusService.getStatus());
        model.addAttribute("depEmpMap", depEmpMap);
        model.addAttribute("viewLog", taskViewLogService.getTaskViewLogByTaskId(id));
        model.addAttribute("user", employeeService.getCurrentEmployee());
        model.addAttribute("currentEmployees", employeeService.getEmployeeByDepartment(departmentService.getDepartmentById(taskService.getTasksById(id).getResponsibleDepartment().getId())));

        return "task";
    }

    //Добавить сообщение
    @PostMapping(path = "/{id}", params = "send")
    public ModelAndView registerNewMessage(@AuthenticationPrincipal Employee employee,
                                           @PathVariable Long id,
                                           @RequestParam(value = "message", defaultValue = "") String message,
                                           @RequestParam(value = "responsibleDepartment", defaultValue = "") String responsibleDepartment,
                                           @RequestParam(value = "responsible", defaultValue = "") String responsible,
                                           @RequestParam(value = "status", defaultValue = "") String status,
                                           @RequestParam(value = "deadline", defaultValue = "") String deadlineString,
                                           @RequestParam(name = "removeDeadline", defaultValue = "false") Boolean removeDeadline,
                                           @RequestParam(value = "urgency", defaultValue = "") String urgency,
                                           @RequestParam(value = "file") MultipartFile file) throws ParseException {
        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
        Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());

        Task task = taskService.getTasksById(id);
        String action = "";

        if (!Integer.valueOf(responsibleDepartment).equals(task.getResponsibleDepartment().getId())) {
            action += "Перенаправлено в: " + departmentService.getDepartmentById(Integer.valueOf(responsibleDepartment)).getName() + ";";
            task.setResponsibleDepartment(departmentService.getDepartmentById(Integer.valueOf(responsibleDepartment)));

            if (responsible.isEmpty()) {
                task.setResponsible(null);
            } else {
                action += "Назначен ответственным сотрудник: " + employeeService.getEmployeeById(Long.valueOf(responsible)).getFullName() + ";";
                task.setResponsible(employeeService.getEmployeeById(Long.valueOf(responsible)));
            }
        } else {
            if (responsible.isEmpty()) {
                task.setResponsible(null);
            } else if (task.getResponsible() == null || !Long.valueOf(responsible).equals(task.getResponsible().getId())) {
                action += "Назначен ответственным сотрудник: " + employeeService.getEmployeeById(Long.valueOf(responsible)).getFullName() + ";";
                task.setResponsible(employeeService.getEmployeeById(Long.valueOf(responsible)));
            }
        }

        if (!Integer.valueOf(status).equals(task.getStatus().getId())) {
            action += "Изменен статус: " + statusService.getStatusById(Integer.valueOf(status)).getName() + ";";
            task.setStatus(statusService.getStatusById(Integer.valueOf(status)));
        }

        if (!Integer.valueOf(urgency).equals(task.getUrgency().getId())) {
            action += "Изменена срочность: " + urgencyService.getUrgencyById(Integer.valueOf(urgency)).getName() + ";";
            task.setUrgency(urgencyService.getUrgencyById(Integer.valueOf(urgency)));
        }

        if (removeDeadline) {
            action += "Срок выполнения удален;";
            task.setDeadline(null);
        } else if (!deadlineString.isEmpty()) {
            Date deadline = new SimpleDateFormat("yyyy-MM-dd").parse(deadlineString);
            action += "Изменен срок выполнения: " + new SimpleDateFormat("dd.MM.yyyy").format(deadline) + ";";
            task.setDeadline(deadline);
        }

        TaskMessage taskMessage = new TaskMessage(message, action, out, task, employee, null);
        taskMessageService.addNewTaskMessage(taskMessage);
        taskService.updateTaskLastAnswer(id, out);
        storeFile(file, taskMessage);
        sendNotificationEmail(responsible, employee, task);

        return new ModelAndView("redirect:/tasks/" + id);
    }

    //Открыть\закрыть задачу
    @PostMapping(path = "/{id}", params = "toggle")
    public ModelAndView toggle(@PathVariable Long id) {
        Task task = taskService.getTasksById(id);
        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
        Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        String action = "";

        if (task.getProponent().getId().equals(employeeService.getCurrentEmployee().getId()) || employeeService.getCurrentEmployee().getUserAccess().getId().equals(3)) {
            if (task.getStatus().getId() != 3) {
                action += "Изменен статус: " + statusService.getStatusById(3).getName() + ";";
                task.setStatus(statusService.getStatusById(3));
            }
            if (!task.getClosed()) {
                task.setClosed('Y');
                action += "Задача закрыта;";
            } else {
                task.setClosed('N');
                action += "Задача открыта;";
            }
            taskService.saveTask(task);
        }

        TaskMessage taskMessage = new TaskMessage("", action, out, task, employeeService.getCurrentEmployee(), null);
        taskMessageService.addNewTaskMessage(taskMessage);
        taskService.updateTaskLastAnswer(id, out);

        return new ModelAndView("redirect:/tasks/" + id);
    }

    //Создание новой задачи
    @GetMapping(path = "/add")
    public String add(Model model) {
        if (employeeService.getCurrentEmployee().getUserAccess().getId().equals(1)) {
            return myErrorController.handleError(null, model);
        }

        Map<Integer, Map<Long, String>> depEmpMap = employeeService.mapDepartmentsAndEmployees();

        model.addAttribute("departments", departmentService.getDepartment());
        model.addAttribute("urgency", urgencyService.getUrgency());
        model.addAttribute("status", statusService.getStatus());
        model.addAttribute("responsible", employeeService.getEmployee());
        model.addAttribute("depEmpMap", depEmpMap);
        model.addAttribute("user", employeeService.getCurrentEmployee());

        return "addTask";
    }

    //Создать задачу
    @PostMapping(path = "add")
    public ModelAndView registerNewTask(@AuthenticationPrincipal Employee employee,
                                        @RequestParam(value = "header", defaultValue = "") String name,
                                        @RequestParam(value = "urgency", defaultValue = "") String urgency,
                                        @RequestParam(value = "responsibleDepartment", defaultValue = "") String responsibleDepartment,
                                        @RequestParam(value = "responsible", defaultValue = "") String responsible,
                                        @RequestParam(value = "deadline", defaultValue = "") String deadlineString,
                                        @RequestParam(value = "message", defaultValue = "") String message,
                                        @RequestParam(value = "file") MultipartFile file) throws ParseException {
        Date deadline;
        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
        Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        Task task;

        if (deadlineString.isEmpty())
            deadline = null;
        else
            deadline = new SimpleDateFormat("yyyy-dd-MM").parse(deadlineString);

        if (responsible.isEmpty())
            task = new Task(name, out, out, deadline, 'N', statusService.getStatusById(4), urgencyService.getUrgencyById(Integer.valueOf(urgency)),
                    employeeService.getEmployeeById(employee.getId()), employee.getDepartment(),
                    departmentService.getDepartmentById(Integer.valueOf(responsibleDepartment)));
        else
            task = new Task(name, out, out, deadline, 'N', statusService.getStatusById(5), urgencyService.getUrgencyById(Integer.valueOf(urgency)),
                    employeeService.getEmployeeById(Long.valueOf(responsible)), employeeService.getEmployeeById(employee.getId()), employee.getDepartment(),
                    departmentService.getDepartmentById(Integer.valueOf(responsibleDepartment)));

        taskService.addNewTask(task);

        TaskMessage taskMessage = new TaskMessage(message, "", out, task, employee, null);

        taskMessageService.addNewTaskMessage(taskMessage);
        storeFile(file, taskMessage);
        sendNotificationEmail(responsible, employee, task);

        return new ModelAndView("redirect:/tasks/" + taskService.getLatestTask().getId());
    }

    //Скачать файл
    @GetMapping("/{taskId}/files/{fileId}")
    @ResponseBody
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long taskId,
                                               @PathVariable Long fileId) {
        Files files = fileStorageService.getFileById(fileId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + files.getName() + "\"")
                .body(files.getData());
    }
}
