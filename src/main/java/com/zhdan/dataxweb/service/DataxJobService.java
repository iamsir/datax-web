package com.zhdan.dataxweb.service;


import com.zhdan.dataxweb.dto.RunJobDto;

/**
 * @author dongan.zhang

 */
public interface DataxJobService {
  
    /**
     * 根据json字符串用线程池启动一个datax作业
     * @param logFilePath
     * @param jobId
     * @param jobJson
     * @return
     */
    String startJobByJsonStr(String logFilePath, long jobId, String jobJson);

    /**
     * 启动任务，并记录日志
     * @param runJobDto
     * @return
     */
    String startJobLog(RunJobDto runJobDto);
}
