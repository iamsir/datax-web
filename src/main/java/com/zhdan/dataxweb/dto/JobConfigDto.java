package com.zhdan.dataxweb.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author dongan.zhang
 **/
@Data
public class JobConfigDto {

    /**
     * 作业名
     */
    @ApiModelProperty(value = "作业名")
    private String jobName;

    /**
     * 分组
     */
    @ApiModelProperty(value = "分组")
    private String jobGroup;

    /**
     * cron表达式
     */
    @ApiModelProperty(value = "cron表达式")
    private String cronExpression;

    /**
     *
     */
    @ApiModelProperty(value = "")
    private String configJson;

    /**
     * 作业描述信息
     */
    @ApiModelProperty(value = "作业描述信息")
    private String description;

    /**
     * 已经同步到的时间点(yyyyMMddHHmmss),
     */
    @ApiModelProperty(value = "已经同步到的时间点(yyyyMMddHHmmss)")
    private String syncTimePoint;


}
