package com.tunan.hadoop.platform.schedule;

import com.tunan.hadoop.platform.domain.HDFSSummary;
import com.tunan.hadoop.platform.domain.HadoopMetrics;
import com.tunan.hadoop.platform.service.MetricsService;
import com.tunan.hadoop.platform.utils.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.util.Date;

@Component
public class HadoopJmxSchedule {


    @Value("${tunan.hadoop.nn.url}")
    private String nnUrl;

    @Autowired
    private MetricsService metricsService;

    @Scheduled(cron = "*/2 * * * * *")
    public void metrics(){

        String nameNodeInfo = "/jmx?qry=Hadoop:service=NameNode,name=NameNodeInfo";
        String url = "http://"+nnUrl+ nameNodeInfo;

        try {
            HadoopMetrics metrics = HttpClient.httpGet(url);

            HDFSSummary hdfsSummary = new HDFSSummary();
            hdfsSummary.setDfsTotal(Long.parseLong(metrics.getValue("Total").toString()));
            hdfsSummary.setDfsUsed(Long.parseLong(metrics.getValue("Used").toString()));
            hdfsSummary.setPercentUsed(Float.parseFloat(metrics.getValue("PercentUsed").toString()));
            hdfsSummary.setDfsFree(Long.parseLong(metrics.getValue("Free").toString()));
            hdfsSummary.setFilesTotal(Long.parseLong(metrics.getValue("TotalFiles").toString()));
            hdfsSummary.setBlocksTotal(Long.parseLong(metrics.getValue("TotalBlocks").toString()));
            hdfsSummary.setFreePercentUsed(Float.parseFloat(metrics.getValue("PercentRemaining").toString()));

            hdfsSummary.setTrash(false);
            hdfsSummary.setCreateTime(System.currentTimeMillis());

            metricsService.addHDFSSummary(hdfsSummary);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}