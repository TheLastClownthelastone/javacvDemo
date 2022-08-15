package com.pt.javacv;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import javax.swing.*;

/**
 * @author nate-pt
 * @date 2022/8/15 14:39
 * @Since 1.8
 * @Description 本地摄像头
 */
public class NativeCameraDemo {

    public static void main(String[] args) throws Exception{
        // 使用opencv 的帧抓取器方式 一般电脑和移动设备的帧抓取器的默认序号为0
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        // 开启摄像头抓取
        grabber.start();

        // 创建一个预览窗口将抓到的数据推动预览窗口上
        CanvasFrame canvasFrame = new CanvasFrame("本地摄像头窗口");
        // 关闭窗口停止抓流
        canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 判断窗口是否关闭
        while (canvasFrame.isDisplayable()) {
            canvasFrame.showImage(grabber.grab());
        }
        // 关闭抓取器 释放资源
        grabber.close();;

    }


}
