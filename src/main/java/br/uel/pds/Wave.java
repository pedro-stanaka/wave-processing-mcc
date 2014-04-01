package br.uel.pds;

import br.uel.pds.utils.EndianessConverter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 *
 * @author Pedro Tanaka
 */
public class Wave {


    private WaveHeader header;
    private byte[] rawData;
    private List<Integer> dataValues;



    public Wave(String fileName) {

        try {
            InputStream is = this.getClass().getResourceAsStream(fileName);
            if(is.markSupported()){
                is.mark(0);
            }
            this.header = new WaveHeader(is);
            this.dataValues = new ArrayList<Integer>();
            this.rawData = new byte[is.available()];
            System.out.println(is.available());
            is.read(rawData);
            this.readData();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readData() {
        if(this.header.getBitsPerSample() == 16){
//            if (this.header.getBlockAlign() == 2){
                this._readData32bits();
//            }else {
//                this._readData16bits();
//            }
        }else{
            this._readData8bits();
        }
    }

    private void _readData32bits() {
        System.out.println("32 read");
        int unsigned;
        for(int i=0; i< this.rawData.length;i+=4) {
            unsigned = EndianessConverter.convertLittleEndian(Arrays.copyOfRange(this.rawData, i, i+4));
            this.dataValues.add(unsigned);
        }
    }

    private void _readData16bits(){
        int unsigned;
        for(int i=0; i< this.rawData.length;i+=2) {
            unsigned = EndianessConverter.convertLittleEndian(Arrays.copyOfRange(this.rawData, i, i+2));
            this.dataValues.add(unsigned);
        }
    }

    private void _readData8bits() {

        int unsigned;
        for(int i=0; i< this.rawData.length; i++) {
            unsigned =  rawData[i] & 0xFF;
            this.dataValues.add(unsigned);
        }

    }

    public byte[] getRawData() {
        return rawData;
    }

    public List<Integer> getDataValues() {
        return dataValues;
    }

    public WaveHeader getHeader() {
        return header;
    }
}
