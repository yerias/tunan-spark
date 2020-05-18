package com.tunan.hadoop.platform.respository;

import com.tunan.hadoop.platform.domain.HDFSSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HDFSSummaryRepository extends JpaRepository<HDFSSummary,Long> {

    HDFSSummary findTopByTrashFalseAndCreateTimeLessThanEqualOrderByCreateTimeDesc(Long time);

    List<HDFSSummary> findByTrashFalseAndCreateTimeBetweenOrderByCreateTimeAsc(Long start,Long end);
}
