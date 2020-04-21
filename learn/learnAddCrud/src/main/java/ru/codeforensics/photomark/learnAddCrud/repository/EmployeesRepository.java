package ru.codeforensics.photomark.learnAddCrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.codeforensics.photomark.learnAddCrud.entity.Employees;


import java.util.List;

public interface EmployeesRepository extends JpaRepository<Employees, Long> {
    List<Employees> findAll();

   <S extends Employees> S save(S s);

}
