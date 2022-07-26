package ru.parhomych.testtasksigur.repository;

import org.springframework.data.repository.CrudRepository;
import ru.parhomych.testtasksigur.entities.Department;
import ru.parhomych.testtasksigur.entities.Employee;

public interface DepartmentRepository extends CrudRepository<Department, Integer> {

}
