package com.donggyeong.voicecollector.collection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.donggyeong.voicecollector.registration.RegistrationService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/collection")
@RequiredArgsConstructor
@Controller
public class CollectionController {

	private final RegistrationService registrationService;
	private final CollectionService collectionService;
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping("/record")
	public String record(Model model) {
		int scriptCnt = registrationService.getScriptCnt();
		int teamRecordCnt = collectionService.getRecordCnt();
		model.addAttribute("scriptCnt", scriptCnt);
		model.addAttribute("teamRecordCnt", teamRecordCnt);
		model.addAttribute("myRecordCnt", "myRecordCnt");
		return "collection_record";
	}	
	
	@PostMapping("/upload")
	public ResponseEntity<?> handleFileUpload(@RequestParam("scriptId") String scriptId, @RequestParam("audioType") String audioType, @RequestParam("base64Data") String base64Data) {
	    try {
	    	collectionService.upload(scriptId, audioType, base64Data);	        
	    } catch (Exception e) {
	      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    } 
	    return ResponseEntity.ok("File uploaded successfully.");
	  }
}
