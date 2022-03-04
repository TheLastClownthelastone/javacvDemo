package com.pt.javacv;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author nate-pt
 * @date 2021/9/17 16:40
 * @Since 1.8
 * @Description
 */
public class Test {
    public static void main(String[] args) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();

        System.out.println("当前时间是：" + dateFormat.format(date));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // 设置为当前时间
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
        date = calendar.getTime();

        System.out.println("上一个月的时间： " + dateFormat.format(date));
    }

    @org.junit.jupiter.api.Test
    public void test1(){
        String str = "360300000000";
        String substring = str.substring(4, str.length());
        System.out.println(substring);
    }
}
