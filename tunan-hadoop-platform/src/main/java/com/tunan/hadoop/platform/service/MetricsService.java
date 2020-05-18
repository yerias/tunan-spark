package com.tunan.hadoop.platform.service;

import com.tunan.hadoop.platform.domain.HDFSSummary;

import java.util.List;

/**
 * @author Tunan
 */
public interface MetricsService {

    public void addHDFSSummary(HDFSSummary hdfsSummary);

    public HDFSSummary findHDFSSummaryByTime(Long time);

    public List<HDFSSummary> findHDFSSummarysByTime(Long start,Long end);

}
