package com.zhdan.dataxweb.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhdan.dataxweb.dto.JobConfigDto;
import com.zhdan.dataxweb.entity.JobConfig;

import java.util.Map;

/**
 * 作业配置表服务接口
 * @author dongan.zhang

 */
public interface DataxJobConfigService extends IService<JobConfig> {

    /**
     * 查询JobConfig、 QRTZ_JOB_DETAILS关联的信息
     *
     * @param searchParam
     * @param pageNum 页码
     * @param pageSize 每页数据数
     * @return
     */
    Page<Map<String, Object>> selectDataxJobConfigPage(String searchParam, int pageNum, int pageSize);


    /**
     * @param entity
     * @return
     */
    boolean runAndSave(JobConfigDto entity) throws Exception;
}