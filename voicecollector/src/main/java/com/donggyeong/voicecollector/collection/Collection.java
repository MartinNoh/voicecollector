package com.donggyeong.voicecollector.collection;

import com.donggyeong.voicecollector.BasetimeEntity;
import com.donggyeong.voicecollector.registration.Registration;
import com.donggyeong.voicecollector.user.SiteUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Collection extends BasetimeEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, length = 131072)
	private String base64Data;
	
	@Column(nullable = false)
	private String dirPath;
	
	@Column(nullable = false)
	private String fileName;
	
	@Column(nullable = false)
	private String extension;
	
	@Column(nullable = false, columnDefinition = "char")
	private String inUseYn;
	
	@ManyToOne
	private SiteUser author;
	
	@ManyToOne
	private Registration script;
	
	@PrePersist
	public void prePersist() {
		this.inUseYn = this.inUseYn == null ? "y" : this.inUseYn;
	}
}
