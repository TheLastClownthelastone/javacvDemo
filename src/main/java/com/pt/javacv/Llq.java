package com.pt.javacv;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;

/**
 * @author nate-pt
 * @date 2021/7/20 14:50
 * @Since 1.8
 * @Description 拉流器的实现
 */
public class Llq {

    public static void main(String[] args) throws Exception {
        // 从斗鱼平台从
        String inputFile = "http://192.168.5.142:8002/hls/d040f700f3db45fab04adaf1240012dd_1080/index.m3u8";
        String outputFile = "3.flv";
        // 抓取器，设置文件来源
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputFile);
        // 转化器(最后一个参数表示是否录制 0否 1 是)
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputFile, 1280, 720, 1);

        grabber.start();

        recorder.start();
        // 申明帧变量
        Frame frame = null;
        while ((frame = grabber.grabFrame()) != null){
            // 将每一帧写入到输出的文件中
            recorder.record(frame);
        }

        recorder.stop();
        grabber.stop();



    }

}
