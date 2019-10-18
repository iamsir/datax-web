package com.zhdan.dataxweb.util;

import com.alibaba.fastjson.JSONObject;

/**
 * JSON工具类
 *
 * @author dongan.zhang
 */
public class JsonUtil {

    /**
     * 判断字符串是否为json格式
     *
     * @param jsonStr
     * @return
     */
    public static boolean isJson(String jsonStr) {
        try {
            JSONObject.parseObject(jsonStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
