package ru.parhomych.testtasksigur.config;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.parhomych.testtasksigur.components.EmployeesMgr;
import ru.parhomych.testtasksigur.components.GuestsMgr;
import ru.parhomych.testtasksigur.components.PassEmulator;
import ru.parhomych.testtasksigur.repository.PersonRepository;
import ru.parhomych.testtasksigur.service.*;

import javax.annotation.PostConstruct;

@Configuration
@EnableJpaRepositories(basePackages = "ru.parhomych.testtasksigur.repository")
@ImportResource("classpath:context.xml")
public class StartupConfig {

    @PostConstruct
    public void init() {
        System.out.println("Start up config initialized.");
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public EmployeesMgr employeesMgr() {
        return new EmployeesMgr();
    }

    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Bean
    public GuestsMgr guestsMgr() {
        return new GuestsMgr();
    }

    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Bean
    public PassEmulator passEmulator() {
        return new PassEmulator();
    }


    @Bean
    public SchedulerService schedulerService() {
        return new SchedulerService();
    }

    @Bean
    public EmployeeService employeeService() {
        return new EmployeeService();
    }

    @Bean
    public DepartmentService departmentService() {
        return new DepartmentService();
    }

    @Bean
    public GuestService guestService() {
        return new GuestService();
    }

    @Bean
    public PassService passService() {
        return new PassService();
    }

    @Bean
    public PersonService personService() {
        return new PersonService();
    }

}
