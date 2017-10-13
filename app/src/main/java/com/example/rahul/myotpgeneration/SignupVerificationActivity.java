package com.example.rahul.myotpgeneration;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupVerificationActivity extends AppCompatActivity implements View.OnClickListener {
    EditText verificationCode;
    Button finishButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // phone number edit text
        verificationCode = (EditText)findViewById(R.id.signup_verification_digits);
        // continue button
        finishButton = (Button) findViewById(R.id.signup_verification_Submit_button);
        assert finishButton != null;
        finishButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.signup_verification_Submit_button){

            String message = null;
            // get value of edit text
            String verificationStr = verificationCode.getText().toString().trim();
            if(verificationStr.isEmpty()){
                message = getResources().getString(R.string.enter_verification_code);
            }else if(verificationCode.length()>4) {
                message = getResources().getString(R.string.code_is_not_valid);
            }
            else{
                // notification base validation
                if(Utils.isCodeValid(this, Integer.parseInt(verificationStr))) {
                    // get current date
                    String currentDate = Utils.getCurrentDate();
                    //SharedPreference
                    SharedPreferences.Editor editor = (getSharedPreferences(Const.SHAREDPREFERENCE_MAIN,MODE_PRIVATE)).edit();
                    // hard coded inserted value
                    editor.putInt(Const.LOGIN_ID,1);
                    editor.putInt(Const.FIRST_TIME,1);
                    editor.putString(Const.EMAIL_SEND_DATE,Utils.getYesterDayDate());
                    editor.putString(Const.TODAY_DATE,currentDate);
                    editor.putString(Const.REGISTRATION_DATE,currentDate);
                    editor.apply();


                    // push on feature
                    if (!isPushOnServiceRunning(PushOnService.class)) {
                        Intent intent = new Intent(getBaseContext(),Welcome.class);
                        intent.putExtra(Const.CUSTOM_FLAG,Const.ACTION_FIRST_TIME);
                        startService(intent);

                    }
                    Intent goToMailActivity = new Intent(SignupVerificationActivity.this, Welcome.class);
                    startActivity(goToMailActivity);

                }else {
                    message = getResources().getString(R.string.code_is_not_valid);
                }
            }
            if(message != null){
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // check push on back ground service is running or not
    // if it is not running then this method will start the service
    private boolean isPushOnServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
