package com.pt.javacv;

import org.bytedeco.javacv.*;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author nate-pt
 * @date 2021/7/28 9:29
 * @Since 1.8
 * @Description 实现自拍功能
 */
public class Zpai {

    public static void main(String[] args) throws Exception {

        // 调用摄像头
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        grabber.start();

        // 将每一帧转换成java2D图片
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage image = converter.convert(grabber.grabFrame());

        // 创建窗口
        CanvasFrame canvasFrame = new CanvasFrame("自拍");
        canvasFrame.setAlwaysOnTop(true);
        canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.out.println("窗口宽："+image.getWidth()+"，窗口高："+image.getHeight());
        // 设置窗口的尺寸
        canvasFrame.setCanvasSize(image.getWidth(), image.getHeight());
        // 实现定时器，在点击窗口的时候触发动画效果
        TimerAction action = new TimerAction(canvasFrame);
        Timer timer = new Timer(10, action);
        action.setTimer(timer);

        final BufferedImage saveImage = image;
        // 窗口添加鼠标点击事件
        canvasFrame.getCanvas().addMouseListener(new MouseAdapter() {
            // 当鼠标点击的时候触发
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("鼠标点击了进入点击事件中。。。。。。。");
                // 在鼠标点击的时候添加动画效果
                Graphics graphics = canvasFrame.getCanvas().getGraphics();
                long l = System.currentTimeMillis();
                int count = 5;
                int x = canvasFrame.getX();
                int y = canvasFrame.getY();
                //graphics.drawString("测试中",x+340,y+240);
                canvasFrame.repaint();
                timer.start();
                // 并且将图片进行保存
                try {
                    ImageIO.write(saveImage, "jpg", new File("zpz.jpg"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });


        while (canvasFrame.isVisible() && (image = converter.convert(grabber.grab())) != null) {
            if (!timer.isRunning()) {
                canvasFrame.showImage(image);
            }
        }
        grabber.close();
        canvasFrame.dispose();
    }

    /**
     * 增加定时事件监听器
     */
    static class TimerAction implements ActionListener {

        private CanvasFrame canvasFrame;

        private int width, height;

        private int count = 0;

        private Graphics2D g;

        private Timer timer;

        private int delta = 10;

        public TimerAction(CanvasFrame canvasFrame) {
            this.g = (Graphics2D) canvasFrame.getCanvas().getGraphics();
            this.canvasFrame = canvasFrame;
            this.width = canvasFrame.getCanvas().getWidth();
            this.height = canvasFrame.getCanvas().getHeight();
        }

        public void setTimer(Timer timer) {
            this.timer = timer;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int offset = delta * count;
            if (width - offset >= offset && height - offset >= offset) {
                g.setColor(Color.BLACK);
                g.drawRect(offset, offset, width - 2 * offset, height - 2 * offset);
                canvasFrame.repaint();
                System.out.println("进入定时器" + count + "次");
                System.out.println("宽："+width+",高："+height);
                count++;
            } else {
                //when animation is done, reset count and stop timer.
                timer.stop();
                count = 0;
            }
        }
    }
}
