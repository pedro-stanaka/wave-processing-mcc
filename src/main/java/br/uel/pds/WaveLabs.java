package br.uel.pds;

import java.io.IOException;

public class WaveLabs {
    public static  void main(String[] args) throws IOException {
        String original = "/25_4100_16_mono.wav";
//        String original = "/25_8000_8_mono.wav";
//        String original = "/i_see_fire.wav";
        WaveProcessor wp = new WaveProcessor(original);
        wp.getWaveFormat(original);
        wp.volumeChange(0.4).getWaveFormat("edited_"+original.substring(1)).saveFile("edited_"+original.substring(1));
    }
}
