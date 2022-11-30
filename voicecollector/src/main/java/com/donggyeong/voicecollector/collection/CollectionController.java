package com.donggyeong.voicecollector.collection;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/collection")
@Controller
public class CollectionController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@RequestMapping("/record")
	public String record() {
		return "collection_record";
	}	
	
	
	@Value("${upload.audio.base.path}")
	private String recordedBasePath;
	
	@PostMapping("/upload")
	public ResponseEntity<?> handleFileUpload(@RequestParam("scriptId") String scriptId, @RequestParam("audioType") String audioType, @RequestParam("base64Data") String base64Data) {

	    try {
	    	logger.info("1. scriptId : " + scriptId);
	    	logger.info("2. audioType : " + audioType);
	    	logger.info("3. base64Data : " + base64Data.substring(0, 40) + "......");
	        
	    	byte[] binary = Base64.getDecoder().decode(base64Data.split(",")[1]);
	    	
	    	String filePath = recordedBasePath + scriptId + "_" + getCurrentDateTime() + "." + audioType;
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
