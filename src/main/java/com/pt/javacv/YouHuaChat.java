package com.pt.javacv;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.image.BufferedImage;

/**
 * @author nate-pt
 * @date 2021/7/28 16:30
 * @Since 1.8
 * @Description 优化小窗口聊天功能
 */
public class YouHuaChat {

    public static void main(String[] args)  throws Exception{
        // 抓取摄像头
        FrameGrabber grabber = FrameGrabber.createDefault(0);
        grabber.start();

        // 抓取文件流
        FFmpegFrameGrabber grabber1 = new FFmpegFrameGrabber("rtmp://192.168.11.123/live/chat");
        grabber1.start();
        Java2DFrameConverter converter = new Java2DFrameConverter();


        CanvasFrame canvasFrame = new CanvasFrame("视频聊天");
        canvasFrame.setBounds(300, 100, 400, 400);
        //canvasFrame.setLayout(null);

        ChatPanel chatPanel = new ChatPanel();
        chatPanel.setBounds(295, 272, 100, 100);
        canvasFrame.add(chatPanel,1);


        ChatPanel chatPanel1 = new ChatPanel();
        canvasFrame.add(chatPanel1,2);


        BufferedImage convert = null;
        BufferedImage convert1 = null;
        // 因为这里使用的是java2d抓取所以调用grabImage
        while ((convert = converter.convert(grabber1.grabImage())) != null && (convert1 = converter.convert(grabber.grab())) != null) {
            chatPanel1.setImage(convert1);
            chatPanel.setImage(convert);
            chatPanel1.repaint();
        }


    }
}
