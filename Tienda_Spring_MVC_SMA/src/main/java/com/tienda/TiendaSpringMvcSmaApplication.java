package com.tienda;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@Slf4j

public class TiendaSpringMvcSmaApplication {

    @GetMapping("/")
    public String mtdComienzo() {
        log.info("Ejecutando el controlador MVC");
        return "indice";
    }

    public static void main(String[] args) {
        SpringApplication.run(TiendaSpringMvcSmaApplication.class, args);
    }

}
