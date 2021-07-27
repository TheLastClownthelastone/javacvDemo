package com.pt.javacv;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;

import javax.swing.*;

/**
 * @author  nate-pt
 * @date  2021/7/27 15:10
 * @Since 1.8
 * @Description  基于dshow 方式获取摄像头和麦克风音频
 */public class Dshow {

    public static void main(String[] args) throws Exception{
        //格式是“video=视频设备名称:audio=音频设备名称”，可以通过ffmpeg命令：ffmpeg -list_devices true -f dshow -i dummy 来获取dshow支持的设备列表

//如果屏幕设备兼容的话，可以使用new FFmpegFrameGrabber("screen-capture-recorder“)获取屏幕画面

        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("video=HD Camera");//获取摄像头画面
        grabber.setFormat("dshow");// 基于dshow的方式

        grabber.start();

        CanvasFrame canvas = new CanvasFrame("dshow预览摄像头");// 新建一个窗口
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas.setAlwaysOnTop(true);
        Frame frame = null;

        long timeDump = System.currentTimeMillis();// 记录一下时间，用来简单计算一下平均帧率
        long lastTimeDump;
        // 抓取屏幕画面
        for (int i = 0; (frame = grabber.grab()) != null; i++) {
            // 显示画面
            canvas.showImage(frame);

            lastTimeDump = System.currentTimeMillis();
            if (lastTimeDump - timeDump > 10000) {// 录屏10秒，超过10秒退出并计算平均帧率
                long inteval = ((lastTimeDump - timeDump) / 1000);
                System.out.println("总耗时：" + inteval + "秒，总帧数：" + i + ",平均帧率：" + i / inteval);
                break;
            }
        }
        grabber.stop();
        canvas.dispose();

    }
}
