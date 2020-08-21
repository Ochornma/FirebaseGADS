package com.promise.gadsbooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

public class BookActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private String PREFRENCES = "subscription";
    private SwitchCompat java, kotlin, firebase;
    private String javaSubscribe = "Java";
    private String kotlinSubscribe = "Kotlin";
    private String firebaseSubscribe = "Firebase";
    CardView cardView;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        Objects.requireNonNull(getSupportActionBar()).setTitle("BOOK");
        cardView = findViewById(R.id.admin_card);
        sharedpreferences = getSharedPreferences(PREFRENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        boolean admin = sharedpreferences.getBoolean("admin", false);
        if (!admin){
            cardView.setVisibility(View.GONE);
        }

        java = findViewById(R.id.java_subscribe);
        kotlin = findViewById(R.id.kotlin_subscribe);
        firebase = findViewById(R.id.firebase_subscribe);
        java.setChecked(sharedpreferences.getBoolean(javaSubscribe, false));
        kotlin.setChecked(sharedpreferences.getBoolean(kotlinSubscribe, false));
        firebase.setChecked(sharedpreferences.getBoolean(firebaseSubscribe, false));
        java.setOnCheckedChangeListener(this);
        kotlin.setOnCheckedChangeListener(this);
        firebase.setOnCheckedChangeListener(this);
        authListenerSetUp();
    }

    public void viewBooks(View view) {
        Intent intent = new Intent(BookActivity.this, SpecificBookActivity.class);
        int id = view.getId();
        String extra = " ";
        switch (id){
            case R.id.button_java:
                extra = javaSubscribe;
                intent.putExtra(Constant.extra, extra);
                startActivity(intent);
                break;
            case  R.id.button_kotlin:
                extra = kotlinSubscribe;
                intent.putExtra(Constant.extra, extra);
                startActivity(intent);
                break;
            case R.id.button_firebase:
                extra = firebaseSubscribe;
                intent.putExtra(Constant.extra, extra);
                startActivity(intent);
                break;
            case R.id.button_android:
                Intent intentAndroid = new Intent(BookActivity.this, BookViewActivity.class);
                startActivity(intentAndroid);
                break;
            case R.id.button_admin:
                Intent intentAdmin = new Intent(BookActivity.this, AdminActivity.class);
                startActivity(intentAdmin);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + id);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profile){
            Intent intent = new Intent(BookActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();
      switch (id){
          case R.id.java_subscribe:

              switchCheck(b, javaSubscribe);
              break;
          case R.id.kotlin_subscribe:
              switchCheck(b, kotlinSubscribe);
              break;
          case  R.id.firebase_subscribe:
              switchCheck(b, firebaseSubscribe);
      }


    }

    private void switchCheck(boolean check, String topic){
        if (check){
            FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BookActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(BookActivity.this, topic + ": subscribed", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BookActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(BookActivity.this, topic + ": Unsubscribed", Toast.LENGTH_SHORT).show();
                }
            });
        }
        editor.putBoolean(topic, check);
        editor.apply();
    }

    private void authListenerSetUp() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(BookActivity.this, AuthActivity.class);
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

    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
    }
}