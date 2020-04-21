package ru.codeforensics.photomark.learnAddCrud.controller;

import org.springframework.http.ResponseEntity;
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


    @PostMapping("/employees")
    ResponseEntity<Void> createEmployeer(@RequestBody Employees employees) {
        employeesService.createEmployees(employees);
        return ResponseEntity.ok().build();
    }
}
