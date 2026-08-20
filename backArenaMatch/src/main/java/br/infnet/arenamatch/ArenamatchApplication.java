package br.infnet.arenamatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients; // <-- Import novo

@SpringBootApplication
@EnableFeignClients // <-- Liga o Spring Cloud OpenFeign
public class ArenamatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArenamatchApplication.class, args);
    }
}