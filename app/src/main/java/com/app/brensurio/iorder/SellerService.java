package com.app.brensurio.iorder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import com.app.brensurio.iorder.activities.SellerMainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SellerService extends IntentService {

    private DatabaseReference mDatabase;
    private String storeName;

    public static int NOTIFICATION_ID = 5453;

    public SellerService() {
        super("SellerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        storeName = intent.getStringExtra("storeName");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("storeorders").orderByChild("store")
                .equalTo(storeName);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notifySeller();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void notifySeller() {
        Intent intent = new Intent(this, SellerMainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(SellerMainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSound(alarmSound)
                .setLights(0xff00ff00, 300, 100)
                .setContentIntent(pendingIntent)
                .setContentText("Check your order list.")
                .build();

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
