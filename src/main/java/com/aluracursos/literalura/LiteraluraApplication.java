package com.aluracursos.literalura;

import com.aluracursos.literalura.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication{
    @Autowired
    private LibroService service;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

}
