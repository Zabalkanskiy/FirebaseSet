package com.example.firebaseset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    //public static final String IDPRESPONSE = "IDPRESPONSE";
    Button outSignInButton;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    TextView email;
    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user ==null){
            startActivity(MainActivity.createIntent(this));
            finish();
            return;
        }

        outSignInButton = findViewById(R.id.out_sign_in);

        email = findViewById(R.id.email);
        userName = findViewById(R.id.user_display_name);

        email.setText(user.getEmail());

        userName.setText(user.getDisplayName());
        
        outSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    private void signOut() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    startActivity(MainActivity.createIntent(SignInActivity.this));
                    finish();

                }
                else{//sign out false
                     }
            }
        });

    }

    public static Intent createIntent(Context context, IdpResponse idpResponse){
        Intent intent = new Intent();
        intent.setClass(context, SignInActivity.class);
        intent.putExtra(ExtraConstants.IDP_RESPONSE, idpResponse);
        return intent;
   }
}