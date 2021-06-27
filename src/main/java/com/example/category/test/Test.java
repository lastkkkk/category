package com.example.category.test;

import com.example.category.com.ucpaas.restDemo.client.JsonReqClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Test {


    public static void main(String[] args) {
        String sid = "";//用户的账号唯一标识“Account Sid”
        String token = "";//用户密钥“Auth Token”
        String appid = "";//创建应用时系统分配的唯一标示
        String templateid = "";//可在后台短信产品→选择接入的应用→短信模板-模板ID，查看该模板ID
        String param = generateWord();//模板中的替换参数（验证码）
        String mobile = "";//接收的单个手机号，暂仅支持国内号码
        String uid = "";//用户透传ID，随状态报告返回

        try {
            String result=new JsonReqClient().sendSms(sid, token, appid, templateid, param, mobile, uid);
            System.out.println("Response content is: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 产生随机的6位数字字符串
     */
    private static String generateWord() {
        int length = 6;
        String[] beforeShuffle = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        List list = Arrays.asList(beforeShuffle);
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
        }
        String afterShuffle = sb.toString();
        return afterShuffle.substring(2, 2 + length);
    }
}
