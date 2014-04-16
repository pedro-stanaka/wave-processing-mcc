package br.uel.pds;

import br.uel.pds.transforms.FourierTransform;
import br.uel.pds.utils.ChartCreator;

import java.io.IOException;
import java.util.Arrays;

public class WaveLabs {
//    private static final String inputFileVoice = "/recording/rec27.wav";
    private static final String inputFileVoice = "/22.wav";
    private static final String inputFileMusic = "/25_8000_8_mono.wav";


    public static  void main(String[] args) throws IOException {
//        applyDft();
        applyVfft();
//        applyFft();
    }



    public static void halfVolume() throws IOException {

        WaveProcessor wp = new WaveProcessor(inputFileMusic);
        wp.volumeChange(0.4).getWaveFormat("edited_" + inputFileMusic.substring(1)).saveFile("edited_" + inputFileMusic.substring(1));
    }

    public static void halfDownSample() throws IOException {

        WaveProcessor wp = new WaveProcessor(inputFileMusic);
        wp.downSample(2).getWaveFormat("edited_" + inputFileMusic.substring(1)).saveFile("edited_" + inputFileMusic.substring(1));
    }

    public static void fadeAudio() throws IOException {
        String original = inputFileMusic;
        WaveProcessor wp = new WaveProcessor(original);
        wp.fadeEfx(3).getWaveFormat("edited_" + original.substring(1)).saveFile("edited_"+original.substring(1));
    }

    public static void applyDft(){

        WaveProcessor wp = new WaveProcessor(inputFileVoice);
        wp.getWaveFormat(inputFileVoice);
        FourierTransform dft = new FourierTransform();
        int samples = 4096;

        Double[] result = dft.discreteFourierTransform(wp.getWave().getDataAsDouble(), samples);
        System.out.println("Data length:  " + result.length);
        ChartCreator cc = new ChartCreator("DFT "+samples);
        cc.addValues(Arrays.asList(Arrays.copyOfRange(result,0,samples/2)));
        cc.createLineChart("DFT 2048", "Freq", "Amp (Db)");
    }

    private static int findNextTwoPotency(int value){ return (int) Math.pow(2, (int) (Math.log((double)value)/Math.log(2))); }

    public static void applyFft(){
        String original = inputFileVoice;
        WaveProcessor wp = new WaveProcessor(original);
        wp.getWaveFormat(original);
        FourierTransform dft = new FourierTransform();


        int length = wp.getWave().getDataAsDouble().length;
        int nextTwoPotency = findNextTwoPotency(length);

        Double[] imaginary = new Double[nextTwoPotency];
        Arrays.fill(imaginary, 0.0);
        // Direct FFT
        Double[] result = dft.FastFourierTransform(Arrays.copyOf(wp.getWave().getDataAsDouble(), nextTwoPotency), imaginary, true);
        for (int i = 0; i < 100; i++) {
            System.out.println(result[i]);
        }
        ChartCreator cc = new ChartCreator("FFT "+ nextTwoPotency);
        cc.addValues(Arrays.asList(Arrays.copyOf(result, nextTwoPotency/2)));
        cc.createLineChart("FFT", "Freq", "Amp (Db)");


        // Inverse FFT
        result = dft.FastFourierTransform(Arrays.copyOf(imaginary, findNextTwoPotency(nextTwoPotency/2)), Arrays.copyOf(wp.getWave().getDataAsDouble(), findNextTwoPotency(nextTwoPotency/2)),false);
        ChartCreator cc2 = new ChartCreator("IFFT :"+ nextTwoPotency);
        cc2.addValues(Arrays.asList(result));
        cc.createLineChart("IFFT", "Freq", "Amp (Db)");
    }

    public static void applyVfft(){
        String original = inputFileVoice;
        WaveProcessor wp = new WaveProcessor(original);
        wp.getWaveFormat(original);
        FourierTransform dft = new FourierTransform();
        int length = wp.getWave().getDataAsDouble().length;
        int nextTwoPotency = findNextTwoPotency(length);

        Double[] imaginary = new Double[nextTwoPotency];
        Arrays.fill(imaginary, 0.0);
        double [] result = dft.vfft(Arrays.copyOf(wp.getWave().getDataAsDouble(), nextTwoPotency), imaginary);
        double [] cr = result.clone();
        double [] cr2 = result.clone();
        System.out.println(result.length);
        ChartCreator cc = new ChartCreator("VFFT :"+ nextTwoPotency);
        cc.addValuesIntercalated(Arrays.copyOf(result, nextTwoPotency/2));
        cc.createLineChart("VFFT", "Freq", "Amp (Db)");

        System.out.println(result.length);
        dft.iVfft(cr, false);
        ChartCreator cc2 = new ChartCreator("IFFT (ns):"+ nextTwoPotency);
        cc2.addValuesIntercalated(result);
        cc2.createLineChart("IFFT (ns)", "Freq", "Amp (Db)");
        dft.iVfft(cr2, true);
        ChartCreator cc3 = new ChartCreator("IFFT (scaled):"+ nextTwoPotency);
        cc3.addValuesIntercalated(result);
        cc3.createLineChart("IFFT (scaled)", "Freq", "Amp (Db)");
    }


}
