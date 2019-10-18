package com.zhdan.dataxweb.controller;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.zhdan.dataxweb.constants.Constant;
import com.zhdan.dataxweb.dto.JobConfigDto;
import com.zhdan.dataxweb.entity.JobConfig;
import com.zhdan.dataxweb.service.DataxJobConfigService;
import com.zhdan.dataxweb.util.JobUtil;
import com.zhdan.dataxweb.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Map;

/**
 * @author dongan.zhang
 */
@Controller
@RequestMapping("datax-job-config")
@Api(tags = "作业配置表接口")
@Slf4j
public class JobConfigController extends ApiController {
    /**
     * 服务对象
     */
    @Autowired
    private DataxJobConfigService dataxJobConfigService;

    @Autowired
    private JobUtil jobUtil;


    /**
     * 分页查询所有数据
     *
     * @return datax job
     */
    @GetMapping(value = "list")
    @ApiOperation("分页查询所有数据")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(paramType = "query", dataType = "String", name = "searchParam", value = "name or group", defaultValue = "test", required = true),
                    @ApiImplicitParam(paramType = "query", dataType = "String", name = "current", value = "当前页", defaultValue = "1", required = true),
                    @ApiImplicitParam(paramType = "query", dataType = "String", name = "size", value = "一页大小", defaultValue = "10", required = true)
            })
    @ResponseBody
    public R<IPage<Map<String, Object>>> selectAll(String searchParam, Integer pageNum, Integer pageSize) {

        IPage<Map<String, Object>> page = dataxJobConfigService.selectDataxJobConfigPage(searchParam, pageNum, pageSize);
        for (Map<String, Object> map : page.getRecords()) {
            //设置jobStatusInfo
            String jobStatusInfo = jobUtil.getJobStatusInfo((String) map.get("job_name"), (String)map.get("job_group"));
            map.put("job_status_info", jobStatusInfo);
            //任务状态正常，根据cron表达式计算下次运行时间
            if (StringUtils.equals(jobStatusInfo, Constant.JOB_STATUS_NORMAL)) {
                map.put("next_fire_time", jobUtil.getNextFireDate((String) map.get("cron_expression")));
            }
        }
        return success(page);
    }



    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @GetMapping("{id}")
    @ResponseBody
    public R<JobConfig> selectOne(@PathVariable Serializable id) {
        return success(this.dataxJobConfigService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param entity 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PostMapping(value = "add")
    @ResponseBody
    public R<Boolean> insert(@RequestBody JobConfigDto entity) {
        if(!JsonUtil.isJson(entity.getConfigJson())) {
            return failed("configJson格式有误");
        }
        if(StringUtils.isNotEmpty(entity.getSyncTimePoint())) {
            if(entity.getSyncTimePoint().length() != 6) {
                return failed("SyncTimePoint格式有误(yyyyMMdd)");
            }
            try {
                Integer.valueOf(entity.getSyncTimePoint());
            } catch (Exception e){
                return failed("SyncTimePoint格式有误(yyyyMMdd)");
            }
        }
        //
        QueryWrapper<JobConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("job_name", entity.getJobName())
                .eq("job_group", entity.getJobGroup());
        JobConfig job = dataxJobConfigService.getOne(queryWrapper);
        if(job != null) {
            return failed("已存在的job");
        }

        //
        try {
            dataxJobConfigService.runAndSave(entity);
        } catch (Exception e) {
            return failed(e.getMessage());
        }

        return success(true);
    }





    /**
     * 修改数据
     *
     * @param entity 实体对象
     * @return 修改结果
     */
    @PostMapping(value = "update")
    @ApiOperation("修改数据")
    @ResponseBody
    public R<Boolean> update(JobConfig entity) {
        return success(this.dataxJobConfigService.updateById(entity));
    }



}