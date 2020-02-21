package com.recko.app;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.recko.app.Misc.Constants;

public class GetStarted extends AppCompatActivity {

    Button getStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        getStarted=findViewById(R.id.getStarted);
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedPreferences pref = getSharedPreferences("status", MODE_PRIVATE);
//                pref.edit().putBoolean("first", false).apply();
//                Intent i = new Intent(GetStarted.this, MainActivity.class);
//                startActivity(i);

                // Disable get started button as it should not be clicked twice.
                getStarted.setEnabled(false);
                SharedPreferences sharedpreferences = getSharedPreferences(Constants.ReckoPREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(Constants.SeenHowItWorksPrefKey, true);
                editor.commit();

                String jumpLocation = getIntent().getStringExtra("jump");
                Log.d("zzz kk", jumpLocation);
                Log.d("zzz kk", "aa");
                if (jumpLocation.equals(UserInfo.class.getSimpleName())) {
                    Intent intent = new Intent(getApplicationContext(), UserInfo.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else if (jumpLocation.equals(Industries.class.getSimpleName())) {
                    Intent intent = new Intent(getApplicationContext(), Industries.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else if (jumpLocation.equals(MainActivity.class.getSimpleName())) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    goto_navigation();
                }

            }
        });
    }

    private void goto_navigation() {
        Intent intent=new Intent(this,NavigationDashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
