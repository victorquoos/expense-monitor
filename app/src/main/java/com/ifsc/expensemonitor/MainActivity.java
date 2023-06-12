package com.ifsc.expensemonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.ifsc.expensemonitor.data.PreferenceUtils;
import com.ifsc.expensemonitor.notifications.AlarmReceiver;


public class MainActivity extends AppCompatActivity {

    private PreferenceUtils preferenceUtils;

    public static NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        AlarmReceiver.setAlarm(this);

        preferenceUtils = new PreferenceUtils(this);

        // Verifica o estado salvo do modo escuro e aplica-o quando o aplicativo abre
        if (preferenceUtils.getDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifications_channel", "Notifications", importance);
            channel.setDescription("Notifications");
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}