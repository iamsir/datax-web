package com.zhdan.dataxweb.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import com.zhdan.dataxweb.dto.RunJobDto;
import com.zhdan.dataxweb.entity.JobConfig;
import com.zhdan.dataxweb.entity.JobLog;
import com.zhdan.dataxweb.log.JobLogFileAppender;
import com.zhdan.dataxweb.service.DataxJobConfigService;
import com.zhdan.dataxweb.service.DataxJobService;
import com.zhdan.dataxweb.service.DataxJobLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.concurrent.*;

/**
 * @author dongan.zhang
 **/
@Slf4j
@Service
public class DataxJobServiceImpl implements DataxJobService {

    private ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("datax-job-%d").build();

    private ExecutorService jobPool = new ThreadPoolExecutor(5, 200, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());


    /**
     * 日志文件保存目录
     */
    @Value("${app.etlLogDir}")
    private String etlLogDir;

    @Autowired
    private DataxJobConfigService dataxJobConfigService;

    @Autowired
    private DataxJobLogService dataxJobLogService;


    @Override
    public String startJobByJsonStr(String logFilePath, long jobId, String jobJson) {

        JobConfig jobConfig = dataxJobConfigService.getById(jobId);

        jobPool.submit(() -> {
            final String tmpFilePath = "jobTmp-" + jobConfig.getId() + "-" + System.currentTimeMillis() + ".conf";
            // 根据json写入到临时本地文件
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(tmpFilePath, "UTF-8");
                writer.println(jobJson);
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                log.info("JSON 临时文件写入异常：", e);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
            CommandLine cmdLine = null;
            String currentEtlArgs = getEtlTimeArgs(jobConfig.getSyncTimePoint());
            try {
                ByteArrayOutputStream stdout = new ByteArrayOutputStream();
                PumpStreamHandler psh = new PumpStreamHandler(stdout);
                cmdLine = new CommandLine("python");
                cmdLine.addArgument(getDataXPyPath());
                cmdLine.addArgument(tmpFilePath);
                if(StringUtils.isNotEmpty(currentEtlArgs)) {
                    cmdLine.addArgument("-p");
                    cmdLine.addArgument(currentEtlArgs, false);
                }
                DefaultExecutor exec = new DefaultExecutor();
                exec.setStreamHandler(psh);
                int executeReturnCode = exec.execute(cmdLine);
                log.info("执行命令:{},执行结果:{}",cmdLine.toString(),executeReturnCode);
                JobLogFileAppender.appendLog(logFilePath, stdout.toString());
            } catch (Exception e) {
                String cmdLineString =  cmdLine!=null?cmdLine.toString() : "";
                JobLogFileAppender.appendLog(logFilePath, cmdLineString + "\n");
                JobLogFileAppender.appendLog(logFilePath, e.getMessage());
                log.error("job 执行异常：cmdLine:{}", cmdLineString, e);
            }
            if(StringUtils.isNotEmpty(currentEtlArgs)) {
                jobConfig.setSyncTimePoint(getEtlTime(jobConfig.getSyncTimePoint()));
                dataxJobConfigService.updateById(jobConfig);
            }
            //  删除临时文件
            FileUtil.del(new File(tmpFilePath));

        });
        return "success";
    }

    private String getDataXPyPath() {
        String dataxPyPath;
        String dataXHome = System.getenv("DATAX_HOME");
        if (StringUtils.isBlank(dataXHome)) {
            dataXHome = System.getProperty("DATAX_HOME");
        }
        if (StringUtils.isBlank(dataXHome)) {
            log.error("DATAX_HOME 环境变量为NULL");
        }
        String osName = System.getProperty("os.name");
        dataXHome = osName.contains("Windows") ? (!dataXHome.endsWith("\\") ? dataXHome.concat("\\") : dataXHome) : (!dataXHome.endsWith("/") ? dataXHome.concat("/") : dataXHome);
        dataxPyPath = dataXHome + "bin/datax.py";
        return dataxPyPath;
    }

    @Override
    public String startJobLog(RunJobDto runJobDto) {
        String logFilePath;
        //取出 jobJson，并转为json对象
        JSONObject json = JSONObject.parseObject(runJobDto.getJobJson());
        //根据jobId和当前时间戳生成日志文件名
        String logFileName = runJobDto.getJobConfigId().toString().concat("_").concat(StrUtil.toString(System.currentTimeMillis()).concat(".log"));
        logFilePath = etlLogDir.concat(logFileName);
        //记录日志
        JobLog jobLog = new JobLog();
        jobLog.setJobId(runJobDto.getJobConfigId());
        jobLog.setLogFilePath(logFilePath);
        dataxJobLogService.save(jobLog);
        //启动任务
        return startJobByJsonStr(logFilePath, runJobDto.getJobConfigId(), JSON.toJSONString(json));
    }



    private String getEtlTimeArgs(String lastEtlTime) {
        String args = "-Dcreate_time='%s' -Dend_time='%s'";
        if(StringUtils.isNotEmpty(lastEtlTime)) {
            String currentEtlTimeStr = getEtlTime(lastEtlTime);
            return String.format(args, currentEtlTimeStr, getEtlTime(currentEtlTimeStr));
        }
        return "";
    }

    private String getEtlTime(String lastEtlTime) {

        if(StringUtils.isNotEmpty(lastEtlTime)) {
            DateTime dateTime = DateUtil.parse(lastEtlTime, DatePattern.PURE_DATE_PATTERN);
            DateTime currentEtlTime = dateTime.setField(DateField.DAY_OF_MONTH, dateTime.dayOfMonth()+1);
            String currentEtlTimeStr = DateUtil.format(currentEtlTime, DatePattern.PURE_DATE_PATTERN);
            return currentEtlTimeStr;
        }
        return "";
    }

}
