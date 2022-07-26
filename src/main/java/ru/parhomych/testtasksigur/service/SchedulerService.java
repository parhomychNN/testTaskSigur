package ru.parhomych.testtasksigur.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.parhomych.testtasksigur.components.EmployeesMgr;
import ru.parhomych.testtasksigur.components.GuestsMgr;
import ru.parhomych.testtasksigur.components.PassEmulator;
import ru.parhomych.testtasksigur.utilities.ParametersKeys;
import ru.parhomych.testtasksigur.utilities.StringValueTemplates;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class SchedulerService {
    @Autowired
    EmployeesMgr employeesMgr;
    @Autowired
    GuestsMgr guestsMgr;
    @Autowired
    PassEmulator passEmulator;

    @Autowired
    DepartmentService departmentService;

    final int VIRTUAL_DAY_FREQUENCY = 1000; // frequency of virtual days in ms

    public void start() {
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime virtualStartDateTime = LocalDateTime.of(2022, 1, 1, 0, 0, 1);
        LocalDateTime virtualStopDateTime = LocalDateTime.of(2022, 12, 31, 23, 59, 59);

        departmentService.fillTheDepartments();

        Map<String, Object> componentParams = new HashMap<>();

        // job that is being repeated once a second (emulates the virtual day)
        Timer timer = new Timer();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd yyyy, EEEE", Locale.ENGLISH);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                LocalDateTime actualDateTime = LocalDateTime.now();
                long iterationNumber = ChronoUnit.SECONDS.between(startDateTime, actualDateTime);
                LocalDateTime virtualActualDateTime = virtualStartDateTime.plusDays(iterationNumber);
                if (virtualActualDateTime.isAfter(virtualStopDateTime.minusDays(1))) {
                    timer.cancel();
                }

                log.info("---------------- " + virtualActualDateTime.format(DateTimeFormatter.ofPattern(StringValueTemplates.DATE_FORMAT)) + " ----------------");

                componentParams.put(ParametersKeys.ITERATION_NUMBER, iterationNumber);
                componentParams.put(ParametersKeys.VIRTUAL_ACTUAL_DATE_TIME, virtualActualDateTime);
                componentParams.put(ParametersKeys.VIRTUAL_STOP_DATE_TIME, virtualStopDateTime);

                componentParams.remove(ParametersKeys.LAST_HIRED_EMPLOYEE);

                log.debug(iterationNumber
                        + ".\t"
                        + virtualActualDateTime.format(formatter)
                );

                // calling employeesMgr job
                employeesMgr.performOneDayAction(componentParams);

                // calling guestsMgr job
                guestsMgr.performOneDayAction(componentParams);

                // calling passEmulator job
                passEmulator.performOneDayAction(componentParams);

            }
        }, 0, VIRTUAL_DAY_FREQUENCY);
    }

}
