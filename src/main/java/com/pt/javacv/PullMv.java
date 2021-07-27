package com.pt.javacv;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;

import javax.swing.*;

/**
 * @author nate-pt
 * @date 2021/7/27 9:59
 * @Since 1.8
 * @Description
 */
public class PullMv {
    public static void main(String[] args)  throws Exception{

        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("rtmp://192.168.11.123/live/chat");
        grabber.setFrameRate(25);
        grabber.setVideoBitrate(100);
        grabber.start();

        CanvasFrame canvasFrame = new CanvasFrame("观看");
        canvasFrame.setAlwaysOnTop(true);
        canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        new Thread(()->{
            Frame frame = null;
            try {
                while ((frame = grabber.grab()) != null) {
                    System.out.println(Thread.currentThread().getName());
                    canvasFrame.showImage(frame);
                }
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
        },"113232").start();

        while (true){

        }
    }
}
