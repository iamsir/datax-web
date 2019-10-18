package com.test;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhdan.dataxweb.DataxWebApplication;
import com.zhdan.dataxweb.entity.JobConfig;
import com.zhdan.dataxweb.service.DataxJobConfigService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * @author dongan.zhang
 * @create 2019-10-14 14:29
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes=DataxWebApplication.class)
public class DataxWebApplicationTest {

    @Autowired
    private DataxJobConfigService dataxJobConfigService;

    @Test
    public void jobConfigListTest() {
        System.out.println(dataxJobConfigService.list());
    }

    @Test
    public void jobConfigListByPagingTest() {
        Page<JobConfig> page = new Page<>(1, 1);
        QueryWrapper<JobConfig> wrapper = new QueryWrapper<>();
        wrapper.lambda().orderByAsc(JobConfig::getCreateDate);
        IPage<JobConfig> result = dataxJobConfigService.page(page, wrapper);
        System.out.println(result.getRecords());

        String json = JSON.toJSONString(result.getRecords());
        System.out.println(json);
    }

    @Test
    public void tests1() {
        System.out.println("----- baseMapper 自带分页 ------");
        Page<JobConfig> page = new Page<>(1, 1);
        IPage<JobConfig> JobConfigIPage = dataxJobConfigService.page(page, new QueryWrapper<JobConfig>()
                .orderByDesc("create_date"));
        assertThat(page).isSameAs(JobConfigIPage);
        System.out.println("总条数 ------> " + JobConfigIPage.getTotal());
        System.out.println("当前页数 ------> " + JobConfigIPage.getCurrent());
        System.out.println("当前每页显示数 ------> " + JobConfigIPage.getSize());
        print(JobConfigIPage.getRecords());
        System.out.println("----- baseMapper 自带分页 ------");

        System.out.println("json 正反序列化 begin");
        String json = JSON.toJSONString(page);
        Page<JobConfig> page1 = JSON.parseObject(json, Page.class);
        print(page1.getRecords());
        System.out.println("json 正反序列化 end");

    }

    private <T> void print(List<T> list) {
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(System.out::println);
        }
    }
}
