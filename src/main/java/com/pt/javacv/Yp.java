package com.pt.javacv;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FrameRecorder;

import javax.sound.sampled.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author nate-pt
 * @date 2021/7/27 14:22
 * @Since 1.8
 * @Description 调用本地音频设备
 */
public class Yp {

    public static void main(String[] args) throws Exception{
        /**
         * 设置音频编码器
         */
        AudioFormat audioFormat = new AudioFormat(44100.0F, 16, 2, true, false);
        System.out.println("准备开启音频！");
        // 获取本地音频混合器基本信息
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
        //获取本地音频混合器
        Mixer mixer = AudioSystem.getMixer(mixerInfo[4]);
        // 通过编码器获取数据线信息
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);

        FFmpegFrameRecorder recorder = FFmpegFrameRecorder.createDefault("Yp.mp4", 400,400);

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

        // 开始抓取音频
        try {
            TargetDataLine line = (TargetDataLine) mixer.getLine(info);
            line.open(audioFormat);
            line.start();

            // 获取当前音频的采样率
            int sampleRate = (int) audioFormat.getSampleRate();
            // 获取音频通道数量
            int channels = audioFormat.getChannels();
            // 初始化音频缓冲区
            int audioBufferSize = sampleRate * channels;
            byte[] audioBytes = new byte[audioBufferSize];

            // 在当前线程中创建定时任务
            ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
            // 没过一帧执行一次
            exec.scheduleAtFixedRate(() -> {
                // 非阻塞的方式进行读取
                int read = line.read(audioBytes, 0, line.available());
                // 设置的16位的音频格式，将byte[] 转成short[]
                int sampleRead = read / 2;
                short[] sample = new short[sampleRead];
                ByteBuffer.wrap(audioBytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(sample);

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


    }
}
