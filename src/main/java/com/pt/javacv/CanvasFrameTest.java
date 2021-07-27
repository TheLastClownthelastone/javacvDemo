package com.pt.javacv;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author nate-pt
 * @date 2021/7/19 9:57
 * @Since 1.8
 * @Description 调用摄像头
 */
public class CanvasFrameTest {

    @Test
    public void exec1() throws Exception {
        // 新疆opencv 抓取器，一般电脑和移动设备摄像头序号为0
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        // 开始获取摄像头数据
        grabber.start();

        // 新疆一个摄像头预览窗口
        CanvasFrame canvasFrame = new CanvasFrame("摄像头预览");
        // 设置关闭程序为jframe关闭窗口形式
        canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // 判断窗口是否关闭
        while (canvasFrame.isDisplayable()) {
            // 将摄像头获取到的数据放入窗口中
            canvasFrame.showImage(grabber.grab());
        }
        // 停止抓取
        grabber.close();
    }

    /**
     * 在要执行的面板中添加小窗口
     */
    @Test
    public void exec2() throws IOException {
        CanvasFrame canvasFrame = new CanvasFrame("小窗口弹窗");


        BufferedImage image = ImageIO.read(new File("C:\\Users\\pt\\Pictures\\Camera Roll\\1.png"));
        canvasFrame.showImage(image);

        canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvasFrame.setAlwaysOnTop(true);
        JPanel jPanel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                try {
                    g.drawImage(ImageIO.read(new File("C:\\Users\\pt\\Pictures\\Camera Roll\\3.jpg")), 0, 0, this.getWidth(), this.getHeight(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        canvasFrame.add(jPanel);

    }

    @Test
    public void exec3() {
        JFrame jFrame = new CanvasFrame("主界面");
        jFrame.setBounds(300,100,400,400);
        JPanel jPanel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                try {
                    g.drawImage(ImageIO.read(new File("C:\\Users\\pt\\Pictures\\Camera Roll\\3.jpg")), 0, 0, this.getWidth(), this.getHeight(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        jPanel.setBounds(295,272,100,100);
        jFrame.add(jPanel);

        JPanel jPanel1 = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                try {
                    g.drawImage(ImageIO.read(new File("C:\\Users\\pt\\Pictures\\Camera Roll\\1.png")), 0, 0, this.getWidth(), this.getHeight(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        jFrame.add(jPanel1);




        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setAlwaysOnTop(true);
        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(false);


        while (true){

        }
    }

}
