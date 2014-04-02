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
    private final static String OUT_FOLDER = "src/main/resources/";
    /**
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        String original = "/25_4100_16_mono.wav";
//        String original = "/25_8000_8_mono.wav";
//        String original = "/i_see_fire.wav";

        wave = new Wave(original);
//        volumeChange(0.5, "ed_fade_"+original.substring(1));
//        downSample(output, 2);
        fadeEfx(5, "ed_fade_" + original.substring(1));
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

        File f = new File(OUT_FOLDER+outputFileName);
        if(f.exists()) f.delete();

        OutputStream os = new FileOutputStream(OUT_FOLDER+outputFileName);
        os.write(wave.getHeader().getRawHeader());
        for (int i = 0; i < wave.getRawData().length; i++) {
            wave.getRawData()[i] = (byte) (wave.getRawData()[i] * factor);
        }
        os.write(wave.getRawData());
        os.close();
    }

    // Resample 25_8000_8_mono.wav in order to reduce its size to 1mb
    /**
     * @TODO documentation here
     * @todo Create an alternative way to downsample in order to avoid aliasing
     * @param outputFileName The name of the file that will be written
     * @param samplingFactor An positive integer greater than one. Should not be very big, otherwise aliasing
     *                       can occur in downsampling process
     */
    private static void downSample(String outputFileName, int samplingFactor) throws IOException {
        RandomAccessFile outputFile = new RandomAccessFile(OUT_FOLDER+outputFileName, "rw");

        int length = 0, aux = 0;
        WaveHeader h = wave.getHeader();
        byte[] temp = new byte[512];

        h.setSampleRate(wave.getHeader().getSampleRate()/samplingFactor);
        h.setSubChunk2Size(wave.getHeader().getSubChunk2Size()/samplingFactor);


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
        h.setByteRate(h.getSampleRate() * h.getNumChannels() * (h.getBitsPerSample()/8));
        outputFile.seek(0);
        outputFile.write(h.getRawHeader());
        outputFile.close();
    }

    // Apply fadeIn fadeOut in 25_4100_16_mono.wav
    private static void fadeEfx(int sec, String outputFileName) throws IOException {
        int numberOfSamples = wave.getHeader().getByteRate() * sec;
        System.out.println(numberOfSamples);

        File f = new File(OUT_FOLDER+outputFileName);
        if(f.exists()) f.delete();

        OutputStream os = new FileOutputStream(OUT_FOLDER+outputFileName);
        os.write(wave.getHeader().getRawHeader());

        double factor = 0, step = 1.0/(double)sec;
        // fade in
        for (int i = 0; i < numberOfSamples; i++) {
            wave.getRawData()[i] = (byte) (wave.getRawData()[i] * factor);
            if(i% (wave.getHeader().getByteRate()) == 0) factor += step;

        }

        factor = 1;
        for (int i = (wave.getRawData().length - numberOfSamples); i < wave.getRawData().length; i++) {
            wave.getRawData()[i] = (byte) (wave.getRawData()[i] * factor);
            if(i% (wave.getHeader().getByteRate()) == 0) factor -= step;
        }
        os.write(wave.getRawData());
        os.close();
    }

    // Count words in 04.wav
    private static void countWords(){

    }
}
