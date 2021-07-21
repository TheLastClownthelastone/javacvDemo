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
public class ZbTl {

    public static void main(String[] args) throws Exception {
        FrameGrabber grabber =FrameGrabber.createDefault(0);
        grabber.start();

        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        opencv_core.IplImage image = converter.convert(grabber.grab());

        // 帧率
        int rate =25;
        FrameRecorder recorder = FrameRecorder.createDefault("rtmp://192.168.11.123/live/steams", image.width(), image.height());
        recorder.setFormat("flv");
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setFrameRate(rate);
        // 调整每一帧的延迟,设置每一帧的间隙
        recorder.setGopSize(rate*rate);

        recorder.start();

        CanvasFrame canvasFrame = new CanvasFrame("直播推流", CanvasFrame.getDefaultGamma() / grabber.getGamma());
        canvasFrame.setAlwaysOnTop(true);
        canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Frame frame = null;
        long stratTime = 0;
        long videoT = 0;
        while (canvasFrame.isVisible() && (image = converter.convert(grabber.grab())) != null) {
            canvasFrame.showImage(converter.convert(image));
            if(stratTime == 0){
                stratTime =System.currentTimeMillis();
            }
            videoT = 1000*(System.currentTimeMillis() -stratTime);
            recorder.setTimestamp(videoT);
            recorder.record(converter.convert(image));
            Thread.sleep(40);
        }

        canvasFrame.dispose();

        recorder.close();

        grabber.close();

    }
}
