package com.donggyeong.voicecollector.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.donggyeong.voicecollector.DataNotFoundException;
import com.donggyeong.voicecollector.registration.Registration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public SiteUser create(String nickname, String email, String password) {
		SiteUser user = new SiteUser();
		user.setNickname(nickname);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		this.userRepository.save(user);
		return user;
	}
	
	public SiteUser getUserByEmail(String email) {
		Optional<SiteUser> siteUser =  this.userRepository.findByEmail(email);
		if(siteUser.isPresent()) {
			return siteUser.get();
		} else {
            throw new DataNotFoundException("siteuser not found");
		}
	}
	
	public SiteUser getUserById(Long id) {
		Optional<SiteUser> siteUser =  this.userRepository.findById(id);
		if(siteUser.isPresent()) {
			return siteUser.get();
		} else {
            throw new DataNotFoundException("siteuser not found");
		}
	}
	
	public Page<SiteUser> getList(int page, String kw) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createdDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return this.userRepository.findAllBySearch(kw, pageable);
	}
	
	public void modify(String siteUserId, String siteUserNickname, String siteUserInUseYn, String siteUserRole) {
		Long userId = Long.parseLong(siteUserId);
		SiteUser siteUser = this.getUserById(userId);
		UserRole userRole = UserRole.valueOf(siteUserRole);
		siteUser.setNickname(siteUserNickname);
		siteUser.setInUseYn(siteUserInUseYn);
		siteUser.setRole(userRole);
		this.userRepository.save(siteUser);
	}
	
	public int getTotalCnt() {
		return this.userRepository.getTotalUserCnt();
	}
}