package com.donggyeong.voicecollector.registration;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationForm {
	@NotEmpty(message = "스크립트는 필수 항목입니다.")
	private String script;
}
