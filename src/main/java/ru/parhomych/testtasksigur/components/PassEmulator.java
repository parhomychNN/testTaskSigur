package ru.parhomych.testtasksigur.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.parhomych.testtasksigur.entities.Employee;
import ru.parhomych.testtasksigur.entities.Guest;
import ru.parhomych.testtasksigur.entities.Person;
import ru.parhomych.testtasksigur.service.PersonService;
import ru.parhomych.testtasksigur.utilities.CardUtils;
import ru.parhomych.testtasksigur.utilities.ParametersKeys;
import ru.parhomych.testtasksigur.utilities.StringValueTemplates;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@Component
public class PassEmulator extends JobComponent {
    @Autowired
    PersonService personService;

    @Override
    public void performOneDayAction(Map<String, Object> actionParameters) {
        String infoString;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(StringValueTemplates.DATE_FORMAT);
        SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat(StringValueTemplates.DATE_TIME_FORMAT);

        Date virtualActualDateTime = Timestamp.valueOf((LocalDateTime) actionParameters.get(ParametersKeys.VIRTUAL_ACTUAL_DATE_TIME));

        // 10 times a day
        for (int i = 0; i < 10; i++) {

            // find out what card to use
            byte[] cardBytes = new byte[32];
            if (new Random().nextDouble() <= 0.2) {
                // probability 1/5 means random
                cardBytes = CardUtils.getRandom16ByteArray();
            } else {
                // probability 4/5 means real card
                List<Person> allPersons = personService.getAllPersons();
                Random random = new Random();
                int randomPersonIndex = random.nextInt(allPersons.size());
                cardBytes = allPersons.get(randomPersonIndex).getCard();
            }

            log.debug("Card used for attempt: " + CardUtils.getHexRepresentationOfByteArray(cardBytes));

            Person personByCard = personService.getPersonByCard(cardBytes);
            if (personByCard == null) {
                // unknown card
                infoString = "Поднесена неизвестная карта: "
                        + CardUtils.getHexRepresentationOfByteArray(cardBytes);
                System.out.println(infoString);
                log.info(infoString);
            } else {
                // somebody owns it
                if (personByCard instanceof Employee) {
                    log.debug("It's employee card");
                    Employee employee = (Employee) personByCard;
                    if (employee.getHireTime().toInstant().isBefore(virtualActualDateTime.toInstant())
                            && employee.getFiredTime() == null) {
                        // it's active employee card
                        infoString = simpleDateTimeFormat.format(virtualActualDateTime)
                                + " Предоставлен доступ сотруднику "
                                + employee.getId()
                                + ". Отдел: "
                                + employee.getDepartment().getName()
                                + ". Карта: "
                                + CardUtils.getHexRepresentationOfByteArray(employee.getCard());
                        System.out.println(infoString);
                        log.info(infoString);
                    } else if (employee.getHireTime().toInstant().isAfter(virtualActualDateTime.toInstant())) {
                        infoString = "Сотрудник "
                                + employee.getId()
                                + " еще не нанят. Доступ запрещен до "
                                + simpleDateFormat.format(employee.getHireTime());
                        System.out.println(infoString);
                        log.info(infoString);
                    } else if (employee.getFiredTime() != null
                            && employee.getFiredTime().toInstant().isBefore(virtualActualDateTime.toInstant())) {
                        // it's fired employee card
                        infoString = simpleDateTimeFormat.format(virtualActualDateTime)
                                + " Доступ запрещен сотруднику "
                                + employee.getId()
                                + ". Отдел: "
                                + employee.getDepartment().getName()
                                + ". Карта: "
                                + CardUtils.getHexRepresentationOfByteArray(employee.getCard());
                        System.out.println(infoString);
                        log.info(infoString);
                    }
                } else if (personByCard instanceof Guest) {
                    log.debug("It's guest card");
                    Guest guest = (Guest) personByCard;
                    if (guest.getVisitDate() != null) {
                        // meeting was assigned
                        infoString = simpleDateTimeFormat.format(virtualActualDateTime)
                                + " Предоставлен доступ гостю "
                                + guest.getId()
                                + ". Пришёл к "
                                + guest.getEmployeeToGo().getId()
                                + " из отдела: "
                                + guest.getEmployeeToGo().getDepartment().getName()
                                + ". Карта: "
                                + CardUtils.getHexRepresentationOfByteArray(guest.getCard())
                                + ". "
                                + ((virtualActualDateTime.toInstant().compareTo(guest.getVisitDate().toInstant()) == 0)
                                ? "Он пришел в день назначенной встречи "
                                : "Он пришел НЕ в день назначенной встречи ")
                                + guest.getVisitDate();
                        System.out.println(infoString);
                        log.info(infoString);
                    } else {
                        // no meeting assigned
                        infoString = simpleDateTimeFormat.format(virtualActualDateTime)
                                + " Доступ запрещён гостю "
                                + guest.getId()
                                + ". Карта: "
                                + CardUtils.getHexRepresentationOfByteArray(guest.getCard());
                        System.out.println(infoString);
                        log.info(infoString);
                    }
                }
            }
        }
        log.debug("PassEmulator performed");
    }
}

