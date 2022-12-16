package com.donggyeong.voicecollector.collection;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.donggyeong.voicecollector.registration.Registration;
import com.donggyeong.voicecollector.registration.RegistrationForm;
import com.donggyeong.voicecollector.registration.RegistrationService;
import com.donggyeong.voicecollector.user.SiteUser;
import com.donggyeong.voicecollector.user.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/collection")
@RequiredArgsConstructor
@Controller
public class CollectionController {

	private final RegistrationService registrationService;
	private final CollectionService collectionService;
	private final UserService userService;
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping("/record")
	public String record(Model model, Principal principal) {
		SiteUser siteUser = this.userService.getUserByEmail(principal.getName());
		int myRecordCnt = collectionService.getMyRecordCnt(siteUser);
		int scriptCnt = registrationService.getScriptCnt();
		Registration myNewScriptData = collectionService.getMyNewScript(siteUser);
		model.addAttribute("myRecordCnt", myRecordCnt);
		model.addAttribute("scriptCnt", scriptCnt);
		model.addAttribute("script", myNewScriptData == null ? "스크립트가 먼저 업로드되어야 합니다." : myNewScriptData.getScript());
		model.addAttribute("scriptId", myNewScriptData == null ? "0" : myNewScriptData.getId());
		return "collection_record";
	}	
	
	@PostMapping("/upload")
	public ResponseEntity<?> handleFileUpload(@RequestParam("scriptId") String scriptId, @RequestParam("audioType") String audioType, @RequestParam("base64Data") String base64Data, Principal principal) {
	    try {
			SiteUser siteUser = this.userService.getUserByEmail(principal.getName());
	    	collectionService.upload(scriptId, audioType, base64Data, siteUser);	        
	    } catch (Exception e) {
	      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    } 
	    return ResponseEntity.ok("File uploaded successfully.");
	  }
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping("/list")
	public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw, @RequestParam(value = "category", defaultValue = "all") String category) {
		Page<Collection> paging = this.collectionService.getList(page, kw, category);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		model.addAttribute("category", category);
		return "collection_list";
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping("/mylist")
	public String mylist(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw, Principal principal) {
		SiteUser siteUser = this.userService.getUserByEmail(principal.getName());
		Page<Collection> paging = this.collectionService.getMyList(page, kw, siteUser);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		return "collection_list";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String modify(Model model, @PathVariable("id") Integer id, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw, @RequestParam(value = "category", defaultValue = "all") String category) {
		Page<Collection> paging = this.collectionService.getList(page, kw, category);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		Collection collection = this.collectionService.getCollection(id);
		model.addAttribute("script", collection.getScript().getScript() == null ? "스크립트를 가져올 수 없습니다." : collection.getScript().getScript());
		model.addAttribute("collectionId", collection.getId() == null ? "0" : collection.getId());
		model.addAttribute("base64Data", collection.getBase64Data() == null ? "0" : collection.getBase64Data());
		return "collection_modify";
	}
	
	@PostMapping("/modify")
	public ResponseEntity<?> handleFileModify(@RequestParam("collectionId") String collectionId, @RequestParam("audioType") String audioType, @RequestParam("base64Data") String base64Data, Principal principal) {
	    try {
	    	Collection collection = this.collectionService.getCollection(Integer.parseInt(collectionId));
			SiteUser siteUser = this.userService.getUserByEmail(principal.getName());
	    	collectionService.modify(collection, audioType, base64Data, siteUser);	        
	    } catch (Exception e) {
	      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    } 
	    return ResponseEntity.ok("File uploaded successfully.");
	  }
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw, RedirectAttributes re, Principal principal) {
		re.addAttribute("page", page);
		re.addAttribute("kw", kw);
		Collection collection = this.collectionService.getCollection(id);
		if(!collection.getAuthor().getEmail().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
		}
		this.collectionService.delete(collection);
		return "redirect:/collection/list";
	}
}
