package com.ifsc.expensemonitor.notifications;

<<<<<<< HEAD
import static android.content.Context.MODE_PRIVATE;

=======
>>>>>>> 2801656 (notificação as 8 da manha)
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
<<<<<<< HEAD
import android.content.SharedPreferences;
=======
>>>>>>> 2801656 (notificação as 8 da manha)

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ifsc.expensemonitor.MainActivity;
import com.ifsc.expensemonitor.R;
<<<<<<< HEAD
<<<<<<< HEAD
import com.ifsc.expensemonitor.SplashActivity;
import com.ifsc.expensemonitor.data.FirebaseSettings;
import com.ifsc.expensemonitor.data.Occurrence;
import com.ifsc.expensemonitor.data.PreferenceUtils;
import com.ifsc.expensemonitor.data.SimpleDate;
=======
import com.ifsc.expensemonitor.database.FirebaseSettings;
import com.ifsc.expensemonitor.database.Occurrence;
import com.ifsc.expensemonitor.database.SimpleDate;
>>>>>>> 2801656 (notificação as 8 da manha)
=======
import com.ifsc.expensemonitor.data.FirebaseSettings;
import com.ifsc.expensemonitor.data.Occurrence;
import com.ifsc.expensemonitor.data.SimpleDate;
>>>>>>> a13e763 (ajustes do merge)

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

<<<<<<< HEAD
=======
        sendNotification(context, notificationManager, "Teste de notificação do alarm receiver", 0);

>>>>>>> 2801656 (notificação as 8 da manha)
        DatabaseReference occurrencesRef = FirebaseSettings.getOccurrencesReference();
        occurrencesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int overdueOccurrences = 0;
                int pendingOccurrences = 0;

                for (DataSnapshot yearSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot monthSnapshot : yearSnapshot.getChildren()) {
                        for (DataSnapshot occurrenceSnapshot : monthSnapshot.getChildren()) {
                            Occurrence occurrence = occurrenceSnapshot.getValue(Occurrence.class);
                            if (occurrence != null) {
                                if (!occurrence.isPaid()) {
                                    if (occurrence.getDate().isBefore(SimpleDate.today())) {
                                        overdueOccurrences++;
                                    } else if (occurrence.getDate().isBefore(SimpleDate.today().plusDays(7))) {
                                        pendingOccurrences++;
                                    }
                                }
                            }
                        }
                    }
                }

                if (overdueOccurrences > 0) {
                    String message = overdueOccurrences == 1 ?
                            context.getString(R.string.notification_overdue_singular, overdueOccurrences) :
                            context.getString(R.string.notification_overdue_plural, overdueOccurrences);
                    sendNotification(context, notificationManager, message, 1);
                }

                if (pendingOccurrences > 0) {
                    String message = pendingOccurrences == 1 ?
                            context.getString(R.string.notification_pending_singular, pendingOccurrences) :
                            context.getString(R.string.notification_pending_plural, pendingOccurrences);
                    sendNotification(context, notificationManager, message, 2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void sendNotification(Context context, NotificationManager notificationManager, String message, int id) {
        // Cria uma intenção que abrirá a MainActivity
<<<<<<< HEAD
        Intent intent = new Intent(context, SplashActivity.class);
=======
        Intent intent = new Intent(context, MainActivity.class);
>>>>>>> 2801656 (notificação as 8 da manha)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifications_channel")
                .setSmallIcon(R.drawable.ic_money)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent); // Adiciona o PendingIntent à notificação

        if (notificationManager != null) {
            notificationManager.notify(id, builder.build());
        }
    }

    public static void setAlarm(Context context) {
<<<<<<< HEAD
        if (!isAlarmSet(context)) {
            System.out.println("LOG::: Configurando alarme");
            // Cria uma intenção para o AlarmReceiver
            Intent intent = new Intent(context, AlarmReceiver.class);

            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            // Configura o tempo para as 8 da manhã
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);

            // Configura o alarme para disparar todos os dias às 8 da manhã
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, alarmIntent);
            }
        } else {
            System.out.println("LOG::: Alarme já configurado");
        }
    }

    public static boolean isAlarmSet(Context context) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE) != null;
    }

=======
        // Cria uma intenção para o AlarmReceiver
        Intent intent = new Intent(context, AlarmReceiver.class);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Configura o tempo para as 8 da manhã
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);

        // Configura o alarme para disparar todos os dias às 8 da manhã
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_HALF_DAY, alarmIntent);
        }
    }

>>>>>>> 2801656 (notificação as 8 da manha)
}
