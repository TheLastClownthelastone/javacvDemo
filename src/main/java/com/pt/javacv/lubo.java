package com.pt.javacv;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.*;

import javax.swing.*;

/**
 * @author nate-pt
 * @date 2021/7/20 11:44
 * @Since 1.8
 * @Description 使用javacv 实现录播将
 */
public class lubo {

    public static void main(String[] args) throws Exception {
        // 创建抓取器，使用本地摄像头
        FrameGrabber grabber = FrameGrabber.createDefault(0);
        // 开始抓取
        grabber.start();

        // 创建一个转换器
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        // 将抓取器抓取的每一帧转换成图像
        // grab() 抓取每一帧
        opencv_core.IplImage image = converter.convert(grabber.grab());

        // 获取图像的宽高
        int width = image.width();
        int height = image.height();

        // 将每一帧写入文件中
        FrameRecorder recorder = FrameRecorder.createDefault("outPut.mp4", width, height);
        // 设置视频编码
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        // 设置视频封装格式
        recorder.setFormat("flv");
        // 设置视频帧率
        recorder.setFrameRate(25);

        // 录制视频
        recorder.start();

        long startTime = 0;
        long videoTs = 0;

        // 创建窗口输出每一帧
        CanvasFrame canvasFrame = new CanvasFrame("录制视频", CanvasFrame.getDefaultGamma() / grabber.getGamma());
        // 设置窗口的关闭形式
        canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 将窗口显示
        canvasFrame.setAlwaysOnTop(true);
        // 进行转化(将图片放入窗口中)
        Frame convert = converter.convert(image);
        // 当窗口关闭，或者是抓取到最后一帧的时候结束循环
        while (canvasFrame.isVisible() && (image = converter.convert(grabber.grab())) != null){
            convert = converter.convert(image);
            // 在窗口中一直更换图片
            canvasFrame.showImage(convert);
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }
            // 设置录制时长
            videoTs = 1000 * (System.currentTimeMillis()-startTime);
            // 更新文件的录制时长
            recorder.setTimestamp(videoTs);
            // 往文件中插入新的帧
            recorder.record(convert);
            // 增加一下延迟
            Thread.sleep(40);
        }

        // 关闭窗口
        canvasFrame.dispose();
        // 关闭转化器
        recorder.close();
        // 关闭抓取器
        grabber.close();


    }


}
