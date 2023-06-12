package com.ifsc.expensemonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

<<<<<<< HEAD
import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> d6b8565 (preferencias padrão)
import com.ifsc.expensemonitor.data.PreferenceUtils;
import com.ifsc.expensemonitor.notifications.AlarmReceiver;

=======
import android.os.Bundle;
>>>>>>> 8e57fcd (iniciando menu de opções)
=======
import android.os.Bundle;
>>>>>>> 8e57fcd (iniciando menu de opções)

public class MainActivity extends AppCompatActivity {

    private PreferenceUtils preferenceUtils;

=======
import com.ifsc.expensemonitor.notifications.NotificationWorker;
=======
import com.ifsc.expensemonitor.notifications.AlarmReceiver;
>>>>>>> 2801656 (notificação as 8 da manha)


public class MainActivity extends AppCompatActivity {

<<<<<<< HEAD
>>>>>>> e5aed21 (notificação.)
=======
import com.ifsc.expensemonitor.notifications.NotificationWorker;

import java.security.cert.CertPathBuilder;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

>>>>>>> 4eaf118 (notificação.)
=======
    private PreferenceUtils preferenceUtils;

>>>>>>> fb51114 (muita coisa)
    public static NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
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
            CharSequence name = "";
            String description = "";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
<<<<<<< HEAD
            NotificationChannel channel = new NotificationChannel("notifications_channel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
=======
            NotificationChannel channel = new NotificationChannel("notifications_channel", "Notifications", importance);
            channel.setDescription("Notifications");
>>>>>>> d6b8565 (preferencias padrão)
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
=======
        // Create a notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
>>>>>>> d4fbed1 (botao de editar despesa)
=======
=======
>>>>>>> 4eaf118 (notificação.)
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
<<<<<<< HEAD
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifications_channel", "Notifications", importance);
            channel.setDescription("Notifications");
<<<<<<< HEAD
=======
            CharSequence name = "";
            String description = "";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifications_channel", name, importance);
            channel.setDescription(description);
>>>>>>> 4eaf118 (notificação.)
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
=======
>>>>>>> d6b8565 (preferencias padrão)
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
<<<<<<< HEAD
>>>>>>> e5aed21 (notificação.)
=======
>>>>>>> 4eaf118 (notificação.)
    }
}