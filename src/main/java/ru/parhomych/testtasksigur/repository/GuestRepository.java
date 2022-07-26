package ru.parhomych.testtasksigur.repository;

import org.springframework.data.repository.CrudRepository;
import ru.parhomych.testtasksigur.entities.Employee;
import ru.parhomych.testtasksigur.entities.Guest;

import java.util.Date;
import java.util.List;

public interface GuestRepository extends CrudRepository<Guest, Integer> {

    Guest findGuestByEmployeeToGo (Employee employee);

}
