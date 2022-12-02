package com.donggyeong.voicecollector.user;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.donggyeong.voicecollector.DataNotFoundException;

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
	
	public SiteUser getUser(String email) {
		Optional<SiteUser> siteUser =  this.userRepository.findByEmail(email);
		if(siteUser.isPresent()) {
			return siteUser.get();
		} else {
            throw new DataNotFoundException("siteuser not found");
		}
	}
}