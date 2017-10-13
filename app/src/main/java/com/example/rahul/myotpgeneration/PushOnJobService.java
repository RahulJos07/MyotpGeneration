package com.example.rahul.myotpgeneration;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

/**
 * Created by VNurtureTechnologies on 06/09/16.
 */
public class PushOnJobService extends GcmTaskService {

    @Override
    public int onRunTask(TaskParams taskParams) {
        int internet = 0;
        if(NetworkUtil.isConnectedToInternet(getApplicationContext())){
            internet = 1;
            Log.d("myapp","connected to internet");
            Intent intent = new Intent(this,PushOnReceiver.class);
            intent.setAction("com.vnurture.vihas.smartcabfuelcard.EMAIL_SEND_FLAG");
            intent.putExtra(Const.INTERNET_STATUS,internet);
            Log.d(Const.TAG,"internet status: "+internet);
            sendBroadcast(intent);
        }else{
            internet = 0;
            Log.d("myapp","not connected to internet");
        }
        return GcmNetworkManager.RESULT_SUCCESS;
    }

}
