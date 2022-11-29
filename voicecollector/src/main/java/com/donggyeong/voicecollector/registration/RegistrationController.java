package com.donggyeong.voicecollector.registration;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/registration")
@RequiredArgsConstructor
@Controller
public class RegistrationController {
	
	private final RegistrationService registrationService;

	@RequestMapping("/list")
	public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw, RegistrationForm registrationForm) {
		Page<Registration> paging = this.registrationService.getList(page, kw);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		return "registration_list";
	}
	
	@PostMapping("/create")
	public String create(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw, @Valid RegistrationForm registrationForm, BindingResult bindingResult) {
		Page<Registration> paging = this.registrationService.getList(page, kw);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("modalClick", 'Y');
			return "registration_list";
		}
		this.registrationService.create(registrationForm.getScript());
		return "redirect:/registration/list";
	}
	
	@GetMapping("/modify/{id}/{kw}/{page}")
	public String modify(Model model, RegistrationForm registrationForm, @PathVariable("id") Integer id, @PathVariable("kw") String kw, @PathVariable("page") int page) {
		Page<Registration> paging = this.registrationService.getList(page, kw);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		Registration registration = this.registrationService.getRegistration(id);
		registrationForm.setScript(registration.getScript());
		model.addAttribute("modalClick", 'Y');
		model.addAttribute("isModify", 'Y');
		return "registration_list";
	}
}
