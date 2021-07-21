package com.pt.javacv;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.junit.jupiter.api.Test;

/**
 * @author nate-pt
 * @date 2021/7/19 14:15
 * @Since 1.8
 * @Description
 */
public class FFmpegFrameGrabberTest {

    /**
     * 拉流并且解码
     */
    @Test
    public void exec1() throws Exception {
        String url = "E:\\BaiduNetdiskDownload\\Kenny G - Going home.mp3";
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(url);
        // 进行初始化，网络不好的时候可能会阻塞，会读取音视频分析得到音视频编码信息等。
        grabber.start();

//        for(;;){
//            // 获取到每一帧
//            Frame grab = grabber.grab();
//            System.out.println(grab);
//        }
        // 判断是否是音频流
        System.out.println(grabber.hasAudio());
        double aspectRatio = grabber.getAspectRatio();
        System.out.println(aspectRatio);
        // 获取音频道数量
        int audioChannels = grabber.getAudioChannels();
        System.out.println(audioChannels);

        // 获取每秒音频帧数的估计
        double audioFrameRate = grabber.getAudioFrameRate();
        System.out.println(audioFrameRate);
        // 获取平均比特率
        int audioBitrate = grabber.getAudioBitrate();
        System.out.println(audioBitrate);

        int audioCodec = grabber.getAudioCodec();
        System.out.println(audioCodec);

    }

    /**
     * 按帧录制视频
     *
     * @param inputFile
     * @param outputFile
     * @param audioChannel
     */
    public static void frameRecord(String inputFile, String outputFile, int audioChannel) throws Exception {

        // 设置全局变量用来控制录播是否结束
        boolean isStart = true;

        // 获取视频源
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputFile);
        // 流媒体输出地址
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputFile, 1280, 720, audioChannel);
        // 开始获取视频流
        recordByFrame(grabber, recorder, isStart);
    }

    private static void recordByFrame(FFmpegFrameGrabber grabber, FFmpegFrameRecorder recorder, boolean isStart) throws Exception {
        try {
            grabber.start();
            recorder.start();
            Frame frame = null;
            while (isStart && (frame = grabber.grabFrame()) != null) {
                recorder.record(frame);
            }
            recorder.stop();
            grabber.stop();
        } finally {
            if (grabber != null) {
                grabber.stop();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String inputFile = "rtsp://admin:admin@192.168.2.236:37779/cam/realmonitor?channel=1&subtype=0";
        // Decodes-encodes
        String outputFile = "recorde.mp4";
        frameRecord(inputFile, outputFile,1);
    }

}
