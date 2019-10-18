package com.zhdan.dataxweb.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhdan.dataxweb.constants.Constant;
import com.zhdan.dataxweb.dao.JobConfigMapper;
import com.zhdan.dataxweb.dto.JobConfigDto;
import com.zhdan.dataxweb.entity.JobConfig;
import com.zhdan.dataxweb.job.DataxJob;
import com.zhdan.dataxweb.service.DataxJobConfigService;
import com.zhdan.dataxweb.util.JobUtil;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 作业配置服务实现类
 *
 * @author dongan.zhang
 */
@Service
public class DataxJobConfigServiceImpl extends ServiceImpl<JobConfigMapper, JobConfig> implements DataxJobConfigService {

    @Resource
    private JobConfigMapper jobConfigMapper;

    @Autowired
    private JobUtil jobUtil;

    @Autowired
    private Scheduler scheduler;

    @Override
    public Page<Map<String, Object>> selectDataxJobConfigPage(String searchParam, int current, int size) {
        Page<Map<String, Object>> page = new Page<>(current, size);
        if(StringUtils.isEmpty(searchParam.trim())) {
            return page.setRecords(jobConfigMapper.selectJobConfig(page));
        } else {
            return page.setRecords(jobConfigMapper.selectJobConfigByName(page, searchParam));
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean runAndSave(JobConfigDto entity) throws Exception {
        String jobName = entity.getJobName();
        String jobGroupName = entity.getJobGroup();
        String configJson = entity.getConfigJson();
        String cronExpression = entity.getCronExpression();
        //
        JobConfig jobConfig = new JobConfig();
        BeanUtils.copyProperties(entity, jobConfig);
        save(jobConfig);
        // 启动调度器
        scheduler.start();
        // 构建job 信息
        JobDetail jobDetail = JobBuilder.newJob(DataxJob.class).withIdentity(jobName, jobGroupName).build();
        //任务信息
        // jobDetail.getJobDataMap().put(Constant.DATAX_CONFIG_JSON, configJson);
        jobDetail.getJobDataMap().put(Constant.DATAX_JOB_ID, jobConfig.getId());
        // 表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        TriggerKey triggerKey = jobUtil.getTriggerKeyByJob(jobName, jobGroupName);
        // 按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
                .withSchedule(scheduleBuilder).build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);

        } catch (SchedulerException e) {
            log.error("创建定时任务失败", e);
            return false;
        }

        return true;
    }

}
