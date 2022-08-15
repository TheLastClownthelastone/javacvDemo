package com.pt.javacv;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.*;

import javax.swing.*;

/**
 * @author nate-pt
 * @date 2021/7/21 15:22
 * @Since 1.8
 * @Description
 */
public class ZbLl {

    public static void main(String[] args) throws Exception{

        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("rtmp://192.168.5.141:9004/live");
        grabber.start();


        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("zb.flv", grabber.getImageWidth(),grabber.getImageHeight());
        recorder.setFormat("flv");
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setFrameRate(25);

        recorder.start();

        CanvasFrame canvasFrame = new CanvasFrame("观看直播");
        canvasFrame.setAlwaysOnTop(true);
        canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Frame frame = null;
        long stratTime = 0;
        long videoT = 0;
        while (canvasFrame.isVisible() && (frame = grabber.grabImage()) != null) {
            canvasFrame.showImage(frame);
            recorder.record(frame);
            if(stratTime == 0){
                stratTime =System.currentTimeMillis();
            }
            videoT = 1000*(System.currentTimeMillis() -stratTime);
            recorder.setTimestamp(videoT);
            // 解析的时候添加线程延迟
        }
        canvasFrame.dispose();

        recorder.close();

        grabber.close();
    }
}
