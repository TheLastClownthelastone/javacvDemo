package com.pt.javacv;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author nate-pt
 * @date 2021/7/26 17:49
 * @Since 1.8
 * @Description
 */
public class Youhua {

    public static void main(String[] args) throws Exception {
        FrameGrabber grabber = FrameGrabber.createDefault(0);
        grabber.start();


        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage image = converter.convert(grabber.grab());


        CanvasFrame canvasFrame = new CanvasFrame("视频聊天");
        canvasFrame.setBounds(300, 100, 400, 400);
        canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvasFrame.setResizable(false);

        ChatPanel chatPanel = new ChatPanel();
        chatPanel.setBounds(295, 272, 100, 100);
        canvasFrame.add(chatPanel);


        ChatPanel chatPanel1 = new ChatPanel();
        canvasFrame.add(chatPanel1);


        while ((image = converter.convert(grabber.grab())) != null) {
            chatPanel.setImage(image);
            // 进行重新绘画，不然程序会卡死在这个位置
            chatPanel.repaint();
        }

        canvasFrame.dispose();
        grabber.close();
    }
}
