package com.pt.javacv;

import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import static org.bytedeco.javacpp.opencv_highgui.cvShowImage;
import static org.bytedeco.javacpp.opencv_highgui.cvWaitKey;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_core.*;

/**
 * @author nate-pt
 * @date 2021/7/28 17:05
 * @Since 1.8
 * @Description opencv 图片处理
 */
public class TuPianChuli {

    public static opencv_core.CvScalar g_min = cvScalar(35, 43, 46, 0);
    public static opencv_core.CvScalar g_max = cvScalar(77, 255, 220, 0);

    public static void main(String[] args) {
        // 获取源图片
        opencv_core.IplImage orgImg = cvLoadImage("zpz.jpg");

        // 将rgb 转成hsv
        opencv_core.IplImage hsv = opencv_core.IplImage.create(orgImg.width(), orgImg.height(), orgImg.depth(), orgImg.nChannels());
        cvCvtColor(orgImg, hsv, CV_BGR2HSV);
        IplImage imgThreshold = cvCreateImage(cvGetSize(orgImg), 8, 1);

        //阈值化
        cvInRangeS(hsv, g_min, g_max, imgThreshold);

        // 形态学闭处理
        IplImage Morphology_result = IplImage.create(orgImg.width(), orgImg.height(), IPL_DEPTH_8U, 1);
        IplConvKernel kernelCross = cvCreateStructuringElementEx(21, 21, 7, 7, CV_SHAPE_RECT, (IntPointer) null);
        cvMorphologyEx(imgThreshold,Morphology_result,Morphology_result,kernelCross,MORPH_CLOSE,1);


        // 膨胀腐蚀
        IplImage erosion_dst = IplImage.create(orgImg.width(), orgImg.height(), IPL_DEPTH_8U, 1);
        IplImage dilate_dst   = IplImage.create(orgImg.width(),orgImg.height(), IPL_DEPTH_8U, 1);
        IplConvKernel kernel=cvCreateStructuringElementEx(3,3,1,1,CV_SHAPE_RECT,(IntPointer)null);
        cvErode(Morphology_result,erosion_dst,kernel,19);
        cvDilate(erosion_dst,dilate_dst,kernel,4);

        // 显示图片
        cvShowImage("Contours",dilate_dst);
        cvWaitKey(0);




    }
}
