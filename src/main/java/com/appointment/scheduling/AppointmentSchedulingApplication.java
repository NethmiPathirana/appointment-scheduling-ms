package com.appointment.scheduling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com/appointment/scheduling")
public class AppointmentSchedulingApplication {
//    echo "# appointment-scheduling-ms" >> README.md
//    git init
//    git add README.md
//    git commit -m "first commit"
//    git branch -M main
//    git remote add origin https://github.com/NethmiPathirana/appointment-scheduling-ms.git
//    git push -u origin main
    public static void main(String[] args) {

        SpringApplication.run(AppointmentSchedulingApplication.class, args);
    }

}
