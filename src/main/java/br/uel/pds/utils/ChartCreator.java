package br.uel.pds.utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.List;


/**
 * JFreeChart wrapper
 */


public class ChartCreator {

    private XYSeries dataSet;



    public ChartCreator(String dataSetTitle){
        this.dataSet = new XYSeries(dataSetTitle);
    }

    public void addValues(byte[] values){
        int cont=0;
        for (byte i : values) {
            this.dataSet.add((double) i, (double) cont);
            cont++;
        }
        System.out.println("end chart add");
    }

    public int createChart(String chartTitle, String xTitle, String yTitle){
        XYDataset sc = new XYSeriesCollection(this.dataSet);
        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, xTitle, yTitle, sc, PlotOrientation.VERTICAL, true, true, false);
        ChartFrame frame1=new ChartFrame("Wave Format",chart);
        frame1.setVisible(true);
        frame1.setSize(1300, 250);
        return 0;
    }


}
