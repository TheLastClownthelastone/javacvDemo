package com.pt.javacv;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.*;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * @author nate-pt
 * @date 2021/7/26 17:17
 * @Since 1.8
 * @Description
 */
public class MChat {
    public static void main(String[] args) throws Exception {
        // 抓取摄像头
        FrameGrabber grabber = FrameGrabber.createDefault(0);
        grabber.start();


        Java2DFrameConverter  converter = new Java2DFrameConverter();
        BufferedImage image = converter.convert(grabber.grab());
        // 向服务器推流
        FrameRecorder recorder = FrameRecorder.createDefault("rtmp://192.168.11.123/live/chat", image.getWidth(), image.getHeight());
        recorder.setFormat("flv");
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setFrameRate(25);
        recorder.start();

        CanvasFrame canvasFrame = new CanvasFrame("视频聊天");
        canvasFrame.setBounds(300, 100, 400, 400);
        canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvasFrame.setResizable(false);

        ChatPanel chatPanel = new ChatPanel();
        chatPanel.setBounds(295, 272, 100, 100);
        canvasFrame.add(chatPanel);


        ChatPanel chatPanel1 = new ChatPanel();
        canvasFrame.add(chatPanel1);


        // 开两个线程进行操作往两个窗口中添加输出帧
        new Thread(() -> {
            try {
                long stratTime = 0;
                long videoT = 0;
                BufferedImage convert = null;
                while ((convert = converter.convert(grabber.grab())) != null) {
                    chatPanel.setImage(convert);
                    chatPanel.repaint();
                    if(stratTime == 0){
                        stratTime =System.currentTimeMillis();
                    }
                    videoT = 1000*(System.currentTimeMillis() -stratTime);
                    recorder.setTimestamp(videoT);
                    recorder.record(converter.convert(convert));
                    Thread.sleep(10);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "本地摄像头").start();

        new Thread(() -> {
            try {
                BufferedImage convert = null;
                FFmpegFrameGrabber grabber1 = new FFmpegFrameGrabber("rtmp://192.168.11.123/live/mvChat");
                grabber1.start();
                long stratTime = 0;
                long videoT = 0;
                while ((convert = converter.convert(grabber1.grab())) != null) {
                    chatPanel1.setImage(convert);
                    if(stratTime == 0){
                        stratTime =System.currentTimeMillis();
                    }
                    videoT = 1000*(System.currentTimeMillis() -stratTime);
                    recorder.setTimestamp(videoT);
                    recorder.record(converter.convert(convert));
                    Thread.sleep(10);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }, "流媒体服务器").start();

        while (true){

        }
    }
}
