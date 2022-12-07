package com.donggyeong.voicecollector.registration;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import com.donggyeong.voicecollector.BasetimeEntity;
import com.donggyeong.voicecollector.collection.Collection;
import com.donggyeong.voicecollector.user.SiteUser;

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
	
	@ManyToOne
	private SiteUser writer;
	
	@OneToMany(mappedBy = "script", cascade = CascadeType.REMOVE)
	private List<Collection> collectionList;
	
	@PrePersist
	public void prePersist() {
		this.inUseYn = this.inUseYn == null ? "y" : this.inUseYn;
	}
}
