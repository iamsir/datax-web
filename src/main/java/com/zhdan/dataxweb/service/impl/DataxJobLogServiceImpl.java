package com.zhdan.dataxweb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhdan.dataxweb.dao.JobLogMapper;
import com.zhdan.dataxweb.entity.JobLog;
import com.zhdan.dataxweb.service.DataxJobLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 日志记录表服务实现类
 *
 * @author dongan.zhang
 */
@Service
@Transactional(readOnly = true)
public class DataxJobLogServiceImpl extends ServiceImpl<JobLogMapper, JobLog> implements DataxJobLogService {

}