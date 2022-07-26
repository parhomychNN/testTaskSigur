package ru.parhomych.testtasksigur.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.parhomych.testtasksigur.entities.Department;
import ru.parhomych.testtasksigur.entities.Employee;
import ru.parhomych.testtasksigur.repository.DepartmentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class DepartmentService {

    @Autowired
    DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        departmentRepository.findAll().forEach(department -> departments.add(department));
        return departments;
    }

    public void fillTheDepartments() {
        for (int i = 0; i < 10; i++) {
            Department department = new Department("Department " + i);
        }
    }

    public Optional<Department> getRandomDepartment() {
        List<Department> allDepartments = getAllDepartments();
        if (allDepartments.isEmpty()) {
            return Optional.empty();
        } else {
            Random random = new Random();
            return Optional.of(allDepartments.get(random.nextInt(allDepartments.size())));
        }
    }

}
