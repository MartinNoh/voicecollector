package com.donggyeong.voicecollector.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	
	public Registration getRegistration(Integer id) {
		Optional<Registration> optional = this.registrationRepository.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		} else {
			throw new DataNotFoundException("Data not found.");
		}
	}
}
