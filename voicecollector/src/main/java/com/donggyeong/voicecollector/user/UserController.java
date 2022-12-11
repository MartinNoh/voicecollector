package com.donggyeong.voicecollector.user;


import java.security.Principal;

import javax.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
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

import com.donggyeong.voicecollector.registration.Registration;
import com.donggyeong.voicecollector.registration.RegistrationForm;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;

	@GetMapping("/signup")
	public String signup(UserCreateForm userCreateForm, Model model) {
		model.addAttribute("useNav", 'N');
		return "signup_form";
	}
	
	@PostMapping("/signup")
	public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult, Model model) {
		model.addAttribute("useNav", 'N');
		
		if(bindingResult.hasErrors()) {
			return "signup_form";
		}
		
		if(!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordIncorrect", "패스워드 확인과 일치하지 않습니다.");
			return "signup_form";
		}
		
        try {
            userService.create(userCreateForm.getNickname(), 
                    userCreateForm.getEmail(), userCreateForm.getPassword1());
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
    		return "signup_form";
        }catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
    		return "signup_form";
        }
		
		return "login_form";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("useNav", 'N');
		return "login_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping("/list")
	public String manage(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw) {
		Page<SiteUser> paging = this.userService.getList(page, kw);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		return "user_list";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify")
	public String modify(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw, @RequestParam("siteUserId") String siteUserId, @RequestParam("siteUserNickname") String siteUserNickname, @RequestParam("siteUserInUseYn") String siteUserInUseYn, @RequestParam("siteUserRole") String siteUserRole, Principal principal) {
		
		Page<SiteUser> paging = this.userService.getList(page, kw);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		model.addAttribute("siteUserId", siteUserId);
		model.addAttribute("modalClick", "modify");
		this.userService.modify(siteUserId, siteUserNickname, siteUserInUseYn, siteUserRole);
		return "user_list";
	}
}