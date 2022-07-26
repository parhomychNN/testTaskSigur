package ru.parhomych.testtasksigur.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.parhomych.testtasksigur.entities.Employee;
import ru.parhomych.testtasksigur.entities.Guest;
import ru.parhomych.testtasksigur.repository.GuestRepository;

import java.util.Optional;

@Service
public class GuestService {

    @Autowired
    GuestRepository guestRepository;

    public void saveOrUpdate(Guest guest) {
        guestRepository.save(guest);
    }

    public Guest getGuestByEmployee(Employee employee) {
        return guestRepository.findGuestByEmployeeToGo(employee);
    }

}
