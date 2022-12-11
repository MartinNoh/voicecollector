package com.donggyeong.voicecollector.inspection;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.donggyeong.voicecollector.collection.Collection;
import com.donggyeong.voicecollector.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class InspectionService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final InspectionRepository inspectionRepository;
	
	public Page<Collection> getList(SiteUser siteUser, int page, String kw) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("modifiedDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return this.inspectionRepository.findAllBySearch(siteUser, kw, pageable);
	}
}
