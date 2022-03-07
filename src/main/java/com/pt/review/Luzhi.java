package com.pt.review;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nate-pt
 * @date 2022/3/4 11:12
 * @Since 1.8
 * @Description 开启本地摄像机并且录制视屏储存到本地
 */
public class Luzhi {

    public static void main(String[] args) throws Exception {
        // 创建抓取器
        FrameGrabber grabber = FrameGrabber.createDefault(0);//本机摄像头默认0
        // 开启抓取器
        grabber.start();


        // 创建转换器
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        // 将抓取器抓取的每一帧抓换成图片
        opencv_core.IplImage iplImage = converter.convert(grabber.grab());
        // 获取抓取图片的宽和高用来整个视屏的宽高和窗口的宽高
        int width = iplImage.width();
        int height = iplImage.height();

        // 创建解析器
        FrameRecorder recorder = FrameRecorder.createDefault("5.mp4",width,height);
        // 设置视频编码
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        // 设置视频的封装格式 flv 为流媒体格式
        recorder.setFormat("flv");
        // 设置视频帧率
        recorder.setFrameRate(25);


        // 开启录制器
        recorder.start();
        long startTime=0;
        long videoTS=0;
        // 设置读取抓取器的窗口
        CanvasFrame frame = new CanvasFrame("视频抓取器",CanvasFrame.getDefaultGamma()/grabber.getGamma());
        // 关闭窗口关闭程序
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 设置窗口顶置
        frame.setAlwaysOnTop(true);
        // 读取一帧
        Frame rotatedFrame=converter.convert(iplImage);
        // 窗口不关闭 或者读取到最后一帧
        while (frame.isVisible()&&(iplImage=converter.convert(grabber.grab()))!=null){
            // 抓取每一帧画面
            rotatedFrame = converter.convert(iplImage);
            frame.showImage(rotatedFrame);
            // 设置视频每一帧对应的时间
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }
            videoTS = 1000*(System.currentTimeMillis()-startTime);
            recorder.setTimestamp(videoTS);
            recorder.record(rotatedFrame);
            // 避免卡顿休息个40毫秒
            Thread.sleep(40);
        }

        frame.dispose();//关闭窗口
        recorder.close();//关闭推流录制器，close包含release和stop操作
        grabber.close();//关闭抓取器
        List<String> list = new ArrayList<>();

        list.add(2,"3");

    }

}
