package com.donggyeong.voicecollector.registration;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.donggyeong.voicecollector.user.SiteUser;
import com.donggyeong.voicecollector.user.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/registration")
@RequiredArgsConstructor
@Controller
public class RegistrationController {

	private final RegistrationService registrationService;
	private final UserService userService;

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
	public String create(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw, @Valid RegistrationForm registrationForm, BindingResult bindingResult, Principal principal) {
		Page<Registration> paging = this.registrationService.getList(page, kw);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("modalClick", "create");
			return "registration_list";
		}
		SiteUser siteUser = this.userService.getUserByEmail(principal.getName());
		this.registrationService.create(registrationForm.getScript(), siteUser);
		return "redirect:/registration/list";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/read/excel")
	public String readExcel(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw, @RequestParam(value = "excelContents", defaultValue = "") String excelContents, @Valid RegistrationForm registrationForm, BindingResult bindingResult, Principal principal) {
		Page<Registration> paging = this.registrationService.getList(page, kw);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);

		try {
			SiteUser siteUser = this.userService.getUserByEmail(principal.getName());
			this.registrationService.readExcel(excelContents, siteUser);
		} catch(Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "컬럼명과 입력하신 스크립트 포맷을 확인 바랍니다.");
		}
		return "redirect:/registration/list";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify")
	public String modify(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw, @Valid RegistrationForm registrationForm, BindingResult bindingResult, @RequestParam("scriptId") String scriptId, @RequestParam("script") String script, Principal principal) {
		Page<Registration> paging = this.registrationService.getList(page, kw);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		model.addAttribute("scriptId", scriptId);
		if(bindingResult.hasErrors()) {
			model.addAttribute("modalClick", "modify");
			return "registration_list";
		}
		Registration registration = this.registrationService.getRegistration(Integer.parseInt(scriptId));
		if(!registration.getWriter().getEmail().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		this.registrationService.modify(registration, script);
		return "registration_list";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw, RedirectAttributes re, Principal principal) {
		re.addAttribute("page", page);
		re.addAttribute("kw", kw);
		Registration registration =this.registrationService.getRegistration(id);
		if(!registration.getWriter().getEmail().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
		}
		this.registrationService.delete(registration);
		return "redirect:/registration/list";
	}
}
