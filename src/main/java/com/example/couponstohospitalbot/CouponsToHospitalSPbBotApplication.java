package com.example.couponstohospitalbot;

import com.example.couponstohospitalbot.telegram.model.TrackingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CouponsToHospitalSPbBotApplication {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(CouponsToHospitalSPbBotApplication.class, args);
        new Thread(ApplicationContextHolder.getContext().getBean(TrackingService.class).waitCoupons());
    }
}
