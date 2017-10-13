package com.example.rahul.myotpgeneration;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


/**
 * Created by VNurtureTechnologies on 03/09/16.
 */
public class PushOnService extends Service {
    boolean isNewAlarmSet = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Toast.makeText(this, "service started", Toast.LENGTH_SHORT).show();
        Log.d("myapp","service started");
        // 8/10/2016 updated
        if(!isNewAlarmSet){
            isNewAlarmSet = Utils.setAlarm(this);
        }else{
            String message = "MorningAlarmService: Alarm already set";
            Utils.makeLogAndEntry(message);
        }
        // file watcher start for testing
//        NetworkUtil.startFileWatcher(this);

        Log.d("myapp","test set");
        SharedPreferences preferences = getSharedPreferences(Const.SHAREDPREFERENCE_MAIN,MODE_PRIVATE);
        int firstTimeFlag = preferences.getInt(Const.FIRST_TIME,0);
        if(firstTimeFlag == 1) {
            //setting alarm for 23:59:59 am to invoke the method
//            Utils.setOnetimeTimer(this);
//            Utils.setDailyAlarm(this,8,0);
//            Utils.setDailyAlarm(this);  // testing only
            Log.d("myapp","alarm started");
            // check for internet connection. if network is connected then it will go to background
            // for further process else Netwrok moniter will start to check network availability.
            if(NetworkUtil.isConnectedToInternet(this)) {
                new StoreInBackground(this,preferences).execute();
            }else{
                NetworkUtil.startNetworkMonitor(getApplicationContext());
            }

        }else{
            // emailSendflag = 0 if previous email is not sent
            // emailSendflag = 1 if previous email is sent
            // This flag is use for sending email if email is not sent successfully
            int emailSendflag = preferences.getInt(Const.EMAIL_SUCCESSFUL_SEND,0);
            if(emailSendflag == 0){
                // sending email
                if(NetworkUtil.isConnectedToInternet(this)) {
                    new StoreInBackground(this,preferences).execute();
                }else{
                    NetworkUtil.startNetworkMonitor(getApplicationContext());
                }
                Log.d(Const.TAG,"Previous email sent");
            }else{
                Log.d(Const.TAG,"Previous all email were sent");
            }
        }

