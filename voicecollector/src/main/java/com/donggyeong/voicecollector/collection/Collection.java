package com.donggyeong.voicecollector.collection;

import com.donggyeong.voicecollector.BasetimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Collection extends BasetimeEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private Integer scriptId;
	
	@Column(nullable = false)
	private String base64Data;
	
	@Column(nullable = false)
	private String dirPath;
	
	@Column(nullable = false)
	private String fileName;
	
	@Column(nullable = false)
	private String extension;
}
