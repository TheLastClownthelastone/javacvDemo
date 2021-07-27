package com.pt.javacv;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;

import javax.swing.*;

/**
 * @author nate-pt
 * @date 2021/7/27 14:47
 * @Since 1.8
 * @Description 抓取电脑桌面
 */
public class Zhuomian {
    public static void main(String[] args) throws Exception{
        // 读取屏幕
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("desktop");
        // 设置输入格式
        grabber.setFormat("gdigrab");
        // 设置帧率
        grabber.setOption("framerate","60");
        // 设置截屏起点X
        grabber.setOption("offset_x","100");
        // 设置截屏起点y
        grabber.setOption("offset_y","100");
        // 设置截取屏幕宽度
        grabber.setImageWidth(800);
        // 设置截取屏幕高度
        grabber.setImageHeight(600);

        // 设置鼠标隐藏
        grabber.setOption("draw_mouse","0");

        grabber.start();

        CanvasFrame canvasFrame = new CanvasFrame("桌面直播");
        canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvasFrame.setAlwaysOnTop(true);


        long timeDump = System.currentTimeMillis();
        long lastTimeDump;
        Frame frame = null;
        // 抓取屏幕
        for (int i = 0;(frame=grabber.grab())!=null;i++){
            //显示画面
            canvasFrame.showImage(frame);


            lastTimeDump = System.currentTimeMillis();
            // 录屏超过了十秒退出并且计算平均帧率
            if ((lastTimeDump-timeDump)>10000) {
                long inteval= ((lastTimeDump-timeDump)/ 1000);
                System.out.println("总耗时："+inteval+"秒，总帧数："+i+"，平均帧率："+(i/inteval));
            }

        }

        grabber.stop();
        canvasFrame.dispose();

    }
}
