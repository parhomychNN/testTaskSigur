package ru.parhomych.testtasksigur.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.parhomych.testtasksigur.entities.Department;
import ru.parhomych.testtasksigur.entities.Employee;
import ru.parhomych.testtasksigur.service.DepartmentService;
import ru.parhomych.testtasksigur.service.EmployeeService;
import ru.parhomych.testtasksigur.utilities.CardUtils;
import ru.parhomych.testtasksigur.utilities.ParametersKeys;
import ru.parhomych.testtasksigur.utilities.StringValueTemplates;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Component
public class EmployeesMgr extends JobComponent {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    DepartmentService departmentService;

    @Override
    public void performOneDayAction(Map<String, Object> actionParameters) {
        String infoString;

        long iterationNumber = (long) actionParameters.get(ParametersKeys.ITERATION_NUMBER);
        LocalDateTime virtualActualDateTime = (LocalDateTime) actionParameters.get(ParametersKeys.VIRTUAL_ACTUAL_DATE_TIME);
        LocalDateTime virtualStopDateTime = (LocalDateTime) actionParameters.get(ParametersKeys.VIRTUAL_STOP_DATE_TIME);
        actionParameters.put(ParametersKeys.WERE_THERE_FIRINGS_TODAY, false);
        actionParameters.remove(ParametersKeys.EMPLOYEES_THAT_WERE_FIRED_TODAY);

        // Hiring
        Employee employee = new Employee();
        employee.setCard(CardUtils.getRandom16ByteArray());
        Department department = departmentService.getRandomDepartment().get();
        employee.setDepartment(department);

        // calculating random HIRE_TIME
        Random random = new Random();
        long randomDaysToPeriodEnd = random.nextInt((int) ChronoUnit.DAYS.between(virtualActualDateTime, virtualStopDateTime) + 1);
        LocalDateTime dateTimeToHire = virtualActualDateTime.plusDays(randomDaysToPeriodEnd);

        employee.setHireTime(Timestamp.valueOf(dateTimeToHire));

        employeeService.saveOrUpdate(employee);

        infoString = virtualActualDateTime.format(DateTimeFormatter.ofPattern(StringValueTemplates.DATE_FORMAT))
                + ". Сотрудник "
                + employee.getId()
                + " нанят "
                + dateTimeToHire.format(DateTimeFormatter.ofPattern(StringValueTemplates.DATE_TIME_FORMAT))
                + ". Отдел: "
                + department.getName();
        System.out.println(infoString);
        log.info(infoString);

        actionParameters.put(ParametersKeys.LAST_HIRED_EMPLOYEE, employee);

        // Firing

        if ((iterationNumber + 1) % 5 == 0) {
            List<Employee> readyToBeFiredEmployees = employeeService.getReadyToBeFiredEmployees(Timestamp.valueOf(virtualActualDateTime));

            Random rand = new Random();
            int amountOfEmployeesToFire = 1 + rand.nextInt(3);
            amountOfEmployeesToFire = Math.min(amountOfEmployeesToFire, readyToBeFiredEmployees.size());

            log.debug("Amount of employees to fire: " + amountOfEmployeesToFire);
            int[] indexesFromReadyToFireEmployeesToFire = pickNRandomIndexes(amountOfEmployeesToFire, readyToBeFiredEmployees.size());

            List<Employee> employeesThatWereFired = new ArrayList<>();
            for (int indexToFire :
                    indexesFromReadyToFireEmployeesToFire) {
                Employee employeeToFire = readyToBeFiredEmployees.get(indexToFire);
                employeeService.fireTheEmployee(employeeToFire, Timestamp.valueOf(virtualActualDateTime));
                actionParameters.put(ParametersKeys.WERE_THERE_FIRINGS_TODAY, true);
                employeesThatWereFired.add(employeeToFire);
                infoString = virtualActualDateTime.format(DateTimeFormatter.ofPattern(StringValueTemplates.DATE_FORMAT))
                        + ". Сотрудник "
                        + employeeToFire.getId()
                        + " уволен "
                        + virtualActualDateTime.format(DateTimeFormatter.ofPattern(StringValueTemplates.DATE_FORMAT))
                        + ". Отдел: "
                        + employeeToFire.getDepartment().getName()
                        + ". Проработал "
                        + ChronoUnit.DAYS.between(employeeToFire.getHireTime().toInstant(), employeeToFire.getFiredTime().toInstant())
                        + ".";
                System.out.println(infoString);
                log.info(infoString);
            }
            actionParameters.put(ParametersKeys.EMPLOYEES_THAT_WERE_FIRED_TODAY, employeesThatWereFired);

            log.debug("EmployeeMgr performed");
        }
    }

    private int[] pickNRandomIndexes(int n, int wholeAmount) {

        List<Integer> list = new ArrayList<Integer>(wholeAmount);
        for (int i = 0; i < wholeAmount; i++)
            list.add(i);
        Collections.shuffle(list);

        int[] answer = new int[n];
        for (int i = 0; i < n; i++)
            answer[i] = list.get(i);
        Arrays.sort(answer);

        return answer;

    }

}