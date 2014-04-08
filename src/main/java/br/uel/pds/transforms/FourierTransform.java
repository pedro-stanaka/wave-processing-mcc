package br.uel.pds.transforms;

import java.util.Arrays;

public class FourierTransform {



    public Double[] FastFourierTransform(Double[] sinal, int comprimento) {
        double tr,ti;
        double rex[] = new double [comprimento];
        double imx[] = new double [comprimento];
        double ur,ui;
        double sr,si;
        int le,le2;
        int ip;

        double nm1 = comprimento -1;
        int nd2 = comprimento/2;

        double m = (int)(Math.log(comprimento)/Math.log(2));
        int j = nd2;

        for(int i = 0;i< comprimento-1;i++){
            if(i<=j){
                tr = rex[j];
                ti = imx[j];
                rex[j] = rex[i];
                imx[j] = imx[i];
                rex[i] = tr;
                rex[i] = ti;
            }

        }

        int k = nd2;
        while(k<j){
            j=j-k;
            k=k/2;
            j+=k;
        }

        for(int l = 0;l<m-1;l++){
            le = (int)Math.pow(2, l);
            le2= le/2;

            ur=1;
            ui=0;

            sr = Math.cos(Math.PI/le2);
            si = -Math.sin(Math.PI/le2);

            for(int p =0;p<le2;p++){
                int pm1 = p;
                for(int i = pm1;i<nm1;i=i+le){
                    ip = i+le2;
                    tr = rex[ip]*ur-imx[ip]*ui;
                    ti = rex[ip]*ui-imx[ip]*ur;
                    rex[ip] = rex[i] - tr;
                    imx[ip] = imx[i] -ti;
                    rex[i] = rex[i] + tr;
                    imx[i] = imx[i] + ti;
                }

                tr = ur;
                ur = tr*sr - ui*si;
                ui = tr*si - ui*sr;

            }
            for (int w = 0; w < comprimento - 1; w++) {
                sinal[j] = Math.log10(Math.sqrt((rex[j] * rex[j]) + (imx[j] * imx[j])));
            }
        }
        return sinal;
    }


    public Double[] dft(Double sinal[], int comprimento) {
        double[] re = new double[comprimento];
        double[] im = new double[comprimento];
        for (int u = 0; u < comprimento - 1; u++) {
            re[u] = 0;
            im[u] = 0;
            for (int i = 0; i < comprimento - 1; i++) {
                re[u] += (sinal[i] * Math.cos(2.0 * 3.1415927 * (double) u * (double) i / (double) comprimento));
                im[u] -= (sinal[i] * Math.sin(2.0 * 3.1415927 * (double) u * (double) i / (double) comprimento));
            }
            re[u] /= (double) comprimento;
            im[u] /= (double) comprimento;
        }
        for (int j = 0; j < comprimento - 1; j++) {
            sinal[j] = Math.log10(Math.sqrt((re[j] * re[j]) + (im[j] * im[j])));
        }
        return Arrays.copyOfRange(sinal, 0, comprimento);
    }

}

