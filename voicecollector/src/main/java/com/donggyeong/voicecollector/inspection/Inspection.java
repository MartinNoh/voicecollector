package com.donggyeong.voicecollector.inspection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;

import com.donggyeong.voicecollector.BasetimeEntity;
import com.donggyeong.voicecollector.collection.Collection;
import com.donggyeong.voicecollector.user.SiteUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Inspection extends BasetimeEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, columnDefinition = "char")
	private String isApproved;

	@ManyToOne
	private SiteUser whoseTurn;
	
	@ManyToOne
	private SiteUser inspector;
	
	@OneToOne
	private Collection work;
	
	@PrePersist
	public void prePersist() {
		this.isApproved = this.isApproved == null ? "n" : this.isApproved;
	}
}
