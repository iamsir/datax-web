package com.test;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.exec.CommandLine;
import org.junit.Test;

import java.util.Date;

/**
 * @author dongan.zhang
 **/
public class GeneralTest {

    @Test
    public void test01() {

        System.out.println(DateUtil.format(new Date(), "yyyyMMddHHmmss"));

    }

    @Test
    public void test02() {
        String d = "20191017160156";
        System.out.println(DateUtil.parse(d));

    }

    @Test
    public void test03() {
        String args = "-Dcreate_time='%s' -Dend_time='%s'";
        String d = "2019101701";
        DateTime dateTime = DateUtil.parse(d, DatePattern.PURE_DATE_PATTERN);
        System.out.println("当前：" + dateTime);

    }

    @Test
    public void test04() {
        String args = "-Dcreate_time=%s -Dend_time=%s";
        String d = "20191017";
        DateTime dateTime = DateUtil.parse(d, DatePattern.PURE_DATE_PATTERN);
        System.out.println("当前：" + dateTime);
        DateTime d1 = dateTime.setField(DateField.DAY_OF_MONTH, dateTime.dayOfMonth()+1);
        String d1Str = DateUtil.format(d1, DatePattern.PURE_DATE_PATTERN);
        System.out.println("之后：" + d1Str);
        String params = String.format(args, d1Str, d1Str);
        System.out.println(params);
        CommandLine cmdLine = new CommandLine("python");
        cmdLine.addArgument(params, false);
        System.out.println("cmdLine:" + cmdLine.toString());

    }
}
