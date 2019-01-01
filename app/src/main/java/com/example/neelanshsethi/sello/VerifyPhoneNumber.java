package com.example.neelanshsethi.sello;

import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.AndroidResources;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.raycoarana.codeinputview.CodeInputView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class VerifyPhoneNumber extends AppCompatActivity {

    //These are the objects needed
    //It is the verification id that will be sent to the user
    private String mVerificationId;

    //The edittext to input the code
    private EditText editTextCode;
    String mobile;
    private boolean mVerificationInProgress = false;
    CodeInputView codeInputView;
    TextView wesenOTP;
    String code;
    TextView resend;
    private Timer timer;
    private int timeRemaining = 10;
    private TextView txtTimer;
    private boolean isWaiting = false;
    //firebase auth object
    private FirebaseAuth mAuth;
    PhoneAuthProvider.ForceResendingToken mResendToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_number);
        codeInputView = findViewById(R.id.codeInput);
        resend=findViewById(R.id.resendCode);
        resend.setVisibility(View.GONE);
        txtTimer = (TextView) findViewById(R.id.txtTimer);

        //initializing objects
        mAuth = FirebaseAuth.getInstance();
        wesenOTP=findViewById(R.id.weSentOTP);
        wesenOTP.setText("We have sent an OTP\n Please wait for auto-read or\n enter the OTP below");
//        editTextCode = findViewById(R.id.editTextCode);


        //getting mobile number from the previous activity
        //and sending the verification code to the number
        Intent intent = getIntent();
        mobile = intent.getStringExtra("mobile");
        sendVerificationCode(mobile);


        //if the automatic sms detection did not work, user can also enter the code manually
        //so adding a click listener to the button
        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    codeInputView = findViewById(R.id.codeInput);
                    code=codeInputView.getCode();
//                    codeInputView.addOnCompleteListener(new OnCodeCompleteListener() {
//                        @Override
//                        public void onCompleted(String code) {
//                        }
//                    });
                            Log.d("zzz","Code is"+code+ "of length "+ code.length());

                            if(code.length()<6 || code.isEmpty())
                            {
                                codeInputView.setError("Enter valid code");
                                codeInputView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        codeInputView.clearError();
                                    }
                                },500);
                            }
                            else {
                                
                                verifyVerificationCode(code);
                                Log.d("zzz", code);

                            }



//                verifying the code entered manually

            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(mobile);
                resend.setEnabled(false);
                resend.setTextColor(Color.parseColor("#d3d3d3"));
            }
        });

    }

    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                10,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
        codeInputView.setCode("");
        mVerificationInProgress = true;
    }

    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

//            Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();
            Log.d("zzz","passed "+ phoneAuthCredential+" "+code);
            mVerificationInProgress = false;


            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {


                codeInputView.setInputType(InputType.TYPE_CLASS_NUMBER);
                codeInputView.setCode(code);
                codeInputView.setEditable(true);
                codeInputView.setShowKeyboard(false);
                //verifying the code
                verifyVerificationCode(code);
            }
            else{

                signInWithPhoneAuthCredential(phoneAuthCredential);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneNumber.this, e.getMessage(), Toast.LENGTH_LONG).show();
            mVerificationInProgress = false;
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Log.d("zzz","CodeSent is " +s);
            Toast.makeText(VerifyPhoneNumber.this,"Code Sent!", Toast.LENGTH_SHORT).show();

            //storing the verification id that is sent to the user
            mVerificationId = s;
            mResendToken = forceResendingToken;
            startResendTimer();
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyPhoneNumber.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user= task.getResult().getUser();
                            //verification successful we will start the profile activity
                            Intent intent = new Intent(VerifyPhoneNumber.this, UserInfo.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Something is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                            codeInputView.setEditable(true);

                            codeInputView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    codeInputView.setCode("");
                                }
                            },1000);


                        }
                    }
                });
    }

    private void resendVerificationCode(String mobile){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+mobile,        // Phone number to verify
                1  ,               // Timeout duration
                TimeUnit.MINUTES,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                mResendToken);             // Force Resending Token from callbacks
    }

    private void startResendTimer(){
        if (timer != null){
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new WaitingTask(), 0, 1000);
        txtTimer.setVisibility(View.VISIBLE);
        isWaiting = true;
    }

    private void stopResendTimer(){
        timer.cancel();
        txtTimer.setVisibility(View.INVISIBLE);
        timeRemaining = 10;
        isWaiting = false;
    }

    class WaitingTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    timeRemaining = timeRemaining - 1;
                    if (timeRemaining == 0){
                        resend.setVisibility(View.VISIBLE);
                        resend.setEnabled(true);
                        resend.setTextColor(getResources().getColor(R.color.colorPrimary));
                        stopResendTimer();
                    }else{
                        String strTimeRemaining = String.format("%02d:%02d",timeRemaining/10,timeRemaining%10);
                        txtTimer.setText(strTimeRemaining);
                    }
                }
            });
        }
    }

}
