package com.donggyeong.voicecollector.inspection;

import java.security.Principal;
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
import com.donggyeong.voicecollector.collection.CollectionService;
import com.donggyeong.voicecollector.registration.Registration;
import com.donggyeong.voicecollector.user.SiteUser;
import com.donggyeong.voicecollector.user.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class InspectionService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final InspectionRepository inspectionRepository;
	
	private final UserService userService;
	private final CollectionService collectionService;
	
	public Page<Collection> getList(SiteUser siteUser, int page, String kw) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("modifiedDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return this.inspectionRepository.findAllBySearch(siteUser, kw, pageable);
	}
	
	public void create(String collectionId, String isApproved, String username) {
		if(inspectionRepository.findById(Integer.parseInt(collectionId)).isEmpty()) {
			Inspection i = new Inspection();
			SiteUser siteUser = this.userService.getUserByEmail(username);
			i.setInspector(siteUser);
			i.setIsApproved(isApproved);
			Collection c = collectionService.getCollection(Integer.parseInt(collectionId));
			i.setWork(c);
			this.inspectionRepository.save(i);
		} else {
			Inspection i = inspectionRepository.findById(Integer.parseInt(collectionId)).get();
			SiteUser siteUser = this.userService.getUserByEmail(username);
			i.setInspector(siteUser);
			i.setIsApproved(isApproved);
			Collection c = collectionService.getCollection(Integer.parseInt(collectionId));
			i.setWork(c);
			this.inspectionRepository.save(i);
		}
	}
}
