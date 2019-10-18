package com.zhdan.dataxweb.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 抽取日志记录表实体类(job_log)
 *
 * @author dongan.zhang
 */

@Data
@ApiModel
@TableName("job_log")
public class JobLog extends Model<JobLog> {

    /**
     *
     */
    @TableId
    @ApiModelProperty(value = "")
    private Long id;

    /**
     * 抽取任务，主键ID
     */
    @ApiModelProperty(value = "抽取任务，主键ID")
    private Long jobId;

    /**
     * 日志文件路径
     */
    @ApiModelProperty(value = "日志文件路径")
    private String logFilePath;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "", hidden = true)
    private Date updateDate;

    /**
     * 2
     */
    @TableLogic
    @ApiModelProperty(value = "", hidden = true)
    private Integer status;

    /**
     *
     */
    @ApiModelProperty(value = "", hidden = true)
    private Integer createBy;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "", hidden = true)
    private Date createDate;

    /**
     *
     */
    @ApiModelProperty(value = "", hidden = true)
    private Integer updateBy;


    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}