package br.uel.pds;

import br.uel.pds.transforms.FourierTransform;
import br.uel.pds.utils.ChartCreator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class WaveProcessor {

    private static Wave wave;
    private final static String OUT_FOLDER = "src/main/resources/";
    private Double[] fourierResults;

    public WaveProcessor(String originalFile) {
        this.wave = new Wave(originalFile);
    }

    public WaveProcessor getWaveFormat(String filename) {
        ChartCreator cc = new ChartCreator("Wave");
        cc.addValues(wave.getDataValues());
        cc.createLineChart("Wave: " + filename, "Sample #", "Amplitude");
        return this;
    }


    /**
     * @param factor
     * @throws IOException
     */
    public WaveProcessor volumeChange(double factor) throws IOException {
        for (int i = 0; i < wave.getRawData().length; i++) {
            wave.getRawData()[i] = (byte) (wave.getRawData()[i] * factor);
        }
        return this;
    }

    // Resample 25_8000_8_mono.wav in order to reduce its size to 1mb

    /**
     * @param samplingFactor An positive integer greater than one. Should not be very big, otherwise aliasing
     * @return WaveProcessor The instance of this class (this).
     * @TODO documentation here
     * @todo Create an alternative way to downsample in order to avoid aliasing
     */
    public WaveProcessor downSample(int samplingFactor) throws IOException {
        WaveHeader h = wave.getHeader();

        h.setSampleRate(wave.getHeader().getSampleRate() / samplingFactor);
        h.setSubChunk2Size(wave.getHeader().getSubChunk2Size() / samplingFactor);

        List<Byte> byteOut = new ArrayList<Byte>();
        for (int i = 0; i < wave.getRawData().length; i += samplingFactor) {
            byteOut.add(wave.getRawData()[i]);
        }

        h.setSubChunk2Size(byteOut.size());
        h.setByteRate(h.getSampleRate() * h.getNumChannels() * (h.getBitsPerSample() / 8));
        h.setChunkSize(36 + byteOut.size());
        wave.setHeader(h);
        return this;
    }

    // Apply fadeIn fadeOut in 25_4100_16_mono.wav
    public WaveProcessor fadeEfx(int sec) throws IOException {
        int numberOfSamples = wave.getHeader().getByteRate() * sec;

        double factor = 0, step = 1.0 / (double) sec;
        // fade in
        for (int i = 0; i < numberOfSamples; i++) {
            wave.getRawData()[i] = (byte) (wave.getRawData()[i] * factor);
            if (i % (wave.getHeader().getByteRate()) == 0) factor += step;

        }

        // fade out
        factor = 1;
        for (int i = (wave.getRawData().length - numberOfSamples); i < wave.getRawData().length; i++) {
            wave.getRawData()[i] = (byte) (wave.getRawData()[i] * factor);
            if (i % (wave.getHeader().getByteRate()) == 0) factor -= step;
        }

        return this;
    }

    // Count words in 04.wav
    public WaveProcessor countWords(int wordCount) {

        return this;
    }

    private int findNextTwoPotency(int value){ return (int) Math.pow(2, (int) (Math.log((double)value)/Math.log(2))); }

    public WaveProcessor applyFft(){
        FourierTransform dft = new FourierTransform();


        int length = wave.getDataAsDouble().length;
        int nextTwoPotency = findNextTwoPotency(length);

        Double[] imaginary = new Double[nextTwoPotency];
        Arrays.fill(imaginary, 0.0);
        // Direct FFT
        this.fourierResults = dft.FastFourierTransform(Arrays.copyOf(wave.getDataAsDouble(), nextTwoPotency), imaginary, true);
        return this;
    }

    public WaveProcessor applyIfft(){
        FourierTransform dft = new FourierTransform();
        System.out.println("Tamanho vetor: " + fourierResults.length);
        // Inverse FFT
        fourierResults = dft.FastFourierTransform(Arrays.copyOf(fourierResults, fourierResults.length/2),
                Arrays.copyOfRange(fourierResults, (fourierResults.length / 2), fourierResults.length),
                false);
        System.out.println("Fourier tamanho: "+fourierResults.length);
        wave.setDataValues(Arrays.copyOf(fourierResults, fourierResults.length/2), true);
        return this;
    }

    public int saveFile(String fileName) throws IOException {
        File f = new File(OUT_FOLDER + fileName);
        if (f.exists()) f.delete();
        OutputStream os = new FileOutputStream(OUT_FOLDER + fileName);
        os.write(wave.getHeader().getRawHeader());
        os.write(wave.getRawData());
        os.close();
        return 0;
    }

    public WaveProcessor plotFourier(){
        ChartCreator cc = new ChartCreator("Wave");
        cc.addValues(wave.getDataValues());
        cc.createLineChart("Wave: " + "FFT", "Sample #", "Amplitude");
        return this;
    }

    public Wave getWave() {
        return wave;
    }

    public void saveFileFourierResult(String fileName) throws IOException {
        File f = new File(OUT_FOLDER + fileName);
        if (f.exists()) f.delete();
        OutputStream os = new FileOutputStream(OUT_FOLDER + fileName);
        os.write(wave.getHeader().getRawHeader());
        os.write(wave.getRawData());
        os.close();
    }
}
