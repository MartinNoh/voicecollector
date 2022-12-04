package com.donggyeong.voicecollector.registration;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.donggyeong.voicecollector.user.SiteUser;

public interface RegistrationRepository extends JpaRepository<Registration, Integer>{

	@Query("select "
			+ "distinct r "
			+ "from Registration r "
			+ "where 1=1 "
			+ "	and r.script like %:kw% "
			+ " and r.inUseYn != 'n'"
			)
	Page<Registration> findAllBySearch(@Param("kw") String kw, Pageable pageable);
	
	@Query("select "
			+ "count(distinct r) "
			+ "from Registration r "
			)
	Integer getTotalRegistrationCnt();
	
	@Query(nativeQuery = true, value = 
			"select * "
			+ "from registration r "
			+ "where r.id not in (select c.script_id from collection c where c.author_id = :#{#siteUser.id} and in_use_yn = 'y') "
			+ "order by r.modified_date asc "
			+ "limit 1 "
			)
	Registration getMyNewScriptData(@Param("siteUser") SiteUser siteUser);
}
