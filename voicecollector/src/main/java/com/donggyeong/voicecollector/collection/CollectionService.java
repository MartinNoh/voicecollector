package com.donggyeong.voicecollector.collection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.donggyeong.voicecollector.registration.RegistrationRepository;

import lombok.RequiredArgsConstructor;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

@RequiredArgsConstructor
@Service
public class CollectionService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final CollectionRepository collectionRepository;
	
	
	public Integer getRecordCnt() {
		return collectionRepository.getTotalCollectionCnt();
	}
	
	@Value("${upload.base.path}")
	private String uploadBasePath;
	
	@Value("${ffmpeg.source.path}")
	private String ffmpegSourcePath;
	
	public void upload(String scriptId, String audioType, String base64Data) throws IOException {
    	logger.info("1. scriptId : " + scriptId);
    	logger.info("2. audioType : " + audioType);
    	logger.info("3. base64Data : " + base64Data.substring(0, 40) + "......");
    	
    	byte[] pcmData = Base64.getDecoder().decode(base64Data.split(",")[1]);
    	
    	// make directory
    	String dirPath = uploadBasePath + "audio/" + scriptId + "/";
        File dir = new File(dirPath);
        if (dir.mkdirs())	logger.info("Created successfully: " + dirPath);
        
        // pcm file upload
    	String filePath = dirPath + scriptId + "_" + getCurrentDateTime();
    	String pcmFilePath = filePath + ".pcm";
    	File pcmFile = new File(pcmFilePath);
        FileOutputStream os = new FileOutputStream(pcmFile, true);
        os.write(pcmData);
        os.close();

        // ffmpeg convert pcm to wav
        String wavFilePath = filePath + ".wav";
        FFmpeg ffmpeg = new FFmpeg(ffmpegSourcePath + "ffmpeg");
        FFprobe ffprobe = new FFprobe(ffmpegSourcePath + "ffprobe");
        
        FFmpegBuilder builder = new FFmpegBuilder().setInput(pcmFilePath)
        		.overrideOutputFiles(true)
        		.addOutput(wavFilePath)
        		.setFormat("wav")
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
	}

	public static String getCurrentDateTime() {
		Date today = new Date();
		Locale currentLocale = new Locale("KOREAN", "KOREA");
		String pattern = "yyyyMMddHHmmss";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);
		return formatter.format(today);
	}
}
