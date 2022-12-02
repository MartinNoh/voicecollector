package com.donggyeong.voicecollector.collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CollectionRepository extends JpaRepository<Collection, Integer>{
	/*
	@Query("select "
			+ "distinct c "
			+ "from Collection c "
			+ "left outer join Registration r on c.scriptId = r"
			+ "where 1=1 "
			+ "	and r.script like %:kw% "
			)
	Page<Collection> findAllBySearch(@Param("kw") String kw, Pageable pageable);
	*/
	
	@Query("select "
			+ "count(c) "
			+ "from Collection c "
			+ ""
			)
	Integer getTotalCollectionCnt();
}
