package br.uel.pds.utils;

/**
 * Created by pedro on 28/03/14.
 */
public class EndianessConverter {


    /**
     * Convert <code>binFile.lenght</code> bytes of <code>binFile.lenght</code> to big endian
     * representation.
     * @param binFile Array of bytes to be converted to big endian representation
     * @return int The byte array in integer representation
     */
    public static int convertLittleEndian(byte[] binFile){
        int response = 0;
        for(int i = 0; i < (binFile.length); i++){
            response += (binFile[i] & 0xFF) << (8*i);
        }
        return response;

    }


    /**
     * Convert an bigEndian int to an byte array little endian representation.
     * @param bigEndian An four byte array
     * @return
     */
    public static byte[] convertBigEndian(int bigEndian){
        if (bigEndian != 0){
            byte[] resp = new byte[4];
            for (int i = 0; i < 4; i++) {
                resp[i] = (byte) (bigEndian >> (8*i));
            }
            return resp;
        }else{
            return new byte[4];
        }
    }
}
