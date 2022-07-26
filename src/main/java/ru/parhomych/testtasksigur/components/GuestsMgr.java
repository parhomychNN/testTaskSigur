package ru.parhomych.testtasksigur.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.parhomych.testtasksigur.entities.Employee;
import ru.parhomych.testtasksigur.entities.Guest;
import ru.parhomych.testtasksigur.service.GuestService;
import ru.parhomych.testtasksigur.utilities.CardUtils;
import ru.parhomych.testtasksigur.utilities.ParametersKeys;
import ru.parhomych.testtasksigur.utilities.StringValueTemplates;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@Component
public class GuestsMgr extends JobComponent {

    @Autowired
    GuestService guestService;

    @Override
    public void performOneDayAction(Map<String, Object> actionParameters) {
        String infoString;

        // cancel guest meetings with employees that were fired today
        if ((boolean) actionParameters.get(ParametersKeys.WERE_THERE_FIRINGS_TODAY)) {
            List<Employee> employeesFired = (List<Employee>) actionParameters.get(ParametersKeys.EMPLOYEES_THAT_WERE_FIRED_TODAY);
            for (Employee employee :
                    employeesFired) {
                Guest guestByEmployee = guestService.getGuestByEmployee(employee);
                if (guestByEmployee != null) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(StringValueTemplates.DATE_FORMAT);
                    infoString = "Встреча гостя "
                            + guestByEmployee.getId()
                            + " с сотрудником "
                            + employee.getId()
                            + " отменена. Отдел: "
                            + employee.getDepartment().getName()
                            + ". Дата встречи: "
                            + simpleDateFormat.format(guestByEmployee.getVisitDate())
                            + ", дата увольнения сотрудника: "
                            + simpleDateFormat.format(employee.getFiredTime());
                    System.out.println(infoString);
                    log.info(infoString);
                    guestByEmployee.setVisitDate(null);
                    guestService.saveOrUpdate(guestByEmployee);
                }
            }
        }

        // create a guest with a probability of 1/2
        if (new Random().nextDouble() <= 0.5) {
            LocalDateTime virtualActualDateTime = (LocalDateTime) actionParameters.get(ParametersKeys.VIRTUAL_ACTUAL_DATE_TIME);
            Employee lastHiredEmployee = (Employee) actionParameters.get(ParametersKeys.LAST_HIRED_EMPLOYEE);
            Guest guest = new Guest();
            guest.setEmployeeToGo(lastHiredEmployee);
            guest.setCard(CardUtils.getRandom16ByteArray());

            // calculation of visit date
            LocalDateTime hireTime = lastHiredEmployee.getHireTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime sixMonthAfterHireTime = hireTime.plusMonths(6);
            int days = (int) ChronoUnit.DAYS.between(hireTime, sixMonthAfterHireTime);
            LocalDateTime visitDate = hireTime.plusDays(new Random().nextInt(days));
            guest.setVisitDate(Timestamp.valueOf(visitDate));

            guestService.saveOrUpdate(guest);

            infoString = "Гостю "
                    + guest.getId()
                    + " назначена встреча сотруднику "
                    + guest.getEmployeeToGo().getId()
                    + ". Отдел: "
                    + guest.getEmployeeToGo().getDepartment().getName()
                    + ". Дата встречи: "
                    + visitDate.format(DateTimeFormatter.ofPattern(StringValueTemplates.DATE_FORMAT))
                    + ". До встречи осталось: "
                    + ChronoUnit.DAYS.between(virtualActualDateTime, visitDate);
            System.out.println(infoString);
            log.info(infoString);
        }

        log.debug("GuestsMgr performed");
    }
}
