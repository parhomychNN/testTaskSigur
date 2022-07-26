package ru.parhomych.testtasksigur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.parhomych.testtasksigur.config.StartupConfig;
import ru.parhomych.testtasksigur.service.SchedulerService;

@SpringBootApplication
public class TestTaskSigurApplication {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(StartupConfig.class);
        context.refresh();

        SpringApplication.run(TestTaskSigurApplication.class, args);

        SchedulerService schedulerService = (SchedulerService) context.getBean("schedulerService");
        schedulerService.start();

    }

}
