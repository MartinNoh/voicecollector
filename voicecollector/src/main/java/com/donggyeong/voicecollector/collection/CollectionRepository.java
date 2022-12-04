package com.donggyeong.voicecollector.collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.donggyeong.voicecollector.user.SiteUser;


public interface CollectionRepository extends JpaRepository<Collection, Integer>{

	@Query(nativeQuery = true, value =
			"select count(*) "
			+ "from collection c "
			+ "where c.author_id = :#{#siteUser.id} "
			)
	Integer getMyCollectionCnt(@Param("siteUser") SiteUser siteUser);
}
