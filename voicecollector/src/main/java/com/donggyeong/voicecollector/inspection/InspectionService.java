package com.donggyeong.voicecollector.inspection;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.donggyeong.voicecollector.DataNotFoundException;
import com.donggyeong.voicecollector.collection.Collection;
import com.donggyeong.voicecollector.collection.CollectionRepository;
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
	private final CollectionRepository collectionRepository;
	
	private final UserService userService;
	private final CollectionService collectionService;
	
	public Page<Collection> getList(int page, String kw, String category) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("modifiedDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		if("all".equals(category))	return this.inspectionRepository.findAllBySearch(kw, pageable);
		else if("wait".equals(category))	return this.inspectionRepository.findAllWaitingBySearch(kw, pageable);
		else	return this.inspectionRepository.findAllBySearch(kw, category, pageable);
	}
	
	public void create(String collectionId, String isApproved, String username) {
		Optional<Collection> optional = this.collectionRepository.findById(Integer.parseInt(collectionId));
		if(optional.isPresent()) {
			if(optional.get().getInspection() == null) {
				Inspection i = new Inspection();
				SiteUser siteUser = this.userService.getUserByEmail(username);
				i.setInspector(siteUser);
				i.setIsApproved(isApproved);
				Collection c = collectionService.getCollection(Integer.parseInt(collectionId));
				i.setWork(c);
				this.inspectionRepository.save(i);
			} else {
				Inspection i = optional.get().getInspection();
				SiteUser siteUser = this.userService.getUserByEmail(username);
				i.setInspector(siteUser);
				i.setIsApproved(isApproved);
				Collection c = collectionService.getCollection(Integer.parseInt(collectionId));
				i.setWork(c);
				this.inspectionRepository.save(i);
			}
		} else {
			throw new DataNotFoundException("Data not found.");
		}
	}
}
