package com.donggyeong.voicecollector.collection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.donggyeong.voicecollector.DataNotFoundException;
import com.donggyeong.voicecollector.registration.Registration;
import com.donggyeong.voicecollector.registration.RegistrationRepository;
import com.donggyeong.voicecollector.registration.RegistrationService;
import com.donggyeong.voicecollector.user.SiteUser;

import lombok.RequiredArgsConstructor;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

@RequiredArgsConstructor
@Service
public class CollectionService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final RegistrationService registrationService;	
	private final CollectionRepository collectionRepository;
	private final RegistrationRepository registrationRepository;
	
	
	public Integer getMyRecordCnt(SiteUser siteUser) {
		return collectionRepository.getMyCollectionCnt(siteUser);
	}
	
	
	public Registration getMyNewScript(SiteUser siteUser) {	
		return registrationRepository.getMyNewScriptData(siteUser);
	}
	
	
	@Value("${upload.base.path}")
	private String uploadBasePath;
	
	@Value("${ffmpeg.source.path}")
	private String ffmpegSourcePath;
	
	public void upload(String scriptId, String audioType, String base64Data, SiteUser siteUser) throws IOException {
		logger.info("base64Data length : " + base64Data.length());
		logger.info(base64Data);
		
		String author = siteUser.getEmail();
		String authorFileName = author.replace("@", "_").replace(".", "_");
    	logger.info("1. scriptId : " + scriptId);
    	logger.info("2. audioType : " + audioType);  // wav
    	logger.info("3. base64Data : " + base64Data.substring(0, 40) + "......");
    	
    	byte[] pcmData = Base64.getDecoder().decode(base64Data.split(",")[1]);
    	
    	// make directory
    	String dirPath = uploadBasePath + "audio/" + scriptId + "/";
        File dir = new File(dirPath);
        if (dir.mkdirs())	logger.info("Created successfully: " + dirPath);
        
        // pcm file upload
        String fileName = scriptId + "_" + authorFileName + "_" + getCurrentDateTime();
    	String filePath = dirPath + fileName;
    	String pcmFilePath = filePath + ".pcm";
    	File pcmFile = new File(pcmFilePath);
        FileOutputStream os = new FileOutputStream(pcmFile, true);
        os.write(pcmData);
        os.close();

        // ffmpeg convert pcm to wav
        String wavFilePath = filePath + "." + audioType;
        FFmpeg ffmpeg = new FFmpeg(ffmpegSourcePath + "ffmpeg");
        FFprobe ffprobe = new FFprobe(ffmpegSourcePath + "ffprobe");
        
        FFmpegBuilder builder = new FFmpegBuilder().setInput(pcmFilePath)
        		.overrideOutputFiles(true)
        		.addOutput(wavFilePath)
        		.setFormat(audioType)
        		.setAudioChannels(1)
        		.setAudioCodec("pcm_s16le")
        		.setAudioRate(22050)
        		.setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
        		.done();
        
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
        
        // delete pcm file
        if(pcmFile.exists()) {
        	if(pcmFile.delete())	logger.info("Successfully deleted PCM file.");
        	else		logger.info("File deletion failed.");
        }else {
        	logger.info("Not found PCM file.");
        }
        
        // insert into db
        Collection c = new Collection();
        c.setScript(this.registrationService.getRegistration(Integer.parseInt(scriptId)));
        c.setBase64Data(base64Data);
        c.setDirPath(dirPath);
        c.setFileName(fileName);
        c.setExtension("." + audioType);
        c.setAuthor(siteUser);
        this.collectionRepository.save(c);
	}

	public void modify(Collection collection, String audioType, String base64Data, SiteUser siteUser) throws IOException {
		logger.info("base64Data length : " + base64Data.length());
		logger.info(base64Data);
		
		String originalFilePath = collection.getDirPath() + collection.getFileName() + collection.getExtension();
		String scriptId = String.valueOf(collection.getScript().getId());
		
		String author = siteUser.getEmail();
		String authorFileName = author.replace("@", "_").replace(".", "_");
    	logger.info("1. originalFilePath : " + originalFilePath);
    	logger.info("2. scriptId : " + scriptId);
    	logger.info("3. audioType : " + audioType);  // wav
    	logger.info("4. base64Data : " + base64Data.substring(0, 40) + "......");
    	
    	byte[] pcmData = Base64.getDecoder().decode(base64Data.split(",")[1]);
    	
    	// make directory
    	String dirPath = uploadBasePath + "audio/" + scriptId + "/";
        File dir = new File(dirPath);
        if (dir.mkdirs())	logger.info("Created successfully: " + dirPath);
        
        // pcm file upload
        String fileName = scriptId + "_" + authorFileName + "_" + getCurrentDateTime();
    	String filePath = dirPath + fileName;
    	String pcmFilePath = filePath + ".pcm";
    	File pcmFile = new File(pcmFilePath);
        FileOutputStream os = new FileOutputStream(pcmFile, true);
        os.write(pcmData);
        os.close();

        // ffmpeg convert pcm to wav
        String wavFilePath = filePath + "." + audioType;
        FFmpeg ffmpeg = new FFmpeg(ffmpegSourcePath + "ffmpeg");
        FFprobe ffprobe = new FFprobe(ffmpegSourcePath + "ffprobe");
        
        FFmpegBuilder builder = new FFmpegBuilder().setInput(pcmFilePath)
        		.overrideOutputFiles(true)
        		.addOutput(wavFilePath)
        		.setFormat(audioType)
        		.setAudioChannels(1)
        		.setAudioCodec("pcm_s16le")
        		.setAudioRate(22050)
        		.setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
        		.done();
        
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
        
        // delete pcm file
        if(pcmFile.exists()) {
        	if(pcmFile.delete())	logger.info("Successfully deleted PCM file.");
        	else		logger.info("PCM file deletion failed.");
        }else {
        	logger.info("Not found PCM file.");
        }
        
        // delete original wav file
    	File originalFile = new File(originalFilePath);
    	if(originalFile.exists()) {
    		if(originalFile.delete())	logger.info("Successfully deleted original file.");
        	else		logger.info("Original file deletion failed.");
    	}else {
        	logger.info("Not found original file.");
        }
    	
        // update db
        collection.setBase64Data(base64Data);
        collection.setFileName(fileName);
        this.collectionRepository.save(collection);
	}
	
	public static String getCurrentDateTime() {
		Date today = new Date();
		Locale currentLocale = new Locale("KOREAN", "KOREA");
		String pattern = "yyyyMMddHHmmss";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);
		return formatter.format(today);
	}
	
	public Page<Collection> getList(int page, String kw) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createdDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return this.collectionRepository.findAllBySearch(kw, pageable);
	}
	
	public Page<Collection> getMyList(int page, String kw, SiteUser siteUser) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createdDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return this.collectionRepository.findAllBySearch(kw, pageable, siteUser);
	}
	
	public Collection getCollection(Integer id) {
		Optional<Collection> optional = this.collectionRepository.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		} else {
			throw new DataNotFoundException("Data not found.");
		}
	}
	
	public void delete(Collection collection) {
		collection.setInUseYn("n");
		this.collectionRepository.save(collection);
	}
}
