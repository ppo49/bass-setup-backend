package com.isa.projkekat.isa_rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;

@SpringBootApplication
public class IsaProjekatApplication {



	public static void main(String[] args) {
		SpringApplication.run(IsaProjekatApplication.class, args);
	}

}
