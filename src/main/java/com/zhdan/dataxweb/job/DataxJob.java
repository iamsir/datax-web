package com.zhdan.dataxweb.job;

import java.util.Date;
import java.util.Map;

import com.zhdan.dataxweb.constants.Constant;
import com.zhdan.dataxweb.dto.RunJobDto;
import com.zhdan.dataxweb.entity.JobConfig;
import com.zhdan.dataxweb.service.DataxJobConfigService;
import com.zhdan.dataxweb.service.DataxJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

/***
 * @author dongan.zhang
 */
@Slf4j
@DisallowConcurrentExecution
public class DataxJob implements Job {

    @Autowired
    private DataxJobService dataxJobService;

    @Autowired
    private DataxJobConfigService dataxJobConfigService;


    @Override
    public void execute(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        String jobName = jobDetail.getKey().getName();
        String jobGroup = jobDetail.getKey().getGroup();

        Map<String, Object> jobParamsMap = jobDetail.getJobDataMap();

        Integer jobId = (Integer) jobParamsMap.get(Constant.DATAX_JOB_ID);
        JobConfig jobConfig = dataxJobConfigService.getById(jobId);

        RunJobDto runJobDto = new RunJobDto();
        runJobDto.setJobConfigId(Long.parseLong(jobId.toString()));
        runJobDto.setJobJson(jobConfig.getConfigJson());
        dataxJobService.startJobLog(runJobDto);

        log.info("定时任务执行,DataxJob:\n{}:{}:{}-->{}", jobId,jobGroup, jobName, new Date());
    }
}  
