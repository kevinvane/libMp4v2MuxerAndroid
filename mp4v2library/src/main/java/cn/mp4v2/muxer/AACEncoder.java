package cn.mp4v2.muxer;

public class AACEncoder {

    static {
        System.loadLibrary("faac-jni");
    }

    private static int maxInputSample = 0;
    private static int audioPos;
    private static byte[] audioBufferZone;
    public static int init(long sampleRate, int sampleDuration){

        //取整数，不管余数
        int inputSample = (maxInputSample / sampleDuration) * sampleDuration;
        maxInputSample = init(sampleRate,inputSample,1,false);
        audioBufferZone = new byte[maxInputSample];
        audioPos = 0;
        return maxInputSample;
    }

    /**
     * 设备输入的数据太小，建个缓冲区，缓冲区到一定大小再编码
     * @param pcm
     * @param pcmLength
     * @return
     */
    public static boolean inputSample(byte[] pcm, int pcmLength){

        System.arraycopy(pcm,0,audioBufferZone,audioPos,pcmLength);
        audioPos += pcmLength;
        return audioPos >= (maxInputSample - pcmLength);
    }

    public static byte[] encodeBufferZone(){

        int frameSize = audioPos;
        audioPos = 0;
        byte[] frame = new byte[frameSize];
        System.arraycopy(audioBufferZone,0,frame,0,frameSize);
        return encode(frame,frameSize);
    }

    public static void release(){

        if(maxInputSample > 0){
            stop();
        }
    }

    /**
     * 初始化
     * @param sampleRate，如：8000
     * @param numChannels 如：1
     * @param isADTS ADTS是每帧数据带的解码信息。直播流要ADTS；存文件不需要。
     * @return inputSamples
     */
    private native static int init(long sampleRate, int bitRate, int numChannels, boolean isADTS);

    /**
     * 编码
     * @param pcm
     * @param pcmLength
     * @return
     */
    private native static byte[] encode(byte[] pcm,int pcmLength);

    /**
     * 获取编码配置信息
     * @return @nullable
     */
    public native static byte[] getConfig();

    private native static void stop();

}
