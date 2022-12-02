package com.donggyeong.voicecollector.registration;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/registration")
@RequiredArgsConstructor
@Controller
public class RegistrationController {

	private final RegistrationService registrationService;

	@PreAuthorize("isAuthenticated()")
	@RequestMapping("/list")
	public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw, RegistrationForm registrationForm) {
		Page<Registration> paging = this.registrationService.getList(page, kw);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		return "registration_list";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String create(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw, @Valid RegistrationForm registrationForm, BindingResult bindingResult) {
		Page<Registration> paging = this.registrationService.getList(page, kw);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("modalClick", "create");
			return "registration_list";
		}
		this.registrationService.create(registrationForm.getScript());
		return "redirect:/registration/list";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/read/excel")
	public String readExcel(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw, @RequestParam(value = "excelContents", defaultValue = "") String excelContents, @Valid RegistrationForm registrationForm, BindingResult bindingResult) {
		Page<Registration> paging = this.registrationService.getList(page, kw);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);

		this.registrationService.readExcel(excelContents);
		/* To DO : 에러 처리 
		try {
			this.registrationService.readExcel(excelContents);
		} catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("modalClick", "readExcel");
			return "registration_list";
		}
		*/
		return "redirect:/registration/list";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify")
	public String modify(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw, @Valid RegistrationForm registrationForm, BindingResult bindingResult, @RequestParam("scriptId") String scriptId, @RequestParam("script") String script) {
		Page<Registration> paging = this.registrationService.getList(page, kw);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		model.addAttribute("scriptId", scriptId);
		if(bindingResult.hasErrors()) {
			model.addAttribute("modalClick", "modify");
			return "registration_list";
		}
		Registration registration = this.registrationService.getRegistration(Integer.parseInt(scriptId));
		this.registrationService.modify(registration, script);
		return "registration_list";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw, RedirectAttributes re) {
		re.addAttribute("page", page);
		re.addAttribute("kw", kw);
		Registration registration =this.registrationService.getRegistration(id);
		this.registrationService.delete(registration);
		return "redirect:/registration/list";
	}
}
