package com.donggyeong.voicecollector.inspection;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.donggyeong.voicecollector.collection.Collection;
import com.donggyeong.voicecollector.collection.CollectionService;
import com.donggyeong.voicecollector.user.SiteUser;
import com.donggyeong.voicecollector.user.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/inspection")
@RequiredArgsConstructor
@Controller
public class InspectionController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final UserService userService;
	private final InspectionService inspectionService;
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping("/list")
	public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw, @RequestParam(value = "category", defaultValue = "all") String category) {
		Page<Collection> paging = this.inspectionService.getList(page, kw, category);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		model.addAttribute("category", category);
		return "inspection_list";
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping("/modify/{collectionId}")
	public String modify(RedirectAttributes re, @PathVariable("collectionId") String collectionId, @RequestParam("isApproved") String isApproved, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw, @RequestParam(value = "category", defaultValue = "all") String category, Principal principal) {
		String username = principal.getName();
		inspectionService.create(collectionId, isApproved, username);
		re.addAttribute("page", page);
		re.addAttribute("kw", kw);
		re.addAttribute("category", category);
		return "redirect:/inspection/list";
	}
	
}
