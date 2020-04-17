package ru.codeforensics.photomark.learnAddCrud.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import ru.codeforensics.photomark.learnAddCrud.entity.Employees;
import ru.codeforensics.photomark.learnAddCrud.repository.EmployeesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EmployeesRepository extends JpaRepository<Employees, Long> {
    List<Employees> findAll();

}
