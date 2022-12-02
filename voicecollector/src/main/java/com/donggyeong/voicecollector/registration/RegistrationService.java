package com.donggyeong.voicecollector.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.donggyeong.voicecollector.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RegistrationService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final RegistrationRepository registrationRepository;
	
	public Page<Registration> getList(int page, String kw) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createdDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return this.registrationRepository.findAllBySearch(kw, pageable);
	}
	
	public void create(String script) {
		Registration r= new Registration();
		r.setScript(script);
		this.registrationRepository.save(r);
	}
	
	public void readExcel(String excelContents) {
		JSONArray jsonArr = new JSONArray();
		JSONParser parser =  new JSONParser();
		try {
			jsonArr = (JSONArray)parser.parse(excelContents);
		} catch (ParseException e) {
			e.printStackTrace();
			logger.info("엑셀 업로드를 실패 하였습니다!");
		}
		for(int i=0; i<jsonArr.size();i ++) {
			JSONObject jsonObj = (JSONObject) jsonArr.get(i);
			String script = (String) jsonObj.get("script");
			create(script);
		}
	}
	
	public Registration getRegistration(Integer id) {
		Optional<Registration> optional = this.registrationRepository.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		} else {
			throw new DataNotFoundException("Data not found.");
		}
	}
	
	public void modify(Registration registration, String script) {
		registration.setScript(script);
		this.registrationRepository.save(registration);
	}
	
	public void delete(Registration registration) {
		registration.setInUseYn("n");
		this.registrationRepository.save(registration);
	}
}
