package com.donggyeong.voicecollector.registration;

import com.donggyeong.voicecollector.BasetimeEntity;

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
public class Registration extends BasetimeEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private String script;
	
	@Column(nullable = false, columnDefinition = "char")
	private String inUseYn;
	
	/*
	@ManyToOne
	private SiteUser email;
	*/
	
	@PrePersist
	public void prePersist() {
		this.inUseYn = this.inUseYn == null ? "y" : this.inUseYn;
	}
}
