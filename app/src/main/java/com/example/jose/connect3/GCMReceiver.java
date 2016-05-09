package com.example.jose.connect3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import res.layout.Board;

public class GCMReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) { Bundle extras = intent.getExtras();
        //Al recibir el mensaje push llama
        //Obtiene el mensaje push enviado por GCM
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        String messageType = gcm.getMessageType(intent);
        if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            //Procesar mensaje. Se puede crear una notificación o avisar
            // a la actividad si está en primer plano del mismo modo que se
            // hace en el ejemplo de servicios

            switch (extras.getString("msgtype")){
                case "1":
                    //NUEVO MOVIMIENTO
                    break;
                case "2":
                    //MENSAJE DE USUARIO

                    if(extras.getString("content").equals("JOINED")){
                        //MENSAJE DE UNION A PARTIDA
                      Intent broad = new Intent(Board.FILTRO_JOINED);
                        broad.putExtras(intent);
                        context.sendBroadcast(broad);
                    }else{
                        //MENSAJE NORMAL
                        Toast.makeText(context,extras.getString("sender")+":"+extras.getString("content"),Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "3":
                    //MENSAJE A RONDA
                    break;
            }
            Log.d("MENSAJE", ""+extras.toString());
        } }}