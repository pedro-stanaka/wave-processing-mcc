package br.uel.pds.utils;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;



public class EndianessConverterTest extends TestCase {

    byte[] byte8000;
    byte[] byte8;
    int i8000;
    int i8;

    @Before
    public void setUp() throws Exception {
        byte8000 = new byte[]{64, 31, 0, 0};
        byte8 = new byte[]{8,0};
        i8 = 8;
        i8000 = 8000;
    }

    @After
    public void tearDown(){
        byte8000 = null;
    }

    @Test
    public void testConvertLittleEndian() throws Exception {
        assertThat(i8000, is(EndianessConverter.convertLittleEndian(byte8000)));
        assertThat(i8, is(EndianessConverter.convertLittleEndian(byte8)));
    }

    @Test
    public void testConvertBigEndian() throws Exception {
        assertThat(byte8000, is(EndianessConverter.convertBigEndian(i8000,4)));
        assertThat(byte8, is(EndianessConverter.convertBigEndian(i8,2)));
    }
}
