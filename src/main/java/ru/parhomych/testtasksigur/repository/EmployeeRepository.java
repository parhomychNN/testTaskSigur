package ru.parhomych.testtasksigur.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.parhomych.testtasksigur.entities.Employee;

import java.util.Date;
import java.util.List;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {

    List<Employee> findEmployeesByHireTimeBeforeAndFiredTimeIsNull(Date actualVirtualTime);

}
