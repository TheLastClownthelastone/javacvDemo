package com.pt.javacv;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author nate-pt
 * @date 2021/7/26 16:35
 * @Since 1.8
 * @Description
 */
public class MvChat {

    public static void main(String[] args) throws Exception {


        CanvasFrame canvasFrame = new CanvasFrame("视频聊天");
        canvasFrame.setBounds(300, 100, 400, 400);
        //canvasFrame.setLayout(null);

        ChatPanel chatPanel = new ChatPanel();
        chatPanel.setBounds(295, 272, 100, 100);
        canvasFrame.add(chatPanel,1);


        ChatPanel chatPanel1 = new ChatPanel();
        canvasFrame.add(chatPanel1,2);


        // 开两个线程进行操作往两个窗口中添加输出帧
        new Thread(() -> {
            try {
                long stratTime = 0;
                long videoT = 0;

                // 抓取摄像头
                FrameGrabber grabber = FrameGrabber.createDefault(0);
                grabber.start();

                BufferedImage convert = null;


                Java2DFrameConverter  converter = new Java2DFrameConverter();
                BufferedImage image = converter.convert(grabber.grab());


                // 向服务器推流
                FrameRecorder recorder = FrameRecorder.createDefault("rtmp://192.168.11.123/live/mvChat", image.getWidth(), image.getHeight());
                recorder.setFormat("flv");
                recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
                recorder.setFrameRate(25);
                recorder.start();

                while ((convert = converter.convert(grabber.grab())) != null) {
                    chatPanel.setImage(convert);
                    chatPanel.repaint();
                    if(stratTime == 0){
                        stratTime =System.currentTimeMillis();
                    }
                    videoT = 1000*(System.currentTimeMillis() -stratTime);
                    recorder.setTimestamp(videoT);
                    recorder.record(converter.convert(convert));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "本地摄像头").start();

        new Thread(() -> {
            try {
                FFmpegFrameGrabber grabber1 = new FFmpegFrameGrabber("rtmp://192.168.11.123/live/chat");
                grabber1.start();

                Java2DFrameConverter  converter = new Java2DFrameConverter();
                BufferedImage convert = null;
                // 因为这里使用的是java2d抓取所以调用grabImage
                while ((convert = converter.convert(grabber1.grabImage())) != null) {
                    System.out.println("线程："+Thread.currentThread().getName()+"绘画中.......");
                    chatPanel1.setImage(convert);
                    chatPanel1.repaint();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }, "流媒体服务器").start();


        System.out.println("程序执行到这里来了。。。。。。。。");

        while (true){

        }


    }


}

class ChatPanel extends JPanel {
    Image image;


    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
