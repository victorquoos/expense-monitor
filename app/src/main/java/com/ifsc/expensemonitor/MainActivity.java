package com.ifsc.expensemonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import com.ifsc.expensemonitor.notifications.NotificationWorker;

import java.security.cert.CertPathBuilder;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public static NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        PeriodicWorkRequest notificacaoWork = new PeriodicWorkRequest.Builder(NotificationWorker.class, 15, TimeUnit.MINUTES).build();
        WorkManager.getInstance(this).enqueue(notificacaoWork);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "";
            String description = "";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifications_channel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}