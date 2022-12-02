package com.donggyeong.voicecollector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.donggyeong.voicecollector.user.UserCreateForm;

@Controller
public class MainController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@RequestMapping("/test/page")
	@ResponseBody
	public String testPage() {
		return "Hello world, this is a test page.";
	}
	

	@RequestMapping("/")
	public String index(UserCreateForm userCreateForm, Model model) {
		model.addAttribute("useNav", 'N');
		return "index";
	}
}
