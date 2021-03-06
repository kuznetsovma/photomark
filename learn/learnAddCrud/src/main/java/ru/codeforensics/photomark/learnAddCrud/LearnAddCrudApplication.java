package ru.codeforensics.photomark.learnAddCrud;

import ru.codeforensics.photomark.learnAddCrud.entity.Employees;
import ru.codeforensics.photomark.learnAddCrud.repository.EmployeesRepository;
import ru.codeforensics.photomark.learnAddCrud.service.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;


@SpringBootApplication
public class LearnAddCrudApplication {

    @Autowired
    private EmployeesService employeesService;



    public static void main(String[] args) {
        SpringApplication.run(LearnAddCrudApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    private void testJpaMethods() {

        employeesService.findAll().forEach(it -> System.out.println(it));


//        Employees employees = new Employees();
//        employees.setFirstName("Max");
//        employees.setLastName("Smith");
//        employees.setEmail("maxsmith@gmail.com");
//        employeesService.createEmployees(employees);


    }

}
