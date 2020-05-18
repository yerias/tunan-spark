package com.tunan.hadoop.platform.service;


import com.tunan.hadoop.platform.domain.HDFSSummary;
import com.tunan.hadoop.platform.respository.HDFSSummaryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestMetricsService {

    @Autowired
    HDFSSummaryRepository hdfsSummaryRepository;

    @Test
    public void findByTime(){

        HDFSSummary bean = hdfsSummaryRepository.findTopByTrashFalseAndCreateTimeLessThanEqualOrderByCreateTimeDesc(1589611226012L);
        System.out.println(bean);
    }
    @Test
    public void findByTime2(){

        List<HDFSSummary> beans = hdfsSummaryRepository.findByTrashFalseAndCreateTimeBetweenOrderByCreateTimeAsc(1589611226012L, 1589611232012L);
        for (HDFSSummary bean : beans) {
            System.out.println(bean);
        }

    }

}
