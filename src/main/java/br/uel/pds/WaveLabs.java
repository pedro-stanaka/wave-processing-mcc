package br.uel.pds;

import br.uel.pds.transforms.FourierTransform;
import br.uel.pds.utils.ChartCreator;

import java.io.IOException;
import java.util.Arrays;

public class WaveLabs {
    //    private static final String inputFileVoice = "/recording/rec27.wav";
    private static final String inputFileVoice = "/22.wav";
    private static final String inputFileMusic = "/25_8000_8_mono.wav";
    private static final String OUTPUT_FOLDER = "src/main/resources/out/";


    public static  void main(String[] args) throws IOException {
//        applyDft();
//        applyVfft();
//        applyFftAndInverse();
//        applyLowPass500();
        applyHighPass500();
    }


    public static void halfVolume() throws IOException {
        WaveProcessor wp = new WaveProcessor();
        Wave wave = new Wave(inputFileMusic);
        wp.volumeChange(wave.getDataValues(), 0.4);
        wp.plotWave(wave.getDataValues(), "edited_" + inputFileMusic.substring(1), "X", "Y");
        wave.saveFile(wave.getDataValues(), "edited_" + inputFileMusic.substring(1));
    }

    public static void halfDownSample() throws IOException {

        WaveProcessor wp = new WaveProcessor();
        Wave wave = new Wave(inputFileMusic);
        wp.downSample(wave, 2);
        wp.plotWave(wave.getDataValues(), "edited_" + inputFileMusic.substring(1), "X", "X");
        wave.saveFile(wave.getDataValues(), "edited_" + inputFileMusic.substring(1));
    }

    public static void fadeAudio() throws IOException {
        String original = inputFileMusic;
        WaveProcessor wp = new WaveProcessor();
        Wave wave = new Wave(original);
        wp.fadeEfx(wave, 3);
        wp.plotWave(wave.getDataValues(), "Wave", "Time", "Amp");
    }

    public static void applyDft(){
        Wave waveFile = new Wave(inputFileVoice);
        FourierTransform dft = new FourierTransform();
        int samples = 4096;
        double[] result = dft.discreteFourierTransform(waveFile.getDataValues(), samples);
        ChartCreator cc = new ChartCreator("DFT "+samples);
        cc.addValues(Arrays.copyOfRange(result, 0, samples / 2));
        cc.createLineChart("DFT 2048", "Freq", "Amp (Db)");
    }



    public static void applyFftAndInverse() throws IOException {
        String original = inputFileVoice;
        Wave waveFile = new Wave(original);

        WaveProcessor wp = new WaveProcessor();
        double[] values = waveFile.getDataValues();
        wp.plotWave(values, "Wave Original", "Time", "Amp");
        double[] fftValues = wp.applyFft(values);
        wp.plotWave(fftValues, "FFT", "Freq", "Occ");
        double[] ifftVals = wp.applyIfft(fftValues);
        wp.plotWave(ifftVals, "IFFT", "Time", "Amp");
        waveFile.saveFile(ifftVals, "ifft-result.wav");
    }

    public static void applyVfft(){
        String original = inputFileVoice;
        Wave wave = new Wave(original);
        WaveProcessor wp = new WaveProcessor();
        FourierTransform dft = new FourierTransform();
        int length = wave.getDataValues().length;
        int nextTwoPotency = wp.findNextTwoPotency(length);

        double[] imaginary = new double[nextTwoPotency];
        Arrays.fill(imaginary, 0.0);
        double [] result = dft.vfft(Arrays.copyOf(wave.getDataValues(), nextTwoPotency), imaginary);

        ChartCreator cc = new ChartCreator("VFFT :"+ nextTwoPotency);
        cc.addValuesIntercalated(Arrays.copyOf(result, nextTwoPotency/2));
        cc.createLineChart("VFFT", "Freq", "Amp (Db)");

        double[] ifft = dft.iVfft(result, false);
        ChartCreator cc2 = new ChartCreator("IFFT (ns):"+ nextTwoPotency);
        cc2.addValues(ifft);
        cc2.createLineChart("IFFT (ns)", "Freq", "Amp (Db)");

        wave.saveFile(ifft, "ivff_result.wav");
    }



    private static void applyLowPass500(){
        Wave w = new Wave(inputFileVoice);
        WaveProcessor wp = new WaveProcessor();
        FourierTransform ft = new FourierTransform();

        int nextTwoPotency = wp.findNextTwoPotency(w.getDataValues().length);
        int cut = wp.findSampleFromSampleRate(nextTwoPotency, w.getHeader().getSampleRate(), 500);

        double[] values = Arrays.copyOf(w.getDataValues(), nextTwoPotency);
        double[] img = new double[nextTwoPotency];
        double[] fftRes = ft.fastFourierTransform(values, img, true);


        double[] lowPass = wp.lowPassFilter(Arrays.copyOf(fftRes, fftRes.length/2), cut);
        double[] complex = wp.mirrorReal(lowPass);
        double[] ifftRes = ft.fastFourierTransform(lowPass,
                complex, false);
        w.saveFile(ifftRes, OUTPUT_FOLDER + "low_pass.wav");
    }


    private static void applyHighPass500(){
        Wave w = new Wave(inputFileVoice);
        WaveProcessor wp = new WaveProcessor();
        FourierTransform ft = new FourierTransform();

        int nextTwoPotency = wp.findNextTwoPotency(w.getDataValues().length);
        int cut = wp.findSampleFromSampleRate(nextTwoPotency, w.getHeader().getSampleRate(), 1000);


        double[] values = Arrays.copyOf(w.getDataValues(), nextTwoPotency);
        double[] img = new double[nextTwoPotency];
        double[] fftRes = ft.fastFourierTransform(values, img, true);

        double[] highPass = wp.highPassFilter(Arrays.copyOf(fftRes, fftRes.length/2) , cut);

        double[] complex = wp.mirrorReal(highPass);
        double[] ifftRes = ft.fastFourierTransform(highPass,
                complex, false);
        w.saveFile(Arrays.copyOf(ifftRes, ifftRes.length/2), OUTPUT_FOLDER + "high_pass.wav");
    }
}
