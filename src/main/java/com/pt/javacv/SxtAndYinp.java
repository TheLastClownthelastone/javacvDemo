package com.pt.javacv;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.*;

import javax.sound.sampled.*;
import javax.swing.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author nate-pt
 * @date 2021/7/21 9:48
 * @Since 1.8
 * @Description 将摄像头和音频推流生成本地文件
 */
public class SxtAndYinp {

    public static void main(String[] args) throws Exception {

        // 开启摄像头抓取器,0表示摄像头
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        // 开始抓取
        grabber.start();
        //将帧解析为图像，获取对应的宽高
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        opencv_core.IplImage image = converter.convert(grabber.grab());

        // 文件解析器
        FFmpegFrameRecorder recorder = FFmpegFrameRecorder.createDefault("SxtAndYinp.mp4", image.width(), image.height());
        recorder.setInterleaved(true);

        // 降低延迟
        recorder.setVideoOption("tune", "zerolatency");
        // 权衡视屏的质量
        recorder.setVideoOption("preset", "ultrafast");
        // 转流
        recorder.setVideoOption("crf", "25");
        // 设置视屏的比特率
        recorder.setVideoBitrate(2000000);
        // 设置视频的编码
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        // 设置视频的封装格式
        recorder.setFormat("flv");
        // 设置视频的帧率
        recorder.setFrameRate(25);
        // 设置关键帧间隙
        recorder.setGopSize(50);
        // 设置音频固定比特率
        recorder.setAudioOption("crf", "0");
        // 最高质量
        recorder.setAudioQuality(0);
        // 设置音频比特率
        recorder.setVideoBitrate(192000);
        // 音频采样率
        recorder.setSampleRate(44100);
        // 设置双声道（立体音）,0 左声道 1 右声道 2双声道
        recorder.setAudioChannels(2);

        // 开始录制
        recorder.start();

        // 音频获取在另外线程中执行
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ": 执行中。。。。。。。。。。。");

            /**
             * 设置音频编码器
             * 采样率44.1k 采样率位数16位
             */
            AudioFormat audioFormat = new  AudioFormat(44100.0F, 16, 2, true, false);

            // 获取音频混合音信息
            Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
            // 获取本地音频混合器 ，音频视频设备为4
            Mixer mixer = AudioSystem.getMixer(mixerInfo[4]);
            System.out.println("音频线路为："+mixer.getMixerInfo().getName());
            // 通过音频编辑器获取数据线信息
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);

            // 开始捕获音频
            try {
                TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(audioFormat);
                line.start();

                // 获取音频采集率
                int sampleRate = (int) audioFormat.getSampleRate();
                // 获取音频通道数量
                int channels = audioFormat.getChannels();
                // 初始化音频通道缓冲区（通道数量*采集率）
                int audioBufferSize = sampleRate * channels;
                byte[] bytes = new byte[audioBufferSize];

                // 在当前线程中创建定时任务
                ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
                // 没过一帧执行一次
                exec.scheduleAtFixedRate(() -> {
                    // 非阻塞的方式进行读取
                    int read = line.read(bytes, 0, line.available());
                    // 设置的16位的音频格式，将byte[] 转成short[]
                    int sampleRead = read / 2;
                    short[] sample = new short[sampleRead];
                    ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(sample);

                    // 将short 字节数组转入shortBuffer中
                    ShortBuffer sBuffer = ShortBuffer.wrap(sample, 0, sampleRead);
                    // 按通道录制shortBuffer
                    try {
                        recorder.recordSamples(sampleRate, channels, sBuffer);
                    } catch (FrameRecorder.Exception e) {
                        e.printStackTrace();
                    }


                }, 0, (long) 1000 / 25, TimeUnit.MICROSECONDS);


            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }

        }, "音频线程").start();


        // 获取摄像头
        CanvasFrame cFrme = new CanvasFrame("摄像头音频录制", CanvasFrame.getDefaultGamma() / grabber.getGamma());
        cFrme.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cFrme.setAlwaysOnTop(true);
        Frame frame = null;
        long startTime = 0;
        long videoTime = 0;
        while (cFrme.isVisible() && (frame = grabber.grab()) != null) {
            cFrme.showImage(frame);
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }
            // 写入帧中记录时间
            videoTime = 1000*(System.currentTimeMillis() - startTime);
            recorder.setTimestamp(videoTime);
            recorder.record(frame);
        }

        // 关闭窗口
        cFrme.dispose();
        // 关闭解析器
        recorder.close();
        // 关闭抓取器
        grabber.close();


    }

}
