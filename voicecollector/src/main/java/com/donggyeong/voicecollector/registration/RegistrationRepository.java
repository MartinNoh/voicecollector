package com.donggyeong.voicecollector.registration;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}
