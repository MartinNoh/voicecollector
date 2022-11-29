package com.donggyeong.voicecollector;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.donggyeong.voicecollector.registration.RegistrationRepository;
import com.donggyeong.voicecollector.registration.RegistrationService;

@SpringBootTest
@ContextConfiguration(classes = VoicecollectorApplication.class)
class VoicecollectorApplicationTests {

	@Autowired
	private RegistrationService registrationService;
	
	@Test
	void contextLoads() {
		for(int i=1; i<300; i++) {
			String sentence = String.format("테스트 데이터입니다. : [%03d]", i);
			this.registrationService.create(sentence);
		}
	}

}
