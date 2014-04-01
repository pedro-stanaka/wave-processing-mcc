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

    public static void main(String[] args){

        //String s = "/25_4100_16_mono.wav";
        //String s = "/horse.wav";
        String s = "/22.wav";
        wave = new Wave(s);
        //volumeChange(0.5, s);
        getWaveFormat(s);
    }

    private static void getWaveFormat(String filename) {
        ChartCreator cc = new ChartCreator("Wave");
        System.out.println("Wave samples: " + wave.getRawData().length);

        cc.addValues(wave.getRawData());
        cc.createChart("Wave: " + filename, "Sample #", "Amplitude");
    }


    private static void volumeChange(double factor, String outputFileName){
        try {
            OutputStream os = new FileOutputStream(outputFileName);
            os.write(wave.getHeader().getRawHeader());
            for (int i = 0; i < wave.getRawData().length; i++) {
//                System.out.println("Ori: "+wave.getRawData()[i]);
                byte b = (byte) (wave.getRawData()[i] * factor);
//                System.out.println(b);
                os.write(b);
            }
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void resample(InputStream is, OutputStream os, float samplingFactor){

    }

    private void fadeOut(){

    }
}
