package com.pt.javacv;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;

/**
 * @author nate-pt
 * @date 2021/7/20 14:50
 * @Since 1.8
 * @Description 拉流器的实现
 */
public class Llq {

    public static void main(String[] args) throws Exception {
        // 从斗鱼平台从
        String inputFile = "https://93a4d4c1c2476f08ae415f00cec92ddc.v.smtcdns.net/mobilep2.douyucdn2.cn/dyliveflv3a/454499rhLahsHkvB.xs?startid=4133331977&substream=1&basesub=4&playid=1626767073682-2312048291&uuid=a7899853-db3a-4793-b475-f3b9476762f7&txSecret=d7a76d837a32c984d3b76f0ef3e89b4f&txTime=60f68145&origin=tct";
        String outputFile = "3.flv";
        // 抓取器，设置文件来源
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputFile);
        // 转化器(最后一个参数表示是否录制 0否 1 是)
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputFile, 1280, 720, 1);

        grabber.start();

        recorder.start();
        // 申明帧变量
        Frame frame = null;
        while ((frame = grabber.grabFrame()) != null){
            // 将每一帧写入到输出的文件中
            recorder.record(frame);
        }

        recorder.stop();
        grabber.stop();



    }

}
