package br.uel.pds;

import br.uel.pds.utils.EndianessConverter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * This class abstract an wave file. Load the from resources folder inside maven project.
 *
 * @author Pedro Tanaka
 */
public class Wave {

    private WaveHeader header;
    private byte[] rawData;
    private List<Double> dataValues;

    /**
     * @TODO documentation here
     * @param fileName
     */
    public Wave(String fileName) {

        try {
            InputStream is = this.getClass().getResourceAsStream(fileName);
            this.header = new WaveHeader(is);
            this.dataValues = new ArrayList<Double>();
            this.rawData = new byte[header.getSubChunk2Size()];
            is.read(rawData);
            readAsRaw(rawData);
//            this.readData();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readAsRaw(byte []data){
        for (byte b : data) {
            dataValues.add((double) b);
        }
    }

    /**
     * @TODO documentation here
     */
    private void readData() {
        if(this.header.getBitsPerSample() == 16){
            if (this.header.getBlockAlign() == 2){
                this._readData32bits();
            }else {
                this._readData16bits();
            }
        }else{
            this._readData8bits();
        }
    }

    public Double[] getDataAsDouble(){
        Double[] ret = new Double[dataValues.size()];
        dataValues.toArray(ret);
        return  ret;
    }

    /**
     * @TODO documentation here
     */
    private void _readData32bits() {
        int unsigned;
        for(int i=0; i< this.rawData.length;i+=4) {
            unsigned = EndianessConverter.convertLittleEndian(Arrays.copyOfRange(this.rawData, i, i+4));
            this.dataValues.add((double) unsigned);
        }
    }

    /**
     * @TODO documentation here
     */
    private void _readData16bits(){
        int unsigned;
        for(int i=0; i< this.rawData.length;i+=2) {
            unsigned = EndianessConverter.convertLittleEndian(Arrays.copyOfRange(this.rawData, i, i+2));
            this.dataValues.add((double) unsigned);
        }
    }

    /**
     * @TODO documentation here
     */
    private void _readData8bits() {
        int unsigned;
        for(int i=0; i< this.rawData.length; i++) {
            unsigned =  rawData[i] & 0xFF;
            this.dataValues.add((double) unsigned);
        }
    }

    /**
     * @TODO documentation here
     * @return
     */
    public byte[] getRawData() {
        return rawData;
    }

    /**
     * @TODO documentation here
     * @return
     */
    public List<Double> getDataValues() {
        return dataValues;
    }

    /**
     * @TODO documentation here
     * @return
     */
    public WaveHeader getHeader() {
        return header;
    }

    public void setHeader(WaveHeader header) {
        this.header = header;
    }

    public void setDataValues(Double[] dataValues, boolean overwriteRaw) {
        this.dataValues = Arrays.asList(dataValues);

        if(overwriteRaw){
            this.rawData = new byte[rawData.length];
            int j = 0;
            System.out.println(dataValues);
            for (int i = 0; i < this.dataValues.size(); i++) {
//                System.arraycopy(EndianessConverter.convertBigEndian(this.dataValues.get(i).intValue(), 2), 0, this.rawData, j, 2);
//                j+=2;
                this.rawData[i] = (byte) this.dataValues.get(i).intValue();
            }
        }
    }


//    public static void main(String args[]){
//        Double a[] = {4, 2};
//
//    }
}
