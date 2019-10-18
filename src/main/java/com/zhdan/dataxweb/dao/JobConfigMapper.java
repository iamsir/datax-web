package com.zhdan.dataxweb.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhdan.dataxweb.entity.JobConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 作业配置表
 *
 * @author dongan.zhang
 */
public interface JobConfigMapper extends BaseMapper<JobConfig> {

    @Select("SELECT " +
            "  jc.id as job_id, " +
            "  t.job_name, " +
            "  t.job_group, " +
            "  jc.description, " +
            "  jc.config_json, " +
            "  jc.sync_time_point, " +
            "  jc.update_date, " +
            "  jc.create_date, " +
            "  ct.cron_expression " +
            "FROM " +
            "  QRTZ_TRIGGERS t " +
            "  LEFT JOIN QRTZ_CRON_TRIGGERS ct ON ( t.TRIGGER_NAME = ct.TRIGGER_NAME AND t.TRIGGER_GROUP = ct.TRIGGER_GROUP ) " +
            "  LEFT JOIN job_config jc ON ( t.JOB_NAME = jc.JOB_NAME AND t.JOB_GROUP = jc.JOB_GROUP )")
    List<Map<String, Object>> selectJobConfig(Page<Map<String, Object>> page);

    @Select("SELECT " +
            "  jc.id as job_id, " +
            "  t.job_name, " +
            "  t.job_group, " +
            "  jc.description, " +
            "  jc.config_json, " +
            "  jc.sync_time_point, " +
            "  jc.update_date, " +
            "  jc.create_date, " +
            "  ct.cron_expression " +
            "FROM " +
            "  QRTZ_TRIGGERS t " +
            "  LEFT JOIN QRTZ_CRON_TRIGGERS ct ON ( t.TRIGGER_NAME = ct.TRIGGER_NAME AND t.TRIGGER_GROUP = ct.TRIGGER_GROUP ) " +
            "  LEFT JOIN job_config jc ON ( t.JOB_NAME = jc.JOB_NAME AND t.JOB_GROUP = jc.JOB_GROUP ) " +
            "WHERE  jc.JOB_NAME like '%${searchParam}%' or jc.JOB_GROUP  like '%${searchParam}%'")
    List<Map<String, Object>> selectJobConfigByName(Page<Map<String, Object>> page, @Param("searchParam") String searchParam);


}