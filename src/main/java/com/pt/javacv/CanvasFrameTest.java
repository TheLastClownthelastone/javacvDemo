package com.pt.javacv;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.junit.jupiter.api.Test;

import javax.swing.*;

/**
 * @author nate-pt
 * @date 2021/7/19 9:57
 * @Since 1.8
 * @Description 调用摄像头
 */
public class CanvasFrameTest {

    @Test
    public void exec1() throws Exception{
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
}
