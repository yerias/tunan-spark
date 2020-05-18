package com.tunan.hadoop.platform.service;

import com.tunan.hadoop.platform.domain.HDFSSummary;
import com.tunan.hadoop.platform.respository.HDFSSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetricsServiceImpl implements MetricsService{

    @Autowired
    private HDFSSummaryRepository hdfsSummaryRepository;

    @Override
    public void addHDFSSummary(HDFSSummary hdfsSummary) {
        hdfsSummaryRepository.save(hdfsSummary);
    }

    @Override
    public HDFSSummary findHDFSSummaryByTime(Long time) {
        return hdfsSummaryRepository.findTopByTrashFalseAndCreateTimeLessThanEqualOrderByCreateTimeDesc(time);
    }

    @Override
    public List<HDFSSummary> findHDFSSummarysByTime(Long start, Long end) {
        return hdfsSummaryRepository.findByTrashFalseAndCreateTimeBetweenOrderByCreateTimeAsc(start,end);
    }
}
