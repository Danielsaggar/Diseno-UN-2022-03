package com.yguacaneme.p1_s12;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.time.ZonedDateTime;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;


public class Ip extends AppCompatActivity {

    private Button KB, AB, GB, YB, UDPB, TCPB, ALLB;
    private DatagramSocket socket = null;
    private Socket tcpsocket = null;
    private InetAddress serverAddress = null;
    private static String msj;
    private DataOutputStream output;
    boolean kv=false, al=false, ga=false, udps=true, tcps=false, alls=false;
    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        updateGPS();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);
        KB=findViewById(R.id.KGB);
        AB=findViewById(R.id.AVB);
        GB=findViewById(R.id.GCB);
        YB=findViewById(R.id.YPB);
        ALLB=findViewById(R.id.ALLB);
        UDPB=findViewById(R.id.UDPB);
        TCPB=findViewById(R.id.TCPB);


        KB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kv=state(kv, KB, KevRun);
            }

        });

        AB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                al=state(al, AB, AljRun);
            }
        });

        GB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ga=state(ga, GB, GabRun);
            }
        });

        YB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*al=state(al, AB, AljRun);*/
                Toast.makeText(Ip.this, "Habilitado en la siguiente versión",Toast.LENGTH_SHORT).show();
            }
        });

        ALLB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!alls){
                    alls=true;
                    ALLB.setBackgroundColor(Color.rgb(0, 128, 0 ));
                    al=state(false, AB, AljRun);
                    kv=state(false, KB, KevRun);
                    ga=state(false, GB, GabRun);
                }else{
                    alls=false;
                    ALLB.setBackgroundColor(Color.rgb(128, 128, 128 ));
                    al=state(true, AB, AljRun);
                    kv=state(true, KB, KevRun);
                    ga=state(true, GB, GabRun);
                }

            }
        });

        TCPB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(udps){
                    tcps=true;
                    udps=false;
                    TCPB.setBackgroundColor(Color.rgb(0, 128, 0 ));
                    UDPB.setBackgroundColor(Color.rgb(128, 128, 128 ));
                }else{
                    tcps=false;
                    udps=true;
                    UDPB.setBackgroundColor(Color.rgb(0, 128, 0 ));
                    TCPB.setBackgroundColor(Color.rgb(128, 128, 128 ));
                }
            }
        });

        UDPB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(udps){
                    tcps=true;
                    udps=false;
                    TCPB.setBackgroundColor(Color.rgb(0, 128, 0 ));
                    UDPB.setBackgroundColor(Color.rgb(128, 128, 128 ));
                }else{
                    tcps=false;
                    udps=true;
                    UDPB.setBackgroundColor(Color.rgb(0, 128, 0 ));
                    TCPB.setBackgroundColor(Color.rgb(128, 128, 128 ));
                }
            }
        });

    }

    private boolean state(boolean X, Button BT, Runnable Run) {
        if(X){
            X=false;
        }else {
            X=true;
        }
        if(X){
            BT.setBackgroundColor(Color.rgb(0, 128, 0 ));
            /*send("192.168.1.5", 52000);*/
            Run.run();
        }
        else {
            BT.setBackgroundColor(Color.rgb(128, 128, 128 ));
            /*send("192.168.1.5", 52000);*/
            mHandler.removeCallbacks(Run);
        }
        return X;
    }

    private void send(String ip, int port){
        if(udps){
            UDP(ip,port);
        }
        if(tcps) {
            TCP(ip,61000);
        }

        


    }

    private void TCP(String ip, int port) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy gfgPolicy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
        }
        try {
            tcpsocket = new Socket(ip, port);
            output = new DataOutputStream(tcpsocket.getOutputStream());
            updateGPS();
            output.writeUTF(msj);
            output.flush();
            tcpsocket.close();
        } catch (IOException e) {
            Toast.makeText(Ip.this, "e: "+e,Toast.LENGTH_SHORT).show();
            Log.e("Error",e.toString());
        }




    }

    private void UDP(String ip, int port) {
        socket=null;
        serverAddress = null;

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy gfgPolicy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
        }
        try {
            // Puerto de envío propio
            socket = new DatagramSocket(port);  //①
            // La ip de la otra parte
            serverAddress = InetAddress.getByName(ip);  //②
            updateGPS();
            byte data[] = msj.getBytes();
            // 8888 aquí está el número de puerto del receptor
            DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, port);   //③
            socket.send(packet);
        } catch (Exception e) {
            Toast.makeText(Ip.this, "e: "+e,Toast.LENGTH_SHORT).show();
            Log.e("Error",e.toString());
        }socket.close();

    }

    private Runnable KevRun = new Runnable() {
        @Override
        public void run() {
            send("201.232.167.74", 52000);
            mHandler.postDelayed(this,5000);
        }
    };

    private Runnable AljRun = new Runnable() {
        @Override
        public void run() {
            send("179.12.194.117", 52000);
            mHandler.postDelayed(this,5000);
        }
    };

    private Runnable GabRun = new Runnable() {
        @Override
        public void run() {
            send("181.235.83.22", 52000);
            mHandler.postDelayed(this,5000);
        }
    };

    public void updateGPS() {
        //Permiso GPS
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Ip.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED) {

            //Localizacion actual
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(Ip.this, new OnSuccessListener<Location>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onSuccess(Location location) {
                    update(location);
                }


            });
        }else {
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void update(Location location) {
        msj="Lat: "+location.getLatitude()+" Lon: "+location.getLongitude()+" Date: "+ZonedDateTime.now();
    }

    }






