package com.example.rahul.myotpgeneration;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
        EditText phoneNumber,username;
        Button continueButton;
    TextView termsConditionTextView;
    CheckBox termBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //username edit text
        username = (EditText)findViewById(R.id.signup_username);
        // phone number edit text
        phoneNumber = (EditText)findViewById(R.id.signup_number_digits);
        termsConditionTextView = (TextView)findViewById(R.id.terms_and_condition_onclick);
        termBox = (CheckBox)findViewById(R.id.signup_terms_condition_checkbox);
        // continue button
        continueButton = (Button) findViewById(R.id.signup_Submit_button);
        assert continueButton != null;
        continueButton.setOnClickListener(this);

        // textview onclick
        termsConditionTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // if submit button is clicked
        if(v.getId() == R.id.signup_Submit_button){
            String message = null;
            // get value of edit text
            String phoneNumberStr = phoneNumber.getText().toString().trim();
            String usernameStr = username.getText().toString().trim();
            if(phoneNumberStr.isEmpty()){
                message = getString(R.string.enter_phone_number);
            }else if(phoneNumberStr.length()>10 || phoneNumberStr.length() < 4) {
                message = getString(R.string.phone_number_is_not_valid);
            }else if(!termBox.isChecked()){
                message = getString(R.string.terms_condition_error_message);
            }else if(usernameStr.isEmpty()){
                message = getString(R.string.enter_username);
            }
            else{
                // validation
                int validationCode = Utils.generateCode();

                // put phone number in shared preference
                SharedPreferences preferences = getSharedPreferences(Const.SHAREDPREFERENCE_MAIN,MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Const.PHONE_NUMBER,phoneNumberStr);
                editor.putString(Const.USERNAME_PREF,usernameStr);
                editor.putInt(Const.VERIFICATION_CODE,validationCode);
                editor.apply();


                Intent intent = new Intent(this,SignupVerificationActivity.class);
                startActivity(intent);
                String smsusername = "RCCREONE";
                String smspassword = "abc321";
                String smsSenderId = "CREONE";
                String otpMessageStart = getResources().getString(R.string.otp_mesage_start_1);
                String otpMessageEnd = getResources().getString(R.string.otp_message_complete_1);
                String otpMessageThankYou = getResources().getString(R.string.otp_message_thank_you_1);
                String otpMessage = otpMessageStart+" "+validationCode+" "+otpMessageEnd+" "+otpMessageThankYou;
                // creating URL
                String URL = getResources().getString(R.string.sms_server_address)+"username="+smsusername+"&password="+smspassword+"&type= TEXT&sender="+smsSenderId+"&mobile="+phoneNumberStr+"&message="+otpMessage;
                WebView webView = new WebView(this);
                webView.loadUrl(URL);
            }
            if(message != null){
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }

        }

        // if terms and condition textView onclick
        if(v.getId() == R.id.terms_and_condition_onclick){
            showTermsAndConditionDialoge();
        }
    }

    // create and show terms and condition dialog
    private void showTermsAndConditionDialoge() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.terms_and_condition_title))
                .setMessage(R.string.terms_and_condition_message)
                .setNeutralButton(getString(R.string.agree_button_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!termBox.isChecked()) {
                            termBox.setChecked(true);
                        }
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }



}
