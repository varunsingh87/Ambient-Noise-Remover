package com.varunsingh.ambientnoiseremover;

import com.varunsingh.soundmanipulation.AudioByteSet;
import com.varunsingh.soundmanipulation.AudioSampleSet;
import com.varunsingh.soundmanipulation.SimpleWave;

public class BitwiseSandbox {
    public static void main(String[] args) {
        float[] origBuffer = AudioSampleSet.createSampleBufferFromWave(
            new SimpleWave(380, 0.3)
        ).getSampleBuffer();
        
        AudioSampleSet.createSampleBufferFromByteBuffer(
            AudioByteSet.createByteBufferFromSampleBuffer(origBuffer).getByteBuffer()
        );
    }

    static int shiftBitwiseRightUnsigned(int origVal) {
        return origVal >>> 8;
    }
    
    static int shiftBitwiseLeftSigned(int origVal) {
        return origVal << 8;
    }
}
