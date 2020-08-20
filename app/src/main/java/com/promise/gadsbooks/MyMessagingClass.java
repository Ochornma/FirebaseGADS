package com.promise.gadsbooks;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;


public class MyMessagingClass extends FirebaseMessagingService {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firebaseFirestore.collection("token");
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    private static String ID = "APP";
    private NotificationManagerCompat notificationManager;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //Toast.makeText(MyMessagingClass.this, remoteMessage.getData().toString(), Toast.LENGTH_SHORT).show();
        Log.i("notify", remoteMessage.getData().toString());
        String title = remoteMessage.getData().get("title");
        String subtitle = remoteMessage.getData().get("subtitle");
        String heading = remoteMessage.getData().get("heading");
        sendOnChannel(title, subtitle, heading);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        sendToFirebase(s);
        super.onNewToken(s);
    }

    public void sendToFirebase(String token){
        HashMap<String, String> tokenId = new HashMap<>();
        tokenId.put("token", token);
        if (user != null){
            collectionReference.document(user.getUid()).set(tokenId);
        }

    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public void sendOnChannel(String title, String message, String heading) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.appstore);
        notificationManager = NotificationManagerCompat.from(this);
        Intent activityIntent = new Intent(this, SpecificBookActivity.class);
        activityIntent.putExtra(Constant.extra, heading);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, ID)
                .setSmallIcon(R.drawable.appstore_small)
                .setContentTitle(title)
                .setLargeIcon(bitmap)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                /*.setColor(Color.parseColor("#8bf6ff"))*/
                .setColor(getColor(R.color.colorPrimaryLight))
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.ic_view, "View", contentIntent)
                .build();
        notificationManager.notify(1, notification);
    }


}
