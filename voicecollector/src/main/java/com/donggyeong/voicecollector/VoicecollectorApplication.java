package com.donggyeong.voicecollector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class VoicecollectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(VoicecollectorApplication.class, args);
	}

}
