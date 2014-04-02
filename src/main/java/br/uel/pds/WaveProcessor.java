package br.uel.pds;

import br.uel.pds.utils.ChartCreator;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 */
public class WaveProcessor {

    private static Wave wave;

    /**
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        //String original = "/25_4100_16_mono.wav";
        String original = "/25_8000_8_mono.wav";
        String output = "edited.wav";

        wave = new Wave(original);
//        volumeChange(0.5, output);
        downSample(output, 2);
//        getWaveFormat(original);

    }

    private static void getWaveFormat(String filename) {
        ChartCreator cc = new ChartCreator("Wave");
        cc.addValues(wave.getDataValues());
        cc.createChart("Wave: " + filename, "Sample #", "Amplitude");
    }


    /**
     *
     * @param factor
     * @param outputFileName
     * @throws IOException
     */
    private static void volumeChange(double factor, String outputFileName) throws IOException {

        OutputStream os = new FileOutputStream(outputFileName);
        os.write(wave.getHeader().getRawHeader());
        for (int i = 0; i < wave.getRawData().length; i++) {
            byte b = (byte) (wave.getRawData()[i] * factor);
            os.write(b);
        }
        os.close();
    }

    // Resample 25_8000_8_mono.wav in order to reduce its size to 1mb

    /**
     *
     * @param outputFileName The name of the file that will be written
     * @param samplingFactor An positive integer greater than one. Should not be very big, otherwise aliasing
     *                       can occur in downsampling process
     */
    private static void downSample(String outputFileName, int samplingFactor) throws IOException {
        RandomAccessFile outputFile = new RandomAccessFile(outputFileName, "rw");

        int length = 0, aux = 0;
        WaveHeader h = wave.getHeader();
        byte[] temp = new byte[512];

        h.setSampleRate(wave.getHeader().getSampleRate()/2);
        h.setSubChunk2Size(wave.getHeader().getSubChunk2Size()/2);


        outputFile.write(h.getRawHeader());
        for (int i = 0; i < wave.getRawData().length; i+=samplingFactor) {
            length++;
            temp[aux++] = wave.getRawData()[i];
            if(aux == 511){
                System.out.println("pos: " + aux + " ## len: " + length);
                outputFile.write(temp);
                aux = 0;
            }
        }
        h.setSubChunk2Size(length);
        outputFile.seek(0);
        outputFile.write(h.getRawHeader());
        outputFile.close();
    }

    // Apply fadeIn fadeOut in 25_4100_16_mono.wav
    private void fadeEfx(int sec, String outputFileName){

    }

    // Count words in 04.wav
    private static void countWords(){

    }
}
