package br.uel.pds;

import br.uel.pds.utils.ChartCreator;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by pedro on 31/03/14.
 */
public class WaveProcessor {

    private static Wave wave;

    public static void main(String[] args) throws IOException {

        //String s = "/25_4100_16_mono.wav";
        String original = "/horse.wav";
//        String original = "/rec6.wav";
        String output = "edited.wav";
        wave = new Wave(original);
        volumeChange(0.5, output);
        getWaveFormat(original);
    }

    private static void getWaveFormat(String filename) {
        ChartCreator cc = new ChartCreator("Wave");
        System.out.println("Wave samples: " + wave.getRawData().length);

        cc.addValues(wave.getDataValues());
        cc.createChart("Wave: " + filename, "Sample #", "Amplitude");
    }


    private static void volumeChange(double factor, String outputFileName) throws IOException {

        OutputStream os = new FileOutputStream(outputFileName);
        os.write(wave.getHeader().getRawHeader());
        for (int i = 0; i < wave.getRawData().length; i++) {
            wave.getRawData()[i] = (byte) (wave.getRawData()[i] * factor);
        }
        os.write(wave.getRawData());
        os.close();
    }

    private void resample(InputStream is, OutputStream os, float samplingFactor){

    }

    private void fadeOut(){

    }
}
