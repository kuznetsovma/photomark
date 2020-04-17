package ru.codeforensics.photomark.learnAddCrud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.codeforensics.photomark.learnAddCrud.entity.Employees;
import ru.codeforensics.photomark.learnAddCrud.repository.EmployeesRepository;

import java.util.List;

@Service
public class EmployeesService {

    @Autowired
    private final EmployeesRepository employeesRepository;

    public EmployeesService(EmployeesRepository employeesRepository){
        this.employeesRepository = employeesRepository;
    }

    public List<Employees> findAll() {
        return employeesRepository.findAll();

    }

}
