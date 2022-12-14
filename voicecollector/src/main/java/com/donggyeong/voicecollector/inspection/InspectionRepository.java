package com.donggyeong.voicecollector.inspection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.donggyeong.voicecollector.collection.Collection;
import com.donggyeong.voicecollector.user.SiteUser;


public interface InspectionRepository extends JpaRepository<Inspection, Integer> {

	@Query("select  c "
			+ "from Collection c "
			+ "left outer join Inspection i on i.work = c "
			+ "left outer join Registration r on r = c.script "
			+ "left outer join SiteUser s on s = c.author "
			+ "where 1=1 "
			+ "    and ( "
			+ "        r.script like %:kw% "
			+ "        or s.email like %:kw% "
			+ "        or s.nickname like %:kw% "
			+ "    ) "
			+ "    and r.inUseYn = 'y' "
			+ "    and c.inUseYn = 'y' "
			+ "    and s.inUseYn = 'y' "
			+ "order by i.isApproved desc"
			)
	Page<Collection> findAllBySearch(@Param("kw") String kw, Pageable pageable);
	
	@Query("select  c "
			+ "from Collection c "
			+ "left outer join Inspection i on i.work = c "
			+ "left outer join Registration r on r = c.script "
			+ "left outer join SiteUser s on s = c.author "
			+ "where 1=1 "
			+ "    and ( "
			+ "        r.script like %:kw% "
			+ "        or s.email like %:kw% "
			+ "        or s.nickname like %:kw% "
			+ "    ) "
			+ "    and r.inUseYn = 'y' "
			+ "    and c.inUseYn = 'y' "
			+ "    and s.inUseYn = 'y' "
			+ "    and c.inspection.isApproved = :category "
			+ "order by i.isApproved desc"
			)
	Page<Collection> findAllBySearch(@Param("kw") String kw, @Param("category") String category, Pageable pageable);
	
	@Query("select  c "
			+ "from Collection c "
			+ "left outer join Inspection i on i.work = c "
			+ "left outer join Registration r on r = c.script "
			+ "left outer join SiteUser s on s = c.author "
			+ "where 1=1 "
			+ "    and ( "
			+ "        r.script like %:kw% "
			+ "        or s.email like %:kw% "
			+ "        or s.nickname like %:kw% "
			+ "    ) "
			+ "    and r.inUseYn = 'y' "
			+ "    and c.inUseYn = 'y' "
			+ "    and s.inUseYn = 'y' "
			+ "    and i.id is null "
			+ "order by i.isApproved desc"
			)
	Page<Collection> findAllWaitingBySearch(@Param("kw") String kw, Pageable pageable);
	
	@Query("select "
			+ "count(distinct c) "
			+ "from Collection c "
			+ "where 1=1 "
			+ "    and c.inUseYn = 'y' "
			)
	Integer getTotalInspectionCnt();
	
	@Query("select "
			+ "count(distinct c) "
			+ "from Collection c "
			+ "left outer join Inspection i on i.work = c "
			+ "where 1=1 "
			+ "    and c.inUseYn = 'y' "
			+ "    and i.id is null "
			)
	Integer getWaitInspectionCnt();
	
	@Query("select "
			+ "count(distinct c) "
			+ "from Collection c "
			+ "where 1=1 "
			+ "    and c.inUseYn = 'y' "
			+ "    and c.inspection.isApproved = 'y' "
			)
	Integer getYInspectionCnt();
	
	@Query("select "
			+ "count(distinct c) "
			+ "from Collection c "
			+ "where 1=1 "
			+ "    and c.inUseYn = 'y' "
			+ "    and c.inspection.isApproved = 'n' "
			)
	Integer getNInspectionCnt();
}
