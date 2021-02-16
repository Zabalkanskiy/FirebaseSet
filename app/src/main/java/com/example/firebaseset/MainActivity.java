package com.example.firebaseset;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int RC_SIGN_IN = 100;

    List<AuthUI.IdpConfig> idpConfigList = new ArrayList<AuthUI.IdpConfig>();

    private FirebaseAuth mAuth;
    Button signInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         mAuth = FirebaseAuth.getInstance();
        //Проверяется есть ли юзер в системе
        if(mAuth.getCurrentUser() != null ){

            startActivity(SignInActivity.createIntent(this,null));
            finish();
            return;
        }

        idpConfigList.add( new AuthUI.IdpConfig.EmailBuilder().build());
        idpConfigList.add( new AuthUI.IdpConfig.GoogleBuilder().build());

        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().enableAnonymousUsersAutoUpgrade()
                                .setIsSmartLockEnabled(true)
                                .setAvailableProviders(idpConfigList)
                                        .build(),
                                RC_SIGN_IN);

                    }
                }

        );


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            handleSignInResponce(resultCode, data);
        }
    }

    private void handleSignInResponce(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);
        Toast toast;
        if(resultCode == RESULT_OK){
            //start SignedActivity
            startActivity(SignInActivity.createIntent(this,response));
            finish();
            return;

        }
        else{
            if(response == null){
                toast = Toast.makeText(this,"Sign in was cancelled", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK ){
                toast = Toast.makeText(this, "You have no Internet connection", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR){
                toast = Toast.makeText(this,"Unknown error", Toast.LENGTH_LONG);
                toast.show();
                return;
            }

        }

        toast = Toast.makeText(this, "Unknown error", Toast.LENGTH_LONG);
        toast.show();
    }

    public static   Intent createIntent(Context context){
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        return  intent;

    }
}