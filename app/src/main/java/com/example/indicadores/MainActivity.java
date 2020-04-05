package com.example.indicadores;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.akhgupta.easylocation.EasyLocationAppCompatActivity;
import com.akhgupta.easylocation.EasyLocationRequest;
import com.akhgupta.easylocation.EasyLocationRequestBuilder;
import com.google.android.gms.location.LocationRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;


import static java.lang.Long.toBinaryString;
import static java.lang.Long.valueOf;

public class MainActivity extends EasyLocationAppCompatActivity implements View.OnClickListener {

    TextView tvLiberado,tvEstado, tvNodeID,tvVoltBatUTR, tvRx, tvTx, tvAlarma, tvAlarma1, tvAlarma2, tvAlarma3,  tvBinaria1, tvBinaria2, tvCorriente1, tvCorriente2, tvCorriente3, tvVoltaje1, tvVoltaje2, tvVoltaje3, tvPico1, tvPico2, tvPico3, tvTemperatura1, tvTemperatura2, tvTemperatura3, tvTiempo1, tvTiempo2, tvTiempo3, tvConnect;
    String cadena = "F1 00 02 00 3E 21 00 00 60 00 00 00 00 0D 00 0D 00 00 00 53 01 55 01 00 00 00 00 00 00 00 15 15 FA 00 00 00 00 00 F2";
    Button btnConnect, btnAbrir, btnCerrar, btnParar, btnCoordenadas;
    boolean connected = false;

    private static final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    public static BluetoothDevice device;
    public static BluetoothSocket socket;
    public static OutputStream outputStream;
    public static InputStream inputStream;

    static Button btnDiagnostico;
    static Handler handler = new Handler();


    static String tokens[];
    static String tokensRSSI[];

    static boolean socketConectado;
    boolean stopThread;

    static String s;

    static String a;
    static String b;

    static Thread thread;

    String controlPassword = "OK";
    float latitudeValue, longitudeValue;
    private int timeInterval = 3000;
    private int fastestTimeInterval = 3000;
    private boolean runAsBackgroundService = false;
    StringBuilder sb, sbAux;
    int control;
    String uno, dos,latitudeUno, latitudeDos, longitudUno, longitudDos;
    Boolean boolDos;
    static boolean boolPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolPassword = false;
        initTextViews();
        //readString(cadena); //Leer del string fijo
        // construct a new instance of SimpleLocation


    }
    public void initTextViews(){
        tvLiberado = (TextView)findViewById(R.id.tvLiberado);
        tvEstado = (TextView)findViewById(R.id.tvEstado);
        tvNodeID = (TextView)findViewById(R.id.tvNodeID);
        tvRx = (TextView)findViewById(R.id.tvRx);
        tvTx = (TextView)findViewById(R.id.tvTx);
        tvVoltBatUTR = (TextView)findViewById(R.id.tvVoltBatUTR);
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
        tvConnect = (TextView)findViewById(R.id.tvConnect);
        tvAlarma = (TextView)findViewById(R.id.tvAlarma);
        tvAlarma1 = (TextView)findViewById(R.id.tvAlarma1);
        tvAlarma2 = (TextView)findViewById(R.id.tvAlarma2);
        tvAlarma3 = (TextView)findViewById(R.id.tvAlarma3);
        btnConnect = (Button)findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(this);
        btnAbrir = (Button)findViewById(R.id.btnAbrir);
        btnAbrir.setOnClickListener(this);
        btnCerrar = (Button)findViewById(R.id.btnCerrar);
        btnCerrar.setOnClickListener(this);
        btnParar = (Button)findViewById(R.id.btnParar);
        btnParar.setOnClickListener(this);
        btnCoordenadas = (Button)findViewById(R.id.btnCoordenadas);
        btnCoordenadas.setOnClickListener(this);
        control = 0;
        uno =  "";
        dos = "";
        boolDos = false;
        latitudeValue = (float) 0;
        longitudeValue = (float) 0;
    }

    public void readString(String message){
        String [] arrOfStr = message.split(" ");

        Integer[] intarray=new Integer[arrOfStr.length];
        int i=0;
        for (String str : arrOfStr){
            int temp1 = Integer.parseInt(arrOfStr[i].trim(), 16 );//De hex a decimal
            intarray[i] = temp1;
            print("Esto es lo que convierto: " + i + " " + temp1);
            i++;
        }

        if(Arrays.asList(arrOfStr).contains("F0")){
            print("Si tiene F0");
            //convertString(arrOfStr);

        }else{
            print("No tiene F0");

            //Colocar los valores
            tvNodeID.setText(intarray[1] + "" + intarray[2]);
            tvRx.setText(String.valueOf(intarray[3]));
            tvTx.setText(String.valueOf(intarray[4]));

            String binario7 = Integer.toBinaryString(intarray[7]);
            for(int a=binario7.length(); a < 8; a++){
                binario7 = '0' + binario7;
            }
            //print("Esto tiene el binary7: " + binario7);
            StringBuilder sb = new StringBuilder(binario7);
            String binario7Final = sb.reverse().toString();

            String binario8 = Integer.toBinaryString(intarray[8]);
            for(int a=binario8.length(); a < 8; a++){
                binario8 = '0' + binario8;
            }
            //print("Esto tiene el binary8: " + binario8);
            StringBuilder sb2 = new StringBuilder(binario8);
            String binario8Final = sb2.reverse().toString();

            String binario9 = Integer.toBinaryString(intarray[9]);
            for(int a=binario9.length(); a < 8; a++){
                binario9 = '0' + binario9;
            }
            StringBuilder sb3 = new StringBuilder(binario9);
            String binario9Final = sb3.reverse().toString();
            //print("Esto tiene el binary9: " + binario9Final);


            String binarioTotal = binario7Final + binario8Final + binario9Final;
            print("Esto tiene el total: " + binarioTotal);
            //Binarios de Alarmas
            if(binarioTotal.charAt(0) == '1'){
                tvAlarma1.setText("Prueba");
            }else{
                tvAlarma1.setText("");
            }
            if(binarioTotal.charAt(1) == '1'){
                tvAlarma1.setText("CC entre Fases");
            }else{
                tvAlarma1.setText("");
            }
            if(binarioTotal.charAt(2) == '1'){
                tvAlarma1.setText("Puesta a Tierra BC");
            }else{
                tvAlarma1.setText("");
            }
            if(binarioTotal.charAt(3) == '1'){
                tvAlarma1.setText("Puesta a Tierra AC");
            }else{
                tvAlarma1.setText("");
            }
            if(binarioTotal.charAt(4) == '1'){
                tvAlarma2.setText("Prueba");
            }else{
                tvAlarma2.setText("");
            }
            if(binarioTotal.charAt(5) == '1'){
                tvAlarma2.setText("CC entre Fases");
            }else{
                tvAlarma2.setText("");
            }
            if(binarioTotal.charAt(6) == '1'){
                tvAlarma2.setText("Puesta a Tierra BC");
            }else{
                tvAlarma2.setText("");
            }
            if(binarioTotal.charAt(7) == '1'){
                tvAlarma2.setText("Puesta a Tierra AC");
            }else{
                tvAlarma2.setText("");
            }
            if(binarioTotal.charAt(8) == '1'){
                tvAlarma3.setText("Prueba");
            }else{
                tvAlarma3.setText("");
            }
            if(binarioTotal.charAt(9) == '1'){
                tvAlarma3.setText("CC entre Fases");
            }else{
                tvAlarma3.setText("");
            }
            if(binarioTotal.charAt(10) == '1'){
                tvAlarma3.setText("Puesta a Tierra BC");
            }else{
                tvAlarma3.setText("");
            }
            if(binarioTotal.charAt(11) == '1'){
                tvAlarma3.setText("Puesta a Tierra AC");
            }else{
                tvAlarma3.setText("");
            }
            if(binarioTotal.charAt(12) == '1'){
                tvAlarma1.setText("Conexión");
            }else{
                tvAlarma1.setText("");
            }
            if(binarioTotal.charAt(13) == '1'){
                tvAlarma2.setText("Conexión");
            }else{
                tvAlarma2.setText("");
            }
            if(binarioTotal.charAt(14) == '1'){
                tvAlarma3.setText("Conexión");
            }else{
                tvAlarma3.setText("");
            }
            if(binarioTotal.charAt(15) == '1'){
                tvAlarma.setText("Voltaje");
            }else{
                tvAlarma.setText("");
            }
            if(binarioTotal.charAt(19) == '1'){
                tvAlarma.setText("Proceso");
            }else{
                tvAlarma.setText("");
            }

            //Estados
            if(binarioTotal.charAt(17) == '1'){
                tvEstado.setText("Cuchilla Movida");
            }else{
                if(binarioTotal.charAt(16) == '0'){
                    tvEstado.setText("Cerrado");
                }else{
                    tvEstado.setText("Abierto");
                }
            }
            if(binarioTotal.charAt(18) == '0'){
                tvLiberado.setText("No");
            }else{
                tvLiberado.setText("Sí");
            }

            tvCorriente1.setText(String.valueOf(intarray[11]*256 + intarray[10]));
            tvCorriente2.setText(String.valueOf(intarray[13]*256 + intarray[12]));
            tvCorriente3.setText(String.valueOf(intarray[15]*256 + intarray[14]));
            tvVoltaje1.setText(String.valueOf(Double.valueOf(intarray[17]*256 + intarray[16])/100));
            tvVoltaje2.setText(String.valueOf(Double.valueOf(intarray[19]*256 + intarray[18])/100));
            tvVoltaje3.setText(String.valueOf(Double.valueOf(intarray[21]*256 + intarray[20])/100));
            tvPico1.setText(String.valueOf(Double.valueOf(intarray[23]*256 + intarray[22])/100));
            tvPico2.setText(String.valueOf(Double.valueOf(intarray[25]*256 + intarray[24])/100));
            tvPico3.setText(String.valueOf(Double.valueOf(intarray[27]*256 + intarray[26])/100));
            tvVoltBatUTR.setText(String.valueOf(Double.valueOf(intarray[29]*256 + intarray[28])/100));
            tvTemperatura1.setText(String.valueOf(intarray[30]));
            tvTemperatura2.setText(String.valueOf(intarray[31]));
            tvTemperatura3.setText(String.valueOf(intarray[32]));
            tvTiempo1.setText(String.valueOf(intarray[33]));
            tvTiempo2.setText(String.valueOf(intarray[34]));
            tvTiempo3.setText(String.valueOf(intarray[35]));
        }
    }
    //011100000000001100000000
    //001000000000001100000000

    public void print(String message){
        System.out.println(message);
    }

    //Identifica el device BT
    public boolean BTinit()
    {
        boolean found = false;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null) //Checks if the device supports bluetooth
        {
            Toast.makeText(getApplicationContext(), "Este dispositivo no soporta bluetooth", Toast.LENGTH_SHORT).show();
        }
        if(!bluetoothAdapter.isEnabled()) //Checks if bluetooth is enabled. If not, the program will ask permission from the user to enable it
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter,0);
            try
            {
                Thread.sleep(1000);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if(bondedDevices.isEmpty()) //Checks for paired bluetooth devices
        {
            Toast.makeText(getApplicationContext(), "Favor de conectar un dispositivo", Toast.LENGTH_SHORT).show();
        }
        else
        {
            for(BluetoothDevice iterator : bondedDevices)
            {

                //Suponiendo que solo haya un bondedDevice
                device = iterator;
                found = true;
                //Toast.makeText(getApplicationContext(), "Conectado a: " + device.getName(), Toast.LENGTH_SHORT).show();
            }
        }
        return found;
    }
    //Conexión al device BT
    public boolean BTconnect()
    {
        try
        {
            conectar();

        }
        catch(IOException e)
        {
            Toast.makeText(getApplicationContext(), "Conexión no exitosa", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            connected = false;
        }

        return connected;
    }

    public void conectar() throws IOException{
        socket = device.createRfcommSocketToServiceRecord(PORT_UUID); //Crea un socket para manejar la conexión
        socket.connect();
        socketConectado = true;
        Log.d("Socket ", String.valueOf(socket.isConnected()));
        Toast.makeText(getApplicationContext(), "Conexión exitosa", Toast.LENGTH_SHORT).show();
        connected = true;
        tvConnect.setText("Conectado a " + device.getName());
        btnConnect.setText("Desconectar módulo Bluetooth");
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();
        beginListenForData();

        //waitMs(5000);
        //closeSocket();

    }

    public void desconectarBluetooth() throws IOException{
        //Desconectar bluetooth
        if(socketConectado){
            System.out.println("Socket Conectado");
            outputStream.close();
            outputStream = null;
            inputStream.close();
            inputStream = null;
            socket.close();
            socket = null;
        }
        resetFields();
        connected = false;
        tvConnect.setText("");
        btnConnect.setText("Conectar a módulo Bluetooth");
        device = null;
        stopThread = true;
        socketConectado = false;
        boolPassword = false; //Para que lo vuelva a pedir la siguiente vez que se conecte a otro equipo
    }

    void beginListenForData() {
        stopThread = false;
        final String[] uno = {null};
        final String[] dos = { null };
        thread = new Thread(new Runnable() {
            public void run() {
                while(!Thread.currentThread().isInterrupted() && !stopThread) {
                    try {
                        //waitMs(1000);
                        final int byteCount = inputStream.available();
                        if(byteCount > 0) {


                            byte[] packetBytes = new byte[byteCount];
                            inputStream.read(packetBytes);

                            sb = new StringBuilder();
                            for (byte b : packetBytes) {
                                sb.append(String.format("%02X ", b));
                                //print("Esto tiene en el append: " + sb.toString());
                            }
                            if(control == 0){
                                uno[0] = sb.toString();
                                //print("Uno: " + uno[0]);
                            }else{
                                dos[0] = sb.toString();
                                //print("Dos: " + dos[0]);
                                print("Esto es todo completo: " + uno[0] + dos[0]);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Status
                                        readString(uno[0] + dos[0]); //String final que se lee y analiza

                                    }
                                });
                            }
                            //System.out.println("Hex as string: " + sb.toString()); //Hex as string
                            if(sb.toString().contains("F1") && !sb.toString().contains("F2")){
                                //print("Tiene F1 pero no F2");
                                control = 1;
                            }else if(!sb.toString().contains("F1") && sb.toString().contains("F2")){
                               // print("Tiene F2 pero no F1");
                                control = 0;
                                boolDos = true;
                                //print(uno + dos);

                            }else if(sb.toString().contains("F1") && sb.toString().contains("F2")){
                                //Mandarlo asi como está
                                //print("Ya estaba completo: " + sb.toString());
                            }

                        }

                    }
                    catch (IOException ex) {
                        stopThread = true;
                    }
                }
                System.out.println("Stop thread es true");
            }
        });
        thread.start();
    }

    public void resetFields(){
        tvNodeID.setText("");
        tvCorriente1.setText("0");
        tvCorriente2.setText("0");
        tvCorriente3.setText("0");
        tvVoltaje1.setText("0");
        tvVoltaje2.setText("0");
        tvVoltaje3.setText("0");
        tvPico1.setText("0");
        tvPico2.setText("0");
        tvPico3.setText("0");
        tvTemperatura1.setText("0");
        tvTemperatura2.setText("0");
        tvTemperatura3.setText("0");
        tvTiempo1.setText("0");
        tvTiempo2.setText("0");
        tvTiempo3.setText("0");
        tvVoltBatUTR.setText("");
        tvLiberado.setText("");
        tvRx.setText("");
        tvTx.setText("");
        tvEstado.setText("");
    }


    private void showToast(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConnect:
                if (!connected) {
                    if (BTinit()) {
                        BTconnect();
                    }
                } else {
                    try {
                        desconectarBluetooth();
                    } catch (IOException ex) {
                    }
                }
                break;

            case R.id.btnAbrir:
                if (connected) {
                    if(boolPassword){
                        sendOpen();
                    }else{
                        showPasswordDialog("Ingrese la contraseña", "");
                    }

                } else {
                    showToast("Bluetooth Desconectado");
                }
                break;
            case R.id.btnCerrar:
                if (connected) {
                    if(boolPassword){
                        sendClose();
                    }else{
                        showPasswordDialog("Ingrese la contraseña", "");
                    }

                } else {
                    showToast("Bluetooth Desconectado");
                }
                break;
            case R.id.btnParar:
                if (connected) {
                    if(boolPassword){
                        sendParar();
                    }else{
                        showPasswordDialog("Ingrese la contraseña", "");
                    }
                } else {
                    showToast("Bluetooth Desconectado");
                }
                break;
            case R.id.btnCoordenadas:
                if (connected) {
                    requestSingleLocation();
                } else {
                    showToast("Bluetooth Desconectado");
                }
                break;
        }
    }
    void sendOpen(){
        try {
            System.out.println("Estoy en sendOpen");
            String msg = "$OPEN_SWITCH&";
            outputStream.write(msg.getBytes());
        } catch (IOException ex) {
        }
    }
    void sendClose(){
        try {
            System.out.println("Estoy en sendClose");
            String msg = "$CLOSE_SWITCH&";
            outputStream.write(msg.getBytes());
        } catch (IOException ex) {
        }
    }
    void sendParar(){
        try {
            System.out.println("Estoy en sendClose");
            String msg = "$STOP_SWITCH&";
            outputStream.write(msg.getBytes());
        } catch (IOException ex) {
        }
    }

    void sendLocation(String location) throws IOException
    {
        try {
            System.out.println("Estoy en el sendLocation");
            String msg = location;
            outputStream.write(msg.getBytes());
        } catch (IOException ex) {
        }

    }

    void sendPassword(String pass) throws IOException
    {
        System.out.println("El control = Pass");
        System.out.println("Estoy en el sendPassword");
        String msg = "$PASS=" + pass + ",& ";
        System.out.println("Este es el pass que mando " + msg);
        outputStream.write(msg.getBytes());
        boolPassword = true;
    }

    public void showPasswordDialog(final String title, final String message){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(getApplicationContext());
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
        edittext.setTextColor(getResources().getColor(R.color.black));
        edittext.setRawInputType(Configuration.KEYBOARD_12KEY);
        alert.setMessage(message);
        alert.setTitle(title);
        alert.setView(edittext);
        alert.setPositiveButton("Ingresar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String pass = edittext.getText().toString();
                //Pasar el pass con este comando = $PASS=12345,& regresa OK si es correcto o error si incorrect
                try{
                    sendPassword(pass);
                }catch(IOException e){
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cerrar el input dialog
                dialog.dismiss();
            }
        });
        alert.show();
    }


    @Override
    protected void onResume() {
        super.onResume();

        // make the device update its location

        // ...
    }

    @Override
    protected void onPause() {
        // stop location updates (saves battery)


        // ...

        super.onPause();
    }
    @Override
    public void onLocationReceived(Location location) {
        //showToast(location.getLatitude() + "," + location.getLongitude());
        latitudeValue = 0;
        longitudeValue = 0;
        latitudeValue = (float)location.getLatitude();
        longitudeValue = (float)location.getLongitude();
        System.out.println("Esto es lat long: " + latitudeValue + " " + longitudeValue);
        showToast("Lat: " + latitudeValue + " Long: " + longitudeValue);
        String latitude = Float.toString(latitudeValue);
        String longitud = Float.toString(longitudeValue);
        latitudeUno = latitude.substring(0, latitude.indexOf("."));
        latitudeDos = latitude.substring(latitude.indexOf(".") + 1, latitude.length());
        longitudUno = longitud.substring(0, longitud.indexOf("."));
        longitudDos = longitud.substring(longitud.indexOf(".") + 1, longitud.length());
        System.out.println("Estos son los numeros separados por el punto: " + latitudeUno + " " + latitudeDos + " " + longitudUno + " " + longitudDos + " " );
        //$GPS=,-10014,0980,2608,8791,&
        String coordenasAMandar = "$GPS=," + latitudeUno + "," + latitudeDos + "," + longitudUno + "," + longitudDos + ",&";
        System.out.println("Esta es la cadena a mandar: " + coordenasAMandar);

        try {
            sendLocation(coordenasAMandar);
        } catch (IOException ex) {
        }

    }

    @Override
    public void onLocationPermissionGranted() {
        showToast("Permiso Otorgado");
    }

    @Override
    public void onLocationPermissionDenied() {
        showToast("Permiso Negado");
    }

    @Override
    public void onLocationProviderEnabled() {
        print("Location services are now ON");

    }

    @Override
    public void onLocationProviderDisabled() {
        print("Location services are still Off");
    }

    public void requestSingleLocation(){
        LocationRequest locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000)
                .setFastestInterval(5000);
        EasyLocationRequest easyLocationRequest = new EasyLocationRequestBuilder()
                .setLocationRequest(locationRequest)
                .setFallBackToLastLocationTime(3000)
                .build();
        requestSingleLocationFix(easyLocationRequest);
    }
}
