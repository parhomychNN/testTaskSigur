package ru.parhomych.testtasksigur.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.parhomych.testtasksigur.entities.Employee;
import ru.parhomych.testtasksigur.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;


    //getting all employee records
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<Employee>();
        employeeRepository.findAll().forEach(employee -> employees.add(employee));
        return employees;
    }

    //getting a specific record
    public Employee getEmployeeById(int id) {
        return employeeRepository.findById(id).get();
    }

    public void saveOrUpdate(Employee employee) {
        employeeRepository.save(employee);
    }

    //deleting a specific record
    public void delete(int id) {
        employeeRepository.deleteById(id);
    }

    public List<Employee> getReadyToBeFiredEmployees(Date actualVirtualTime) {
        return employeeRepository.findEmployeesByHireTimeBeforeAndFiredTimeIsNull(actualVirtualTime);
    }

    public void fireTheEmployee(Employee employee, Date dateToFire) {
        employee.setFiredTime(dateToFire);
        saveOrUpdate(employee);
    }
}
