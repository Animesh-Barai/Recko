package com.example.neelanshsethi.recko;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
                Intent i = new Intent(GetStarted.this, MainActivity.class);
                startActivity(i);

            }
        });
    }
}
