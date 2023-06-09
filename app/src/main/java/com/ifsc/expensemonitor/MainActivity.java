package com.ifsc.expensemonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.TypedValue;

import com.ifsc.expensemonitor.data.PreferenceUtils;
import com.ifsc.expensemonitor.notifications.AlarmReceiver;

=======
import android.os.Bundle;
>>>>>>> 8e57fcd (iniciando menu de opções)

public class MainActivity extends AppCompatActivity {

    private PreferenceUtils preferenceUtils;

    public static NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }
}