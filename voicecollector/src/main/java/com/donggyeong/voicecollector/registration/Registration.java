package com.donggyeong.voicecollector.registration;

import java.util.List;

import com.donggyeong.voicecollector.BasetimeEntity;
import com.donggyeong.voicecollector.collection.Collection;
import com.donggyeong.voicecollector.user.SiteUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	
	@ManyToOne
	private SiteUser writer;
	
	@OneToMany(mappedBy = "script", cascade = CascadeType.REMOVE)
	private List<Collection> collectionList;
	
	@PrePersist
	public void prePersist() {
		this.inUseYn = this.inUseYn == null ? "y" : this.inUseYn;
	}
}
