package com.donggyeong.voicecollector.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<SiteUser, Long>{
	Optional<SiteUser> findByEmail(String email);
	
	@Query("select "
			+ "distinct s "
			+ "from SiteUser s "
			+ "where 1=1 "
			+ "    and ( "
			+ "        s.email like %:kw% "
			+ "        or s.nickname like %:kw% "
			+ "    ) "
			)
	Page<SiteUser> findAllBySearch(@Param("kw") String kw, Pageable pageable);

	@Query("select "
			+ "distinct s "
			+ "from SiteUser s "
			+ "where 1=1 "
			+ "    and ( "
			+ "        s.email like %:kw% "
			+ "        or s.nickname like %:kw% "
			+ "        ) "
			+ "    and s.role = :userRole"
			)
	Page<SiteUser> findAllRoleBySearch(@Param("kw") String kw, Pageable pageable, @Param("userRole") UserRole userRole);
	
	@Query("select "
			+ "distinct s "
			+ "from SiteUser s "
			+ "where 1=1 "
			+ "    and ( "
			+ "        s.email like %:kw% "
			+ "        or s.nickname like %:kw% "
			+ "    ) "
			+ "    and s.inUseYn = :use_category"
			)
	Page<SiteUser> findAllUseBySearch(@Param("kw") String kw, Pageable pageable, @Param("use_category") String use_category);
	
	@Query("select "
			+ "distinct s "
			+ "from SiteUser s "
			+ "where 1=1 "
			+ "    and ( "
			+ "        s.email like %:kw% "
			+ "        or s.nickname like %:kw% "
			+ "    ) "
			+ "    and s.role = :userRole"
			+ "    and s.inUseYn = :use_category"
			)
	Page<SiteUser> findAllUseRoleBySearch(@Param("kw") String kw, Pageable pageable, @Param("use_category") String use_category, @Param("userRole") UserRole userRole);
	
	@Query("select "
			+ "count(distinct s) "
			+ "from SiteUser s "
			)
	Integer getTotalUserCnt();
}