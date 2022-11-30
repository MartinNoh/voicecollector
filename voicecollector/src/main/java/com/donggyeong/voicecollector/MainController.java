package com.donggyeong.voicecollector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@RequestMapping("/test/page")
	@ResponseBody
	public String testPage() {
		return "Hello world, this is a test page.";
	}
	

	/*
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	*/
}
