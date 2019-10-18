package com.zhdan.dataxweb.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用于启动任务接收的实体
 * @author dongan.zhang
 */
@Data
public class RunJobDto implements Serializable {

    private String jobJson;

    private Long jobConfigId;
}
