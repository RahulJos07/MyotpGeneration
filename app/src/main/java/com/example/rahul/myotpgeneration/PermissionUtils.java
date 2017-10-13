package com.example.rahul.myotpgeneration;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by vihas on 11/10/2016.
 */

public class PermissionUtils {
    Activity activity;
    int[] permissionRequestCodes;
    String[] permissions;
    private static PermissionUtils utils = new PermissionUtils();
    private PermissionUtils(){}
    public static PermissionUtils getInstance(){
        return utils;
    }

    public void initialize(Activity activity, int[] permissionRequestCodes, String permissions[], int detailsMessages[]){
        this.activity = activity;
        this.permissionRequestCodes = permissionRequestCodes;
        this.permissions = permissions;
    }

    public void checkPermissions(){
        if(permissions.length == permissionRequestCodes.length) {
            for (int i = 0; i < permissions.length ; i++) {
                if (ContextCompat.checkSelfPermission(activity, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{permissions[i]}, permissionRequestCodes[i]);
                }
            }
        }else{
            Toast.makeText(activity, "Enter Correct permissions", Toast.LENGTH_SHORT).show();
        }
    }

}
