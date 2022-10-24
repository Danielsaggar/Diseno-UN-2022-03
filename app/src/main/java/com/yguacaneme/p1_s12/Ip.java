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
    private InetAddress serverAddress = null;
    private static String msj;
    boolean kv=false, al=false, ga=false, ye=false, udps=true, tcps=false, alls=false, v1=false, v2=false;
    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        updateGPS("1");
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
                if(v1){
                    kv=state(kv, KB, KevRun);
                }
                if(v2){
                    kv=state(kv, KB, KevRun2);
                }
            }

        });

        AB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v1){
                    al=state(al, AB, AljRun);
                }
                if(v2){
                    al=state(al, AB, AljRun2);
                }
            }
        });

        GB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v1){
                    ga=state(ga, GB, GabRun);
                }
                if(v2){
                    ga=state(ga, GB, GabRun2);
                }
            }
        });

        YB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v1){
                    ye=state(ye, YB, YesRun);
                }
                if(v2){
                    ye=state(ye, YB, YesRun2);
                }

            }
        });

        ALLB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!alls){
                    alls=true;
                    ALLB.setBackgroundColor(Color.rgb(0, 128, 0 ));
                    if(v1){
                        al=state(false, AB, AljRun);
                        kv=state(false, KB, KevRun);
                        ga=state(false, GB, GabRun);
                        ye=state(false, YB, YesRun);
                    }
                    if(v2){
                        al=state(false, AB, AljRun2);
                        kv=state(false, KB, KevRun2);
                        ga=state(false, GB, GabRun2);
                        ye=state(false, YB, YesRun2);
                    }
                }else{
                    alls=false;
                    ALLB.setBackgroundColor(Color.rgb(128, 128, 128 ));
                    if(v1){
                        al=state(true, AB, AljRun);
                        kv=state(true, KB, KevRun);
                        ga=state(true, GB, GabRun);
                        ye=state(true, YB, YesRun);
                    }
                    if(v2){
                        al=state(true, AB, AljRun);
                        kv=state(true, KB, KevRun);
                        ga=state(true, GB, GabRun);
                        ye=state(true, YB, YesRun);
                    }

                }

            }
        });

        TCPB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!v2){
                    TCPB.setBackgroundColor(Color.rgb(0, 128, 0 ));
                    v2=true;
                }else{
                    TCPB.setBackgroundColor(Color.rgb(128, 128, 128 ));
                    v2=false;
                }
            }
        });

        UDPB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!v1){
                    UDPB.setBackgroundColor(Color.rgb(0, 128, 0 ));
                    v1=true;
                }else{
                    UDPB.setBackgroundColor(Color.rgb(128, 128, 128 ));
                    v1=false;
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
            Run.run();
        }
        else {
            BT.setBackgroundColor(Color.rgb(128, 128, 128 ));
            mHandler.removeCallbacks(Run);
        }
        return X;
    }

    private void send(String ip, int port, String vehicle){
        if(udps){
            UDP(ip,port, vehicle);
        }
        if(tcps) {
            Toast.makeText(Ip.this, "No deberías estar aquí",Toast.LENGTH_SHORT).show();
        }

        


    }


    private void UDP(String ip, int port, String vehicle) {
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
            updateGPS(vehicle);
            byte data[] = msj.getBytes();
            // 8888 aquí está el número de puerto del receptor
            DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, port);   //③
            socket.send(packet);
        } catch (Exception e) {
            Toast.makeText(Ip.this, "e: "+e,Toast.LENGTH_SHORT).show();
            Log.e("Error",e.toString());
        }socket.close();

    }

    private Runnable YesRun = new Runnable() {
        @Override
        public void run() {
            send("34.230.132.163", 52000, "1");
            mHandler.postDelayed(this,5000);
        }
    };

    private Runnable YesRun2 = new Runnable() {
        @Override
        public void run() {
            send("34.230.132.163", 52000, "2");
            mHandler.postDelayed(this,5000);
        }
    };

    private Runnable KevRun = new Runnable() {
        @Override
        public void run() {
            send("44.207.244.176", 52000, "1");
            mHandler.postDelayed(this,5000);
        }
    };

    private Runnable KevRun2 = new Runnable() {
        @Override
        public void run() {
            send("44.207.244.176", 52000, "2");
            mHandler.postDelayed(this,5000);
        }
    };

    private Runnable AljRun = new Runnable() {
        @Override
        public void run() {
            send("34.193.1.204", 52000,"1");
            mHandler.postDelayed(this,5000);
        }
    };

    private Runnable AljRun2 = new Runnable() {
        @Override
        public void run() {
            send("34.193.1.204", 52000,"2");
            mHandler.postDelayed(this,5000);
        }
    };

    private Runnable GabRun = new Runnable() {
        @Override
        public void run() {
            send("54.208.54.187", 52000,"1");
            mHandler.postDelayed(this,5000);
        }
    };

    private Runnable GabRun2 = new Runnable() {
        @Override
        public void run() {
            send("54.208.54.187", 52000,"2");
            mHandler.postDelayed(this,5000);
        }
    };

    public void updateGPS(String ve) {
        //Permiso GPS
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Ip.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED) {

            //Localizacion actual
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(Ip.this, new OnSuccessListener<Location>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onSuccess(Location location) {
                    update(location,ve);
                }


            });
        }else {
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void update(Location location, String vehicle) {
        String F = ZonedDateTime.now().toString();
        String[] part1 = F.split("T");
        String[] part2 = part1[1].split("\\.");
        msj=""+location.getLatitude()+"%"+location.getLongitude()+"%"+part1[0]+"%"+part2[0]+"%"+vehicle;
        //Toast.makeText(Ip.this, "Mensaje: "+ msj,Toast.LENGTH_SHORT).show();
    }


}






