package com.donggyeong.voicecollector.user;

import com.donggyeong.voicecollector.BasetimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
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
	private String useYn;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserRole role;
	
	@PrePersist
	public void prePersist() {
		this.useYn = this.useYn == null ? "y" : this.useYn;
		this.role = this.role == null ? UserRole.USER : this.role;
	}
}