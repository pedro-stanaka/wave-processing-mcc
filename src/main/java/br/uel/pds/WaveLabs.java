package br.uel.pds;

import br.uel.pds.transforms.FourierTransform;
import br.uel.pds.utils.ChartCreator;

import java.io.IOException;
import java.util.Arrays;

public class WaveLabs {
    public static  void main(String[] args) throws IOException {
        halfVolume();
    }


    public static void halfVolume() throws IOException {
        String original = "/25_8000_8_mono.wav";
        WaveProcessor wp = new WaveProcessor(original);
        wp.volumeChange(0.4).getWaveFormat("edited_"+original.substring(1)).saveFile("edited_"+original.substring(1));
    }

    public static void applyDft(){
        String original = "/22.wav";
        WaveProcessor wp = new WaveProcessor(original);
        wp.getWaveFormat(original);
        FourierTransform dft = new FourierTransform();
        int samples = 1024;

        Double[] result = dft.dft(wp.getWave().getDataAsDouble(), samples);
        System.out.println("Data length:  " + result.length);
        ChartCreator cc = new ChartCreator("DFT 2048");
        cc.addValues(Arrays.asList(Arrays.copyOfRange(result,0,samples/2)));
        cc.createLineChart("DFT 2048", "Freq", "Amp (Db)");
    }

}
