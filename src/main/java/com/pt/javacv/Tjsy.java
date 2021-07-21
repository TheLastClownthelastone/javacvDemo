package com.pt.javacv;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.*;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author nate-pt
 * @date 2021/7/20 16:17
 * @Since 1.8
 * @Description 在视屏中添加水印
 */
public class Tjsy {

//    public static void main(String[] args) throws Exception {
//        // 获取抓取器
//        OpenCVFrameGrabber  grabber = new OpenCVFrameGrabber(0);
//        // 开始抓取
//        grabber.start();
//
//        // 获取转化器(将视频的每一帧转成图像)
//        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
//        // 抓取到一帧
//        Frame grab = grabber.grab();
//
//        opencv_core.IplImage image = null;
//
//        if (grab != null) {
//            System.out.println("抓取到第一帧了、、");
//            image = converter.convert(grab);
//        } else {
//            System.out.println("没有取到第一帧");
//        }
//        // 将每一帧存入文件中
//        FrameRecorder recorder = FrameRecorder.createDefault("shuiyin.mp4", image.width(), image.height());
//
//        // 设置编码格式
//        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
//        // 设置封装形式
//        recorder.setFormat("flv");
//        // 设置帧率
//        recorder.setFrameRate(25);
//        recorder.setGopSize(25);
//
//        // 开始转化
//        recorder.start();
//
//
//        // 创建窗口
//        CanvasFrame canvasFrame = new CanvasFrame("添加水印", CanvasFrame.getDefaultGamma() / grabber.getGamma());
//
//        canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        canvasFrame.setAlwaysOnTop(true);
//// 水印文字位置
//        opencv_core.Point point1 = new opencv_core.Point(10, 50);
//        opencv_core.Point point2 = new opencv_core.Point(200, 200);
//        opencv_core.Point point3 = new opencv_core.Point(200, 240);
//        // 颜色
//        opencv_core.Scalar scalar1 = new opencv_core.Scalar(0, 255, 255, 0);
//        opencv_core.Scalar scalar2 = new opencv_core.Scalar(255, 0, 0, 0);
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        while (canvasFrame.isVisible() && (grab = grabber.grab()) != null) {
//            // 从第二帧开始添加水印
//            opencv_core.Mat mat = converter.convertToMat(grab);
//            // 加文字水印，opencv_imgproc.putText（图片，水印文字，文字位置，字体，字体大小，字体颜色，字体粗度，文字反锯齿，是否翻转文字）
//            opencv_imgproc.putText(mat, "eguid!", point2, opencv_imgproc.CV_FONT_VECTOR0, 2.2, scalar2, 1, 0,
//                    false);
//            // 翻转字体，文字平滑处理（即反锯齿）
//            opencv_imgproc.putText(mat, "eguid!", point3, opencv_imgproc.CV_FONT_VECTOR0, 2.2, scalar2, 1, 20,
//                    true);
//
//            opencv_imgproc.putText(mat, sdf.format(new Date()), point1, opencv_imgproc.CV_FONT_ITALIC, 0.8, scalar1,
//                    2, 20, false);
//            // 窗口显示图片
//            canvasFrame.showImage(converter.convert(mat));
//
//            // 文件中添加新的一帧
//            recorder.record(converter.convert(mat));
//            mat.release();
//            mat.close();
//
//            Thread.sleep(40);
//        }
//
//
//        canvasFrame.dispose();
//
//        recorder.stop();
//
//        grabber.stop();
//
//        scalar1.close();
//        scalar2.close();
//        point1.close();
//        point2.close();
//        point3.close();
//    }


    public static void main(String[] args)  throws Exception{
        // 转换器，用于Frame/Mat/IplImage相互转换
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();

        // 使用OpenCV抓取本机摄像头，摄像头设备号默认0
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);

        // 开启抓取器
        grabber.start();

        //做好自己！--eguid版权所有，转载请注明出处！
        CanvasFrame cFrame = new CanvasFrame("做好自己！--eguid！", CanvasFrame.getDefaultGamma() / grabber.getGamma());
        cFrame.setAlwaysOnTop(true);
        cFrame.setVisible(true);
        // 水印文字位置
        opencv_core.Point point1 = new Point(10, 50);
        Point point2 = new Point(200, 200);
        Point point3 = new Point(200, 240);
        // 颜色
        Scalar scalar1 = new Scalar(0, 255, 255, 0);
        Scalar scalar2 = new Scalar(255, 0, 0, 0);
        Frame frame = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int index = 0;
        // 关闭窗口即停止运行
        while (cFrame.isShowing()) {
            if ((frame = grabber.grabFrame()) != null) {
                // 取一帧视频（图像），并转换为Mat
                opencv_core.Mat mat = converter.convertToMat(grabber.grabFrame());

                // 加文字水印，opencv_imgproc.putText（图片，水印文字，文字位置，字体，字体大小，字体颜色，字体粗度，文字反锯齿，是否翻转文字）
                opencv_imgproc.putText(mat, "eguid!", point2, opencv_imgproc.CV_FONT_VECTOR0, 2.2, scalar2, 1, 0,
                        false);
                // 翻转字体，文字平滑处理（即反锯齿）
                opencv_imgproc.putText(mat, "eguid!", point3, opencv_imgproc.CV_FONT_VECTOR0, 2.2, scalar2, 1, 20,
                        true);

                opencv_imgproc.putText(mat, sdf.format(new Date()), point1, opencv_imgproc.CV_FONT_ITALIC, 0.8, scalar1,
                        2, 20, false);
                // 在窗口显示处理后的图像，Frame frame=converter.convert(mat);
                cFrame.showImage(converter.convert(mat));
                if (index == 0) {
                    // 保存第一帧图片到本地
                    opencv_imgcodecs.imwrite("eguid.jpg", mat);
                }
                // 释放Mat资源
                mat.release();
                mat.close();

            }
            Thread.sleep(40);
            index++;
        }

        cFrame.dispose();// 销毁窗口
        grabber.stop();// 停止抓取器

        // 手动释放资源
        scalar1.close();
        scalar2.close();
        point1.close();
        point2.close();
        point3.close();
    }



}
