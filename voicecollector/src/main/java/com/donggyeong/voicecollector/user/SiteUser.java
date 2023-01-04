package com.donggyeong.voicecollector.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import com.donggyeong.voicecollector.BasetimeEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SiteUser extends BasetimeEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String nickname;
	
	@Column(nullable = false, columnDefinition = "char")
	private String inUseYn;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserRole role;
	
	@PrePersist
	public void prePersist() {
		this.inUseYn = this.inUseYn == null ? "y" : this.inUseYn;
		this.role = this.role == null ? UserRole.USER : this.role;
	}
}