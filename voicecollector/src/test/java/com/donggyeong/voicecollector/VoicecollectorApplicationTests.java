package com.donggyeong.voicecollector;

import java.security.Principal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.donggyeong.voicecollector.registration.RegistrationRepository;
import com.donggyeong.voicecollector.registration.RegistrationService;
import com.donggyeong.voicecollector.user.SiteUser;
import com.donggyeong.voicecollector.user.UserService;

@SpringBootTest
@ContextConfiguration(classes = VoicecollectorApplication.class)
class VoicecollectorApplicationTests {

	@Autowired
	private RegistrationService registrationService;
	private UserService userService;
	
	@Test
	void contextLoads() {
		for(int i=1; i<=300; i++) {
			String script = String.format("테스트 데이터입니다. : [%03d]", i);
			SiteUser siteUser =  this.userService.getUser("ehdrud1129@saltlux.com");
			this.registrationService.create(script, siteUser);
		}
	}

}
