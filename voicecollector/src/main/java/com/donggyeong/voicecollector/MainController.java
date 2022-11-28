package com.donggyeong.voicecollector;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@RequestMapping("/test/page")
	@ResponseBody
	public String testPage() {
		return "Hello world, this is a test page.";
	}
	
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	
	@SuppressWarnings("resource")
	@PostMapping("/upload")
	public ResponseEntity<?> handleFileUpload(@RequestParam("scriptSentenceId") String scriptSentenceId, @RequestParam("audioType") String audioType, @RequestParam("base64Data") String base64Data) {

	    try {
	    	logger.info("1. scriptSentenceId : " + scriptSentenceId);
	    	logger.info("2. audioType : " + audioType);
	    	logger.info("3. base64Data : " + base64Data);
	        
	    	byte[] binary = Base64.getDecoder().decode(base64Data.split(",")[1]);
	    	
	    	String filePath = System.getProperty( "user.home" ) + "\\audio\\" + scriptSentenceId + "_" + getCurrentDateTime() + "." + audioType;
	    	File file = new File(filePath);
	        FileOutputStream os = new FileOutputStream(file, true);
	        os.write(binary);
	        os.close();
	    	
	    } catch (Exception e) {
	      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    } 
	    return ResponseEntity.ok("File uploaded successfully.");
	  }

	
	public static String getCurrentDateTime() {
		Date today = new Date();
		Locale currentLocale = new Locale("KOREAN", "KOREA");
		String pattern = "yyyyMMddHHmmss";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);
		return formatter.format(today);
	}
}
