package com.pt.javacv;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.*;

import java.awt.image.BufferedImage;

/**
 * @author nate-pt
 * @date 2021/7/26 18:00
 * @Since 1.8
 * @Description
 */
public class PushMv {

    public static void main(String[] args) throws Exception {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("E:\\BaiduNetdiskDownload\\19第十九讲：图的搜索.avi");
        grabber.start();

        Java2DFrameConverter converter = new Java2DFrameConverter();

        FrameRecorder recorder = FrameRecorder.createDefault("rtmp://192.168.11.123/live/chat", 400, 400);
        recorder.setFrameRate(25);
        recorder.setFormat("flv");
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);

        recorder.start();

        long startTime = 0;
        long videT = 0;
        BufferedImage image = null;
        while ((image = converter.convert(grabber.grab())) != null) {
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }
            videT = 1000*(System.currentTimeMillis() -startTime);
            recorder.setTimestamp(videT);
            recorder.record(converter.convert(image));
        }

        System.out.println("流推送完毕~~~~~~~~~~~~~~~~~");
        recorder.close();
        grabber.close();
    }
}
