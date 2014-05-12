package br.uel.pds;

import br.uel.pds.transforms.FourierTransform;
import br.uel.pds.utils.ChartCreator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class WaveProcessor {



    /**
     * @param factor
     * @throws IOException
     */
    public double[] volumeChange(double[] data, double factor) {
        for (int i = 0; i < data.length; i++) {
            data[i] = (data[i] * factor);
        }
        return data;
    }

    // Resample 25_8000_8_mono.wav in order to reduce its size to 1mb

    /**
     * @param wave Wave instance
     * @param samplingFactor An positive integer greater than one. Should not be very big, otherwise aliasing
     * @return WaveProcessor The instance of this class (this).
     * @TODO documentation here
     * @todo Create an alternative way to downsample in order to avoid aliasing
     */
    public WaveProcessor downSample(Wave wave, int samplingFactor) throws IOException {
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
    public WaveProcessor fadeEfx(Wave wave, int sec) throws IOException {
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

    public int findNextTwoPotency(int value){ return (int) Math.pow(2, (int) (Math.log((double)value)/Math.log(2))); }

    public double[] applyFft(double[] values){
        FourierTransform dft = new FourierTransform();

        int nextTwoPotency = findNextTwoPotency(values.length);
        double[] imaginary = new double[nextTwoPotency];
        Arrays.fill(imaginary, 0.0);
        // Direct FFT
        return dft.fastFourierTransform(Arrays.copyOf(values, nextTwoPotency), imaginary, true);
    }

    public double[] applyIfft(double[] fftValues){

        FourierTransform dft = new FourierTransform();
        // Inverse FFT
        return dft.fastFourierTransform(Arrays.copyOf(fftValues, fftValues.length / 2),
                Arrays.copyOfRange(fftValues, (fftValues.length / 2), fftValues.length),
                false);
    }

    public void plotWave(double[] data, String title, String xTitle, String yTitle){
        ChartCreator cc = new ChartCreator(title);
        cc.addValues(data);
        cc.createLineChart(title, xTitle, yTitle);
    }

    public double getWaveEnergy(double[] data){
        double waveEnergy = 0.0;
        for (int i = 0; i < data.length; i++) {
            waveEnergy += Math.pow(data[i], 2.0);
        }
        return waveEnergy;
    }

    public double[] mirrorReal(double[] real){
        int j = 0;
        double[] mirror = new double[real.length];
        for (int i = real.length - 1; i >= 0; i--) {
            mirror[j] = real[i];
        }
        return real;
    }

    public static int findSampleFromSampleRate(int dataLength, int sampleRate, int desiredFreq){
        return ((desiredFreq*dataLength)/(sampleRate/2));
    }


    /** Filters **/

    public double[] lowPassFilter(double[] freqValues, int cutThreshold){
        double[] result = Arrays.copyOf(freqValues, freqValues.length);
        Arrays.fill(result, cutThreshold, freqValues.length, 0.0);
        return result;
    }

    public double[] highPassFilter(double[] freqValues, int cutThreshold){
        double[] result = freqValues.clone();
        Arrays.fill(result, 0, cutThreshold, 0.0);
        return result;
    }


    public double[] bandPassFilter(double[] freqValues, int bandBegin, int bandEnd){
        double[] result = Arrays.copyOf(freqValues, freqValues.length);
        Arrays.fill(result, 0, bandBegin, 0.0);
        Arrays.fill(result, bandEnd, freqValues.length, 0.0);
        return result;
    }


    public double[] bandRejectFilter(double[] freqValues, int bandBegin, int bandEnd){
        double[] result = Arrays.copyOf(freqValues, freqValues.length);
        Arrays.fill(result, bandBegin, bandEnd, 0.0);
        return result;
    }

}
