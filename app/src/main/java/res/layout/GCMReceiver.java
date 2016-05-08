package res.layout;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
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
            Log.d("MENSAJE", ""+extras.toString());
        } }}