package com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper

import android.media.AudioFormat.*
import android.media.MediaRecorder
import android.media.AudioRecord
import android.util.Log


class AudioDataCollector {
    val RECORDER_SAMPLERATE = 8000
    val RECORDER_CHANNELS = CHANNEL_IN_MONO
    val RECORDER_AUDIO_ENCODING = ENCODING_PCM_16BIT

    fun getData():Float{
        var averageAplitude: Float = 0F
        var bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING)

        var recorder = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, bufferSize)

        val data = ShortArray(bufferSize)
        Log.d("lasa", "recording")

       // while (!Thread.interrupted()) {

            recorder.startRecording()

            recorder.read(data, 0, bufferSize)
            Log.d("lasa", "buffer size: $bufferSize")
            recorder.stop()
            Log.d("lasa", "Stopped")
            var i=0
            for (s in data) {
//                try {
//                    Thread.sleep(300.00.toLong())
//                } catch (e: InterruptedException) {
//                    e.printStackTrace()
//                }

                averageAplitude += Math.abs(s.toInt()).toFloat()
                i++
            }
            Log.d("lasa", "iterations: $i")
            averageAplitude /= data.size
        //}

        recorder.release()
        return averageAplitude
    }
}