package com.donggyeong.voicecollector.email;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@RequestMapping("/email")
@RequiredArgsConstructor
@Controller
public class EmailController {
	
	private final EmailService emailService;
	
	@PostMapping("/confirm")	
	public ResponseEntity<Map<String, Object>> confirm(@RequestParam("confirmEmail") String confirmEmail) {
		Map<String, Object> result = new HashMap<>();
	    try {
	    	String confirmCode = emailService.sendSimpleMessage(confirmEmail);
	    	result.put("confirmCode", confirmCode);
	    } catch (Exception e) {
	      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    } 
	    return ResponseEntity.ok().body(result);
	  }
}
