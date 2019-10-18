package com.test;

import com.zhdan.dataxweb.DataxWebApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author dongan.zhang
 * @create 2019-10-16 08:39
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataxWebApplication.class)
public class DatasourceTest {

    @Resource
    private DataSource dataSource;

    @Test
    public void dataSourceTest() {

        System.out.println(dataSource);

    }
}
