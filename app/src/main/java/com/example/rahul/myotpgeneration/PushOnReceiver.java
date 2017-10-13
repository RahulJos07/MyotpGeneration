package com.example.rahul.myotpgeneration;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmNetworkManager;

import java.util.Calendar;

/**
 * Created by VNurtureTechnologies on 03/09/16.
 */
public class PushOnReceiver extends BroadcastReceiver {
//    public static ConnectivityReceiverListener connectivityReceiverListener;
   // private boolean isConnected = false;
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = context.getSharedPreferences(Const.SHAREDPREFERENCE_MAIN, Context.MODE_PRIVATE);
        String action = intent.getAction();
        if (action.equals("android.intent.action.BOOT_COMPLETED")) {
            if (!isPushOnServiceRunning(context,PushOnService.class)) {
                Intent intent1 = new Intent(context,PushOnService.class);
                context.startService(intent1);
            }
            Toast.makeText(context, "Boot Completed", Toast.LENGTH_SHORT).show();
            int hour = Calendar.getInstance().getTime().getHours();
            if(preferences.getInt(Const.EMAIL_SUCCESSFUL_SEND,-1) == 1){
                if(Utils.isDateMatched(preferences.getString(Const.EMAIL_SUCCESSFUL_SEND_DATE,null))){
                    // date matched
                    // switch on the phone on same day
                    Log.d("myapp","switch on the phone on same day.... emails are already sent");
                }else{
                    // date not matched
                    // switch on the phone on next day
                    // check for the hour. if time passed then sms email send trigger flag set to 0 and email send
                    if(hour > Const.ALARM_SET_HOUR){
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt(Const.EMAIL_SUCCESSFUL_SEND,0);
                        editor.apply();
                        // sending broadcast to alarm
                        //Intent intent1 = new Intent(context,SmartFuelAlarmReceiver.class);
                        //context.sendBroadcast(intent1);
                    }
                }
            }
            Log.d("myapp", "boot completed");
        } else if (action.equals("com.vnurture.vihas.smartcabfuelcard.PUSH_ON")) {
            if (!isPushOnServiceRunning(context,PushOnService.class)) {
                Intent intent1 = new Intent(context,PushOnService.class);
                context.startService(intent1);
            }
            Log.d("myapp", "received broadcast");
        }
//
        if(action.equals("com.vnurture.vihas.smartcabfuelcard.EMAIL_SEND_FLAG")){
            GcmNetworkManager gcmNetworkManager = GcmNetworkManager.getInstance(context);
            gcmNetworkManager.cancelAllTasks(PushOnJobService.class);
            Log.d(Const.TAG,"GCM Task for internet off");
            new PushOnService.StoreInBackground(context,preferences).execute();
        }

        if(action.equals("com.vnurture.vihas.smartcabfuelcard.EXISTING_FILE_SEND")){
            GcmNetworkManager gcmNetworkManager = GcmNetworkManager.getInstance(context);
            gcmNetworkManager.cancelAllTasks(FileWatcherService.class);
            Log.d(Const.TAG,"PushOnReceiver: File watcher service stop");
            // after receive request cheeck for internet if internet is not avaiable the start network
            // network monitor and close file monitor
            if(preferences.getInt(Const.EMAIL_SUCCESSFUL_SEND,-1) == 0) {
                if (NetworkUtil.isConnectedToInternet(context)) {
                    // getting dir path
                    String dirPath = preferences.getString(Const.DIR_PATH, null);

                    if (NetworkUtil.getFilePathWithName(dirPath)) {
                        String serverUri = context.getString(R.string.server_mail_url);
                        Log.d(Const.TAG, "File watcher: Previous file exists");
                        // checking file availability in directory
                        Log.d(Const.TAG, "File watcher: Sending existing file...");
                        int count = CounterHolder.filePathArrayList.size();
                        for (int i = 0; i < count; i++) {
                            // file upload path
                            String sourceFileUri = CounterHolder.filePathArrayList.get(i);
                            // file upload name
                            String uploadFileName = CounterHolder.fileNameArrayList.get(i);
                            String uploadFilePath = dirPath + "/";
                            // file upload
                            new NetworkUtil.FileUploadInBackground(context, sourceFileUri, uploadFilePath, uploadFileName, serverUri, true).execute();
                        }
                    }else{
                        // create new request for mail
                        // this will only call when folder will not even call
                        Log.d("myapp","PushOnReceiver: new request for email");
                        new PushOnService.StoreInBackground(context,preferences).execute();
                    }
                } else {
                    NetworkUtil.startNetworkMonitor(context);
                }
            }else{
                Log.d("myapp","FileWatcher: email is already send");
            }
        }
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
