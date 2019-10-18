package com.zhdan.dataxweb.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 作业配置表实体类(job_config)
 *
 * @author dongan.zhang
 */
@Data
@ApiModel
@TableName("job_config")
public class JobConfig extends Model<JobConfig> implements Serializable {

    /**
     *
     */
    @TableId
    @ApiModelProperty(value = "")
    private Integer id;

    /**
     *
     */
    @ApiModelProperty(value = "")
    private Integer userId;

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

    /**
     *
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
     *
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "", hidden = true)
    private Date updateDate;


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