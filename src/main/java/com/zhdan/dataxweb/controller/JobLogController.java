package com.zhdan.dataxweb.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhdan.dataxweb.entity.JobLog;
import com.zhdan.dataxweb.service.DataxJobLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.IOException;

/**
 * @author dongan.zhang
 **/
@RestController
@RequestMapping("datax-log")
@Api(tags = "datax日志接口")
public class JobLogController extends ApiController{

    @Autowired
    private DataxJobLogService dataxJobLogService;

    /**
     * 分页查询
     *
     * @return datax log
     */
    @GetMapping(value = "list")
    @ApiOperation("分页查询所有数据")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(paramType = "query", dataType = "String", name = "jobId", value = "jobId", defaultValue = "1", required = true),
                    @ApiImplicitParam(paramType = "query", dataType = "String", name = "current", value = "当前页", defaultValue = "1", required = true),
                    @ApiImplicitParam(paramType = "query", dataType = "String", name = "size", value = "一页大小", defaultValue = "10", required = true)
            })
    public R<IPage<JobLog>> selectAll(Integer jobId, Integer pageNum, Integer pageSize) {
        Page page = new Page(pageNum, pageSize);
        QueryWrapper<JobLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("job_id", jobId).orderByDesc("create_date");
        IPage<JobLog> iPage = dataxJobLogService.page(page, queryWrapper);

        return success(iPage);
    }


    /**
     * 获取日志内容
     *
     * @return datax log
     */
    @GetMapping(value = "content/{logId}")
    @ApiOperation("查询日志内容")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(paramType = "query", dataType = "String", name = "jobId", value = "jobId", defaultValue = "1", required = true)
            })
    public R<String> selectLogContent(@PathVariable("logId") Integer logId) throws IOException {
        JobLog jobLog = dataxJobLogService.getOne(new QueryWrapper<JobLog>().eq("id", logId));
        return success(readFile(jobLog.getLogFilePath()));
    }


    private String readFile(String fileName) throws IOException {
        return FileCopyUtils.copyToString(new FileReader(fileName));
    }



}
