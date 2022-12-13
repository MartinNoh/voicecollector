package com.donggyeong.voicecollector.inspection;

import com.donggyeong.voicecollector.collection.Collection;
import com.donggyeong.voicecollector.user.SiteUser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InspectionDTO {
	private Integer id;
	private String isApproved;
	private SiteUser inspector;
	private Collection work;

}
