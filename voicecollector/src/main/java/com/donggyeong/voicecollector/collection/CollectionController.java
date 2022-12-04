package com.donggyeong.voicecollector.collection;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.donggyeong.voicecollector.registration.Registration;
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
		SiteUser siteUser = this.userService.getUser(principal.getName());
		int myRecordCnt = collectionService.getMyRecordCnt(siteUser);
		int scriptCnt = registrationService.getScriptCnt();
		Registration myNewScriptData = collectionService.getMyNewScript(siteUser);
		model.addAttribute("myRecordCnt", myRecordCnt);
		model.addAttribute("scriptCnt", scriptCnt);
		model.addAttribute("script", myNewScriptData.getScript());
		model.addAttribute("scriptId", myNewScriptData.getId());
		return "collection_record";
	}	
	
	@PostMapping("/upload")
	public ResponseEntity<?> handleFileUpload(@RequestParam("scriptId") String scriptId, @RequestParam("audioType") String audioType, @RequestParam("base64Data") String base64Data, Principal principal) {
	    try {
			SiteUser siteUser = this.userService.getUser(principal.getName());
	    	collectionService.upload(scriptId, audioType, base64Data, siteUser);	        
	    } catch (Exception e) {
	      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    } 
	    return ResponseEntity.ok("File uploaded successfully.");
	  }
}
