package com.example.neelanshsethi.sello;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        checkRecur();
    }

    private void checkRecur()
    {
        if(!InternetConnection.checkConnection(SplashScreen.this)) {

            Snackbar.make(findViewById(android.R.id.content),"No Internet Connection. Please connect to Internet to proceed!",Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkRecur();
                        }
                    }).show();

        }
        else
        {
            proceed();
        }
    }
//    protected void onStart() {
//        super.onStart();
//
//        if (user != null) {
//            // User is signed in
//            Intent intent = new Intent(this, ProfileActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//
//        } else {
//            // No user is signed in
//        }
//    }

    private void proceed()
    {
        SharedPreferences pref=getSharedPreferences("status",MODE_PRIVATE);
        final boolean test=pref.getBoolean("first",true);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (test) {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Intent i = new Intent(SplashScreen.this, GetStarted.class);
                    startActivity(i);
                    finish();
                }

            }
        },3000);


    }
}

