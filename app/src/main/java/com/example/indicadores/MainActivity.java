package com.example.indicadores;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tvNodeID, tvCoordinador, tvNodo, tvBinaria1, tvBinaria2, tvCorriente1, tvCorriente2, tvCorriente3, tvVoltaje1, tvVoltaje2, tvVoltaje3, tvPico1, tvPico2, tvPico3, tvTemperatura1, tvTemperatura2, tvTemperatura3, tvTiempo1, tvTiempo2, tvTiempo3;
    String cadena = "F1 00 02 00 3E 21 00 00 60 00 00 00 00 0D 00 0D 00 00 00 53 01 55 01 00 00 00 00 00 00 00 15 15 FA 00 00 00 00 00 F2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTextViews();
        readString(cadena);

    }
    public void initTextViews(){
        tvNodeID = (TextView)findViewById(R.id.tvNodeID);
        tvCoordinador = (TextView)findViewById(R.id.tvCoord);
        tvNodo = (TextView)findViewById(R.id.tvNodo);
        tvBinaria1 = (TextView)findViewById(R.id.tvBinaria1);
        tvBinaria2 = (TextView)findViewById(R.id.tvBinaria2);
        tvCorriente1 = (TextView)findViewById(R.id.tvCorriente1);
        tvCorriente2 = (TextView)findViewById(R.id.tvCorriente2);
        tvCorriente3 = (TextView)findViewById(R.id.tvCorriente3);
        tvVoltaje1 = (TextView)findViewById(R.id.tvVoltaje1);
        tvVoltaje2 = (TextView)findViewById(R.id.tvVoltaje2);
        tvVoltaje3 = (TextView)findViewById(R.id.tvVoltaje3);
        tvPico1 = (TextView)findViewById(R.id.tvPico1);
        tvPico2 = (TextView)findViewById(R.id.tvPico2);
        tvPico3 = (TextView)findViewById(R.id.tvPico3);
        tvTemperatura1 = (TextView)findViewById(R.id.tvTemperatura1);
        tvTemperatura2 = (TextView)findViewById(R.id.tvTemperatura2);
        tvTemperatura3 = (TextView)findViewById(R.id.tvTemperatura3);
        tvTiempo1 = (TextView)findViewById(R.id.tvTiempo1);
        tvTiempo2 = (TextView)findViewById(R.id.tvTiempo2);
        tvTiempo3 = (TextView)findViewById(R.id.tvTiempo3);



    }

    public void readString(String message){
        String [] arrOfStr = message.split(" ");



        Integer[] intarray=new Integer[arrOfStr.length];
        int i=0;
        for (String str : arrOfStr){
            int temp1 = Integer.parseInt(arrOfStr[i].trim(), 16 );
            intarray[i] = temp1;
            print("Esto es lo que convierto: " + i + " " + temp1);
            i++;
        }



        print("Esto imprimo:");
        print(intarray.toString());
        if(Arrays.asList(arrOfStr).contains("F0")){
            print("Si tiene F0");
            //convertString(arrOfStr);
        }else{
            print("No tiene F0");
            //Colocar los valores
            tvNodeID.setText(intarray[1] + "" + intarray[2]);
            tvCoordinador.setText(String.valueOf(intarray[3]));
            tvNodo.setText(String.valueOf(intarray[4]));
            //tvCaracteres.setText(intarray[5]);
            //tvComando.setText(intarray[6]);
            tvBinaria1.setText(String.valueOf(Integer.toBinaryString(intarray[7])));
            tvBinaria2.setText(String.valueOf(Integer.toBinaryString(intarray[8])));
            tvCorriente1.setText(String.valueOf(intarray[12]*256 + intarray[11]));
            tvCorriente2.setText(String.valueOf(intarray[14]*256 + intarray[13]));
            tvCorriente3.setText(String.valueOf(intarray[16]*256 + intarray[15]));
            tvVoltaje1.setText(String.valueOf(Double.valueOf(intarray[18]*256 + intarray[17])/100));
            tvVoltaje2.setText(String.valueOf(Double.valueOf(intarray[20]*256 + intarray[19])/100));
            tvVoltaje3.setText(String.valueOf(Double.valueOf(intarray[22]*256 + intarray[21])/100));
            tvPico1.setText(String.valueOf(Double.valueOf(intarray[24]*256 + intarray[23])/100));
            tvPico2.setText(String.valueOf(Double.valueOf(intarray[26]*256 + intarray[25])/100));
            tvPico3.setText(String.valueOf(Double.valueOf(intarray[28]*256 + intarray[27])/100));
            tvTemperatura1.setText(String.valueOf(intarray[29]));
            tvTemperatura2.setText(String.valueOf(intarray[30]));
            tvTemperatura3.setText(String.valueOf(intarray[31]));
            tvTiempo1.setText(String.valueOf(intarray[32]));
            tvTiempo2.setText(String.valueOf(intarray[33]));
            tvTiempo3.setText(String.valueOf(intarray[34]));



        }

    }


    public void print(String message){
        System.out.println(message);
    }
}
