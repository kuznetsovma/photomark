package ru.codeforensics.photomark.learnAddCrud.controller;

import ru.codeforensics.photomark.learnAddCrud.entity.Employees;
import ru.codeforensics.photomark.learnAddCrud.service.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeesController {

    @Autowired
    private EmployeesService employeesService;

    @GetMapping("/employees")
    List<Employees> getAllEmployees() {
        return employeesService.findAll();
    }
}
