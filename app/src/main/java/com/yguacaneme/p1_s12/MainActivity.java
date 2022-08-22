package com.yguacaneme.p1_s12;


import android.Manifest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class MainActivity extends AppCompatActivity {

    private TextView Lat, Lon;
    private EditText Cell;
    private Button GPS, SMS, WHT, SND;
    private static double lat, lon;
    private String num="HOLA";
    int x;

    private static final int PERMISSION=99;

    //Location
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Cell = findViewById(R.id.cell_t);
        Lat = findViewById(R.id.Lat_t);
        Lon = findViewById(R.id.Lon_t);
        GPS = findViewById(R.id.gps_B);
        SMS = findViewById(R.id.sms_B);
        WHT = findViewById(R.id.what_B);
        SND = findViewById(R.id.send_B);

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)
                !=PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(MainActivity.this, Manifest
                .permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new  String[]{
                    Manifest.permission.SEND_SMS},1000);
        }

        SMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lat=Double.parseDouble(Lat.getText().toString());
                lon=Double.parseDouble(Lon.getText().toString());
                num=Cell.getText().toString();
                if ((x = num.split("").length) == 10){
                    sendtxt(num,lat,lon);
                }else {
                    Toast.makeText(MainActivity.this,"Ingrese un número de télefono válido", Toast.LENGTH_SHORT).show();
                }

            }
        });

        GPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGPS();
                Lat.setVisibility(View.VISIBLE);
                Lon.setVisibility(View.VISIBLE);
            }
        });

        WHT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String latT =Lat.getText().toString();
                String lonT=Lon.getText().toString();
                num=Cell.getText().toString();
                if ((x = num.split("").length) == 10){
                    sendwhat(latT, lonT, num);
                }else {
                    Toast.makeText(MainActivity.this,"Ingrese un número de télefono válido", Toast.LENGTH_SHORT).show();
                }


            }
        });

        SND.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Ip.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void sendwhat(String latT,String lonT, String numC ) {

        boolean inst = instalada("com.whatsapp");
        if (inst){
            //Envío de mensaje Whatsapp
            Intent intent= new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://api.whatsapp.com/send?phone="+numC+"&text="+"Lat: "+latT+" Lon: "+lonT));
            startActivity(intent);
        }
        else {
            Toast.makeText(MainActivity.this,"No hay whatsapp", Toast.LENGTH_LONG).show();
        }
    }

    private boolean instalada(String s) {
        PackageManager packageManager = getPackageManager();
        boolean install;
        try {
            packageManager.getPackageInfo(s, PackageManager.GET_ACTIVITIES);
            install=true;
        }catch (PackageManager.NameNotFoundException e){
            install=false;
            e.printStackTrace();
        }
        return install;
    }

    private void sendtxt(String num, double Lat, double Lon) {
        try{
            //Envío de mensaje sms
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(num, null, "Lat: "+Lat+" Lon: "+Lon, null, null);
            Toast.makeText(MainActivity.this, "Mensaje enviado",Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(MainActivity.this, "Mensaje no enviado",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


        }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION:
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                updateGPS();
            }else {
                Toast.makeText(MainActivity.this,"Los permisos", Toast.LENGTH_LONG);
                finish();
            }
            break;
        }
    }
    private void updateGPS() {
        //Permiso GPS
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(MainActivity.this);
        if (ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION )==PackageManager.PERMISSION_GRANTED) {

            //Localizacion actual
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    update(location);
                }


            });
        }else {
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION);
            }
        }
    }
    private void update(Location location) {
        lat=location.getLatitude();
        lon=location.getLongitude();
        Lat.setText(""+lat);
        Lon.setText(""+lon);

    }

    public static double getLat(){
        return lat;
    }

    public static double getLon(){
        return lon;
    }
    
    
    
}