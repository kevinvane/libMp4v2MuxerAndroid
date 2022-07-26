package cn.mp4v2.muxer;

public class Mp4v2Jni {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("mp4v2-jni");
    }

    /**
     * 查找nalu
     * @param type 指定查找的Nalu类型，如: 5,7,8 。如果此值小于 0，将不判断Nalu类型，找到Nalu即返回。
     * @param data 数组源
     * @param dataLength 数据源的长度
     * @param startPosition 数据源开始查找的位置
     * @param lastPosition 查找的最后位置，长度为1的数组。
     * @return nalu buffer
     */
    public static native synchronized byte[] getNaluForType(int type, byte[] data,int dataLength,int startPosition,int[] lastPosition);


    /**
     * 只是用于特殊场景：
     * 当 “ SPS + PPS + I帧 ” 粘在一起作为一个数据包时，通过查找I帧的位置，将I帧数据拷贝出来。
     * @param data
     * @param dataLength
     * @return
     */
    public static native synchronized int getFirstIDRPosition(byte[] data,int dataLength);


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public static native synchronized boolean createMp4File(String outputFilePath);

    /**
     * 添加视频轨道
     * @param sps 包含 “0x00 0x00 0x00 0x01”
     * @param spsSize
     * @param pps 包含 “0x00 0x00 0x00 0x01”
     * @param ppsSize
     * @return
     */
    public static native synchronized boolean addAvcVideoTrack(byte[] sps,
                                                               int spsSize,
                                                               byte[] pps,
                                                               int ppsSize,
                                                               int frameRate,
                                                               int width,
                                                               int height);

    /**
     * 写入H264帧
     * @param frame 包含 “0x00 0x00 0x00 0x01”
     * @param frameSize
     * @param isKeyFrame
     * @return
     */
    public static native synchronized boolean writeAvcVideoFrame(byte[] frame,
                                                                 int frameSize,
                                                                 boolean isKeyFrame,
                                                                 boolean isDebug);


    /**
     * 添加Acc音轨
     * @param sampleRate 采样率，如： 44100
     * @param sampleDuration 每一帧的字节数，如：1024
     * @param accConfig 解码配置信息：1. Android硬编码器会输出配置；2. FACC软编码器也会输出配置
     * @return
     */
    public static native synchronized boolean addAccAudioTrack(int sampleRate,
                                                               int sampleDuration,
                                                               byte[] accConfig,
                                                               int accConfigLength);

    /**
     * 写入Acc音频数据
     * 注意：AAC音频数据不能加 ADTS header
     * @param frame
     * @param frameLength
     * @return
     */
    public static native synchronized boolean writeAccAudioFrame(byte[] frame,
                                                                 int frameLength);


    public static native synchronized void closeMp4File();

}
