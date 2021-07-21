package com.pt.javacv;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.*;

import javax.swing.*;

/**
 * @author nate-pt
 * @date 2021/7/20 9:39
 * @Since 1.8
 * @Description 调用本地摄像头推流
 */
public class FrameGrabberTest {

    /**
     * 按帧录制本地摄像头进行推流
     *
     * @param outPutFile 录制文件的路径
     * @param frameRate  视频帧率
     */
    public static void recordCamera(String outPutFile, double frameRate) throws Exception {
        // 另一种获取摄像头的方式，FrameGrabber会自己寻找可以打开的摄像头的抓取器
        FrameGrabber frameGrabber = FrameGrabber.createDefault(0);
        // 开启抓取器
        frameGrabber.start();

        // 创建转换器
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        // 抓取视频的每一帧作为图像
        opencv_core.IplImage grabberImage = converter.convert(frameGrabber.grab());
        // 获取每一帧图像的宽高
        int width = grabberImage.width();
        int height = grabberImage.height();

        // 创建录制器
        FrameRecorder recorder = FrameRecorder.createDefault(outPutFile, width, height);
        // 设置编码
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        // 设置封装格式,如果推送到rtmp就必须是flv封装格式
        recorder.setFormat("flv");
        // 设置帧率
        recorder.setFrameRate(frameRate);

        // 开启录制器
        recorder.start();

        long startTime = 0;
        long videoTS = 0;

        // 创建窗口
        CanvasFrame frame = new CanvasFrame("camera", CanvasFrame.getDefaultGamma() / frameGrabber.getGamma());
        // 关闭窗口的时候程序关闭
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        // 需要转换才能推送到rtmp上
        Frame rotatedFrame = converter.convert(grabberImage);

        while ((frame.isVisible()) && (grabberImage = converter.convert(frameGrabber.grab())) != null) {

            rotatedFrame = converter.convert(grabberImage);
            frame.showImage(rotatedFrame);
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }

            videoTS = 1000 * (System.currentTimeMillis() - startTime);
            recorder.setTimestamp(videoTS);
            recorder.record(rotatedFrame);
            Thread.sleep(40);
        }
        // 关闭窗口
        frame.dispose();
        // 关闭推流控制器
        recorder.close();
        // 关闭抓取器
        frameGrabber.close();

    }

    public static void main(String[] args) throws Exception {
        recordCamera("1.mp4",25);
    }
}
