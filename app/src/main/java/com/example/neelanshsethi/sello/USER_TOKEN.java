package com.example.neelanshsethi.sello;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

public class USER_TOKEN {

    private static String idToken;
    public static String token()
    {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            idToken = task.getResult().getToken();
                            Log.d("token",idToken);
                            // Send token to your backend via HTTPS
                            //
                        } else {
                            // Handle error -> task.getException();
                        }
                    }
                });
        return idToken;

    }
}