        return START_STICKY;
    }


    public static class StoreInBackground extends AsyncTask<Void,Void,Void> {
        Context context;
        SharedPreferences preferences;
        String dirPath = null;
        public StoreInBackground(Context context, SharedPreferences preferences) {
            this.context = context;
            this.preferences = preferences;
        }

        @Override
        protected Void doInBackground(Void... params) {

            int flag = preferences.getInt(Const.FIRST_TIME,0);
            if(flag == 1) {
                if (storeContacts()) {
                    Log.d(Const.TAG, "pushonservice - All contacts read completed");

                }
                if (storeMessages(flag)) {
                    Log.d(Const.TAG, "pushonservice - All sms read completed");
                }

            }else {
                if (storeMessages(flag)) {
                    Log.d(Const.TAG, "pushonservice - Today's sms read completed");
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(CounterHolder.smsOlaModelArrayList.size() > 0 || CounterHolder.smsBkModelArrayList.size() > 0 || CounterHolder.smsUberModelArrayList.size() > 0 || CounterHolder.smsPromArrayList.size() > 0) {
                // uploading xml file to server call
                if (uploadXMLFiles()) {
                    // if files are uploaded to server
                    new NetworkUtil.SendEmails(context).execute();
                    Log.d(Const.TAG, "pushonservice - files are uploaded to server");
                } else {
                    // if files are not uploaded to server
                    Log.d(Const.TAG, "pushonservice - uploading fail");
                }
            }
            else{
                // empty attachment mail send
                CounterHolder.filePathArrayList = new ArrayList<>();
                CounterHolder.fileNameArrayList = new ArrayList<>();
                new NetworkUtil.SendEmails(context).execute();
            }

        }

        //contacts
        private boolean storeContacts() {
            // initilization
            CounterHolder.contactModelArrayList = new ArrayList<>();

            ContentResolver cr = context.getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
            try{
                assert cur != null;
                if (cur.getCount() > 0) {
                    while (cur.moveToNext()) {
                        String id = cur.getString(
                                cur.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cur.getString(cur.getColumnIndex(
                                ContactsContract.Contacts.DISPLAY_NAME));

                        if (cur.getInt(cur.getColumnIndex(
                                ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                            Cursor pCur = cr.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                                    new String[]{id}, null);
                            assert pCur != null;
                            while (pCur.moveToNext()) {
                                String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                            Log.d(Const.TAG, name+" "+phoneNo);
                                ContactModel model = new ContactModel();
                                model.setContactName(name);
                                model.setContactNumber(phoneNo);
                                CounterHolder.contactModelArrayList.add(model);


                            }
                            pCur.close();
                        }
                    }
                }else{
                    Log.d("myapp","no contacts found");
                }
                // setting format for file

                String phoneNo = preferences.getString(Const.PHONE_NUMBER,null);
                DateFormat dateFormatter = new SimpleDateFormat("ddMMyyyy");
                String finaldatestr = dateFormatter.format(Calendar.getInstance().getTime());
                Log.d("myapp","Count of contacts" + CounterHolder.contactModelArrayList.size());
                if(CounterHolder.contactModelArrayList.size() > 0){
                    String finalName = "contacts-"+phoneNo+"-"+finaldatestr+".xls";
//                    createContactXmlFile(finalName,CounterHolder.contactModelArrayList);
                    createContactxlsFile(finalName,CounterHolder.contactModelArrayList);
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }

            return false;
        }


        // sms
        // flag will check if service started for first time or not
        // emailDate is for read specific day message
        private boolean storeMessages(int flag) {
            /**
             *  isfrom values : 1 - ola, 2 - uber , 3 - bank
             *  initializing
             */
            int isFrom = 0;
            boolean isPromp = true;
            CounterHolder.smsOlaModelArrayList = new ArrayList<>();
            CounterHolder.smsUberModelArrayList = new ArrayList<>();
            CounterHolder.smsBkModelArrayList = new ArrayList<>();
            CounterHolder.smsPromArrayList = new ArrayList<>();
            Cursor cursor = null;

            if(flag == 1){
                cursor = Utils.readSMS(context);
            }else{
                String yesterdayDate = Utils.getYesterDayDate();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Const.EMAIL_SEND_DATE,yesterdayDate);
                editor.apply();
                    // return sms of current date
                    cursor = Utils.readSMS(context, yesterdayDate, "00:00:00");
                    Log.d(Const.TAG,"pushonservice - date matched");

            }

            if(cursor != null) {

                cursor.moveToFirst();

                try {

                    while (!cursor.isAfterLast() && !cursor.isBeforeFirst()) {
                        String messageAddress = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                        if (isCorporateMessage(messageAddress)) {
                            String matchString[] = messageAddress.split("-");
                            if (matchString[0].length() == 2) {

//                                        Log.d("myapp", matchString[1]);

                                if (matchString[1].contains("OLA")) {
                                    isFrom = 1;
                                } else if (matchString[1].contains("UBER")) {
                                    isFrom = 2;
                                } else if (matchString[1].contains("BK")) {
                                    isFrom = 3;
                                }else{
                                    isFrom = 4;
                                }
                                // check if message has otp or not
                                String smsBody = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                                if (!Utils.isContainAnyTypeOfCode(smsBody)) {
                                    // store SMS
                                    SMSModel sms = new SMSModel();
                                    //id
                                    sms.setId(cursor.getString(cursor.getColumnIndexOrThrow("_id")).toString());
                                    // body
                                    sms.setBody(cursor.getString(cursor.getColumnIndexOrThrow("body")).toString());
                                    // number or address
                                    sms.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("address")).toString());
                                    //Date
                                    long date = cursor.getLong(cursor.getColumnIndex("date"));
                                    // convert date to readable format
                                    SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy HHmm");
                                    String dateString = formatter.format(new Date(date));
                                    sms.setDate(dateString);

                                    switch (isFrom) {
                                        case 1:
                                            // sms of ola
                                            CounterHolder.smsOlaModelArrayList.add(sms);
                                            break;
                                        case 2:
                                            // sms of uber
                                            CounterHolder.smsUberModelArrayList.add(sms);
                                            break;
                                        case 3:
                                            // sms of bank
                                            CounterHolder.smsBkModelArrayList.add(sms);
                                            break;
                                        case 4:
                                            // rest SMSes
                                            CounterHolder.smsPromArrayList.add(sms);
                                            break;
                                        default:
                                            Log.d("myapp", "not listed");
                                    }
                                }
                            }


                        }
                        cursor.moveToNext();
                    }

                    cursor.close();
                    Log.d("myapp", "Count of ola sms" + CounterHolder.smsOlaModelArrayList.size());
                    Log.d("myapp", "Count of Uber sms" + CounterHolder.smsUberModelArrayList.size());
                    Log.d("myapp", "Count of bank sms" + CounterHolder.smsBkModelArrayList.size());
                    Log.d("myapp", "Count of prom sms" + CounterHolder.smsPromArrayList.size());
                    // setting format for file

                    String phoneNo = preferences.getString(Const.PHONE_NUMBER, null);
                    String emailDateForFile = preferences.getString(Const.EMAIL_SEND_DATE,null);
                    String finaldatestr = new SimpleDateFormat("ddMMyyyy").format(new SimpleDateFormat("dd-MM-yyyy").parse(emailDateForFile));
                    if (CounterHolder.smsOlaModelArrayList.size() > 0) {
                        String smsName = "ola";
                        String fileName = smsName + "-" + phoneNo + "-" + finaldatestr + ".xls";

                        createSMSxlsFile(fileName,CounterHolder.smsOlaModelArrayList);
                    } else {
                        Log.d(Const.TAG, "no ola message");
                    }
                    if (CounterHolder.smsBkModelArrayList.size() > 0) {
                        String smsName = "bank";
                        String fileName = smsName + "-" + phoneNo + "-" + finaldatestr + ".xls";

                        createSMSxlsFile(fileName,CounterHolder.smsBkModelArrayList);
                    } else {
                        Log.d(Const.TAG, "no bank message");
                    }
                    if (CounterHolder.smsUberModelArrayList.size() > 0) {
                        String smsName = "uber";
                        String fileName = smsName + "-" + phoneNo + "-" + finaldatestr + ".xls";

                        createSMSxlsFile(fileName,CounterHolder.smsUberModelArrayList);
                    } else {
                        Log.d(Const.TAG, "no uber message");
                    }
                    if (CounterHolder.smsPromArrayList.size() > 0) {
                        String smsName = "promo";
                        String fileName = smsName + "-" + phoneNo + "-" + finaldatestr + ".xls";

                        createSMSxlsFile(fileName,CounterHolder.smsPromArrayList);
                    } else {
                        Log.d(Const.TAG, "no promotional message");
                    }

                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                Log.d(Const.TAG,"No new data to read");
            }
            return false;
        }

        //sending email
        private boolean uploadXMLFiles() {
            // changed url to vnurture
            String serverUri = context.getString(R.string.server_mail_url);
            //check xml files count
            if(NetworkUtil.getFilePathWithName(dirPath)) {
                // checking file availability in directory
                int count = CounterHolder.filePathArrayList.size();
                for (int i = 0; i < count; i++) {
                    // file upload path
                    String sourceFileUri = CounterHolder.filePathArrayList.get(i);
                    // file upload name
                    String uploadFileName = CounterHolder.fileNameArrayList.get(i);
                    String uploadFilePath = dirPath+"/";
                    // file upload
                    new NetworkUtil.FileUploadInBackground(context,sourceFileUri,uploadFilePath,uploadFileName,serverUri,false).execute();
                }
//            Utils.setOnetimeTimer(this);

                return true;
            }else{
                Log.d(Const.TAG,"No files");
            }
            return false;
        }

//

        private boolean isCorporateMessage(String messageAddress) {
            if (messageAddress.trim().contains("-")) {
                return true;
            }
            return false;
        }


        private void createSMSxlsFile(String fileName, ArrayList<SMSModel> arrayList){
            try {
                // get path to directory
                File dir = Utils.createDirectory(context);
                Log.d(Const.TAG, dir.getPath() + "/" + fileName);
                dirPath = dir.getAbsolutePath();
                // create String path
                String path = dir.getAbsolutePath() + "/" + fileName;
                Log.d(Const.TAG, path);
                File xlsfile = new File(path);
                if (xlsfile.exists()) {
                    Log.d(Const.TAG, "xml file is already there");
                    xlsfile.delete();
                    Log.d(Const.TAG, "existed file is deleted");
                    xlsfile.createNewFile();
                    Log.d(Const.TAG, "new xml file is created");
                } else {
                    xlsfile.createNewFile();
                    Log.d(Const.TAG, "new xml file created");
                }
                // change workbook settings
                WorkbookSettings wbSettings = new WorkbookSettings();
                wbSettings.setLocale(new Locale("en", "EN"));
                // create workbook
                WritableWorkbook workbook;
                int row = 1;
                workbook = Workbook.createWorkbook(xlsfile, wbSettings);
                WritableSheet sheet = workbook.createSheet("First Sheet", 0);
                // setting title for workbook
                Label headerLabel = new Label(0,0, Const.SMS_HEADER);
                Label contentLabel = new Label(1,0, Const.SMS_CONTENT);
                Label dateTimeLabel = new Label(2,0, Const.SMS_DATE_TIME);
                Label cityLabel = new Label(3,0, Const.SMS_City);
                sheet.addCell(headerLabel);
                sheet.addCell(contentLabel);
                sheet.addCell(dateTimeLabel);
                sheet.addCell(cityLabel);
                //insert data to workbook dynamically
                int count = arrayList.size();
                for(int i = 0 ; i < count ; i++){
                    // column count handling
                    // setting data to cell
                    sheet.addCell(new Label(0,row,arrayList.get(i).getAddress()));
                    sheet.addCell(new Label(1,row,arrayList.get(i).getBody()));
                    sheet.addCell(new Label(2,row,arrayList.get(i).getDate()));
                    sheet.addCell(new Label(3,row,"Mumbai"));
                    // row count incrementing
                    row++;
                }
                // write cells to workbook
                workbook.write();
                // cose workbook
                workbook.close();

            }catch(Exception e){
                e.printStackTrace();;
            }

        }

        private void createContactxlsFile(String fileName, ArrayList<ContactModel> arrayList){
            try {
                // get path to directory
                File dir = Utils.createDirectory(context);
                Log.d(Const.TAG, dir.getPath() + "/" + fileName);
                dirPath = dir.getAbsolutePath();
                // create String path
                String path = dir.getAbsolutePath() + "/" + fileName;
                Log.d(Const.TAG, path);
                File xlsfile = new File(path);
                if (xlsfile.exists()) {
                    Log.d(Const.TAG, "xml file is already there");
                    xlsfile.delete();
                    Log.d(Const.TAG, "existed file is deleted");
                    xlsfile.createNewFile();
                    Log.d(Const.TAG, "new xml file is created");
                } else {
                    xlsfile.createNewFile();
                    Log.d(Const.TAG, "new xml file created");
                }
                // change workbook settings
                WorkbookSettings wbSettings = new WorkbookSettings();
                wbSettings.setLocale(new Locale("en", "EN"));
                // create workbook
                WritableWorkbook workbook;
                int row = 1;
                workbook = Workbook.createWorkbook(xlsfile, wbSettings);
                WritableSheet sheet = workbook.createSheet("First Sheet", 0);
                // setting title for workbook
                Label nameLabel = new Label(0,0, Const.CONTACT_NAME);
                Label contactLabel = new Label(1,0, Const.CONTACT_PHONE_NUMBER);
                sheet.addCell(nameLabel);
                sheet.addCell(contactLabel);
                //insert data to workbook dynamically
                int count = arrayList.size();
                for(int i = 0 ; i < count ; i++){
                    // column count handling
                    // setting data to cell
                    sheet.addCell(new Label(0,row,arrayList.get(i).getContactName()));
                    sheet.addCell(new Label(1,row,arrayList.get(i).getContactNumber()));
                    // row count incrementing
                    row++;
                }
                // write cells to workbook
                workbook.write();
                // cose workbook
                workbook.close();

            }catch(Exception e){
                e.printStackTrace();;
            }

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        Intent intent = new Intent(this,PushOnReceiver.class);
        intent.setAction("com.vnurture.vihas.smartcabfuelcard.PUSH_ON");
        sendBroadcast(intent);
        Log.d("myapp","service distroy");
    }

    // when we clear the memory
    public void onTaskRemoved(Intent intent){
        super.onTaskRemoved(intent);
        Log.d(Const.TAG,"onTaskRemoved");
        Intent intent1 = new Intent(this,PushOnReceiver.class);
        intent1.setAction("com.vnurture.vihas.smartcabfuelcard.PUSH_ON");
        sendBroadcast(intent1);
    }


}
