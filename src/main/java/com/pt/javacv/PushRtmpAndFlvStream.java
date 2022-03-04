package com.pt.javacv;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;

import javax.swing.*;

/**
 * @author nate-pt
 * @date 2021/7/20 11:20
 * @Since 1.8
 * @Description
 */
public class PushRtmpAndFlvStream {

    /**
     * flv-rtmp通用推流
     * @param input 可以是动态图片(apng,gif等等)，视频文件（mp4,flv,avi等等）,流媒体地址（http-flv,rtmp，rtsp等等）
     * @param output 可以是flv,http-flv,rtmp
     * @param width 录制/推流的视频图像宽度
     * @param height 录制/推流的视频图像高度
     * @param frameRate 录制/推流的视频帧率
     */
    public static void pushFlvOrRtmp(String input,String output,Integer width,Integer height,Integer frameRate) throws Exception, org.bytedeco.javacv.FrameGrabber.Exception{
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(input);
        grabber.start();

        if(width==null||height==null) {
            width=grabber.getImageWidth();
            height=grabber.getImageHeight();
        }

        FFmpegFrameRecorder recorder =new FFmpegFrameRecorder(output,width,height,2);

        //设置flv格式
        recorder.setFormat("flv");

        if(frameRate==null) {
            frameRate=25;
        }
        recorder.setFrameRate(frameRate);//设置帧率
        //因为我们是直播，如果需要保证最小延迟，gop最好设置成帧率相同或者帧率*2
        //一个gop表示关键帧间隔，假设25帧/秒视频，gop是50，则每隔两秒有一个关键帧，播放器必须加载到关键帧才能够开始解码播放，也就是说这个直播流最多有2秒延迟
        recorder.setGopSize(frameRate*frameRate);//设置gop
        recorder.setVideoQuality(1.0); //视频质量
        recorder.setVideoBitrate(100);//码率
//		recorder.setVideoCodecName("h264");//设置视频编码
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);//这种方式也可以
//		recorder.setAudioCodecName("aac");//设置音频编码，这种方式设置音频编码也可以
        recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);//设置音频编码

        recorder.start();


        CanvasFrame canvas = new CanvasFrame("视频预览");// 新建一个窗口
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame frame = null;

        // 只抓取图像画面
        for (;(frame = grabber.grabImage()) != null;) {
            try {
                //录制/推流
                recorder.record(frame);
                //显示画面
                canvas.showImage(frame);
            } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
                e.printStackTrace();
            }
        }

        recorder.close();//close包含stop和release方法。录制文件必须保证最后执行stop()方法，才能保证文件头写入完整，否则文件损坏。
        grabber.close();//close包含stop和release方法
        canvas.dispose();
    }

    public static void main(String[] args) throws Exception, org.bytedeco.javacv.FrameGrabber.Exception {
        pushFlvOrRtmp("http://192.168.5.24:32082/hls/745fb7dcd63941d1bd3b74a327ca2baa_1080/index.m3u8","eguid.flv",400,300,25);

    }


}
