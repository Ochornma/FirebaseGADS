package com.promise.gadsbooks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;
import java.util.Objects;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        Objects.requireNonNull(getSupportActionBar()).setTitle("AUTHENTICATION");
        Button login = findViewById(R.id.login);
        Button signup = findViewById(R.id.signup);
        login.setOnClickListener(this);
        signup.setOnClickListener(this);
        authListenerSetUp();
    }

    private void authListenerSetUp() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(AuthActivity.this, BookActivity.class);
                    intent.putExtra(Constant.admin, admin(Objects.requireNonNull(firebaseAuth.getCurrentUser().getEmail())));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        };
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
       /* GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            Intent intent = new Intent(AuthActivity.this, BookActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        if (view.getId() == R.id.login) {
            intent = new Intent(AuthActivity.this, SignInActivity.class);
        } else {
            intent = new Intent(AuthActivity.this, SignUpActivity.class);
        }
        startActivity(intent);
    }

    private boolean admin(String email){
        return email.substring(email.indexOf("@") + 1).toLowerCase(Locale.ROOT).equals("admin.com");
    }

}