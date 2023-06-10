package com.ifsc.expensemonitor;

import androidx.appcompat.app.AppCompatActivity;

<<<<<<< HEAD
import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.os.Build;
import android.os.Bundle;

import com.ifsc.expensemonitor.notifications.AlarmReceiver;

=======
import android.os.Bundle;
>>>>>>> 8e57fcd (iniciando menu de opções)

public class MainActivity extends AppCompatActivity {

    public static NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        AlarmReceiver.setAlarm(this);
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