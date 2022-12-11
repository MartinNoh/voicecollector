package com.donggyeong.voicecollector.collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.donggyeong.voicecollector.user.SiteUser;


public interface CollectionRepository extends JpaRepository<Collection, Integer>{

	@Query(nativeQuery = true, value =
			"select count(*) "
			+ "from collection c "
			+ "where c.author_id = :#{#siteUser.id} and in_use_yn = 'y'"
			)
	Integer getMyCollectionCnt(@Param("siteUser") SiteUser siteUser);
	
	@Query("select "
			+ "distinct c "
			+ "from Collection c "
			+ "left outer join Registration r on c.script = r "
			+ "where 1=1 "
			+ "    and r.script like %:kw% "
			+ "    and r.inUseYn = 'y' "
			+ "    and c.inUseYn = 'y' "
			)
	Page<Collection> findAllBySearch(@Param("kw") String kw, Pageable pageable);
}
