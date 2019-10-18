package com.zhdan.dataxweb.controller;

import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.zhdan.dataxweb.constants.Constant;
import com.zhdan.dataxweb.util.JobUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/***
 * 定时任务管理
 * @author dongan.zhang
 */
@RestController
@RequestMapping("schedule-job")
@Slf4j
public class ScheduleJobController  extends ApiController {


    @Autowired
    private Scheduler scheduler;

    @Autowired
    private JobUtil jobUtil;


    /***
     * 暂停任务
     * @param jobName:
     * @param jobGroupName:
     */
    @PostMapping(value = "/pause")
    public R<Boolean> pauseJob(@RequestParam(value = "jobName") String jobName,
                               @RequestParam(value = "jobGroup") String jobGroupName) throws Exception {
        String jobStatusInfo = jobUtil.getJobStatusInfo(jobName, jobGroupName);
        if (StringUtils.equals(jobStatusInfo, Constant.JOB_STATUS_PAUSED)) {
            throw new RuntimeException("当前任务已是暂停状态!");
        }
        JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
        try {
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        log.info("暂停任务");
        return success(true);

    }

    /***
     * 恢复任务
     * @param jobName:
     * @param jobGroupName:
     * @return: void
     */
    @PostMapping(value = "/resume")
    public R<Boolean> resumeJob(@RequestParam(value = "jobName") String jobName,
                                @RequestParam(value = "jobGroup") String jobGroupName) throws Exception {

        String jobStatusInfo = jobUtil.getJobStatusInfo(jobName, jobGroupName);
        if (!StringUtils.equals(jobStatusInfo, Constant.JOB_STATUS_PAUSED)) {
            throw new RuntimeException("任务仅在暂停状态时才能恢复!");
        }
        JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
        try {
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        log.info("恢复任务");
        return success(true);
    }

    /***
     * 更新定时任务
     * @param jobName:
     * @param jobGroupName:
     * @param cronExpression:
     */
    @PostMapping(value = "/reschedule")
    public R<Boolean> rescheduleJob(@RequestParam(value = "jobName") String jobName,
                              @RequestParam(value = "jobGroup") String jobGroupName,
                              @RequestParam(value = "cronExpression") String cronExpression) throws Exception {
        TriggerKey triggerKey = jobUtil.getTriggerKeyByJob(jobName, jobGroupName);

        //表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

        //按新的cronExpression重新构建trigger
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .withSchedule(scheduleBuilder).build();
        try {
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            log.error("更新定时任务失败", e);
            throw new RuntimeException(e);
        }
        return success(true);
        
    }

    /***
     * 删除任务
     * @param jobName:
     * @param jobGroupName:
     */
    @PostMapping(value = "/delete")
    public R<Boolean> deleteJob(@RequestParam(value = "jobName") String jobName,
                                @RequestParam(value = "jobGroup") String jobGroupName) throws Exception {

        JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
        TriggerKey triggerKey = jobUtil.getTriggerKeyByJob(jobName, jobGroupName);
        try {
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        return success(true);
    }

    /***
     * 查询任务
     * @param pageNum:
     * @param pageSize:
     */
//    @GetMapping(value = "/query")
//    public Map<String, Object> queryjob(@RequestParam(value = "pageNum") Integer pageNum, @RequestParam(value = "pageSize") Integer pageSize) {
//        PageInfo<ScheduleJob> jobAndTrigger = scheduleJobService.getJobAndTriggerDetails(pageNum, pageSize);
//
//        Map<String, Object> map = new HashMap<>(10);
//        map.put("JobAndTrigger", jobAndTrigger);
//        map.put("number", jobAndTrigger.getTotal());
//
//        return map;
//    }

}
