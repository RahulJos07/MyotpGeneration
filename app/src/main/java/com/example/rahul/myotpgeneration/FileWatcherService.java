package com.example.rahul.myotpgeneration;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

/**
 * Created by vihas on 11/19/2016.
 */

public class FileWatcherService extends GcmTaskService {
    @Override
    public int onRunTask(TaskParams taskParams) {
        Log.d("myapp","FileWatcherService: file watcher service started");
        if (!isPushOnServiceRunning(this,PushOnService.class)) {
            Intent intent1 = new Intent(this,PushOnService.class);
            this.startService(intent1);
            Log.d("myapp","FileWatcherService: stopped service started");
        }else{
            Log.d("myapp","FileWatcherService: background service is running");
        }
        if(Utils.isDirExists()){
            // sending broadcast
            Intent sendFilesIntent = new Intent(this,PushOnReceiver.class);
            sendFilesIntent.setAction("com.vnurture.vihas.smartcabfuelcard.EXISTING_FILE_SEND");
            sendBroadcast(sendFilesIntent);
        }else{
            Log.d("myapp","FileWatcherService: folder not found");
        }
        return GcmNetworkManager.RESULT_SUCCESS;
    }


    // check push on back ground service is running or not
    // if it is not running then this method will start the service
    private boolean isPushOnServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
