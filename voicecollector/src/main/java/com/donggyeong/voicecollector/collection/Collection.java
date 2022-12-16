package com.donggyeong.voicecollector.collection;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;

import com.donggyeong.voicecollector.BasetimeEntity;
import com.donggyeong.voicecollector.inspection.Inspection;
import com.donggyeong.voicecollector.registration.Registration;
import com.donggyeong.voicecollector.user.SiteUser;

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
	
	@OneToOne(mappedBy = "work", cascade = CascadeType.REMOVE)
	private Inspection inspection;
	
	@PrePersist
	public void prePersist() {
		this.inUseYn = this.inUseYn == null ? "y" : this.inUseYn;
	}
}
