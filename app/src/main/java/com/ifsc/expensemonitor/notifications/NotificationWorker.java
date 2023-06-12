package com.ifsc.expensemonitor.notifications;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ifsc.expensemonitor.MainActivity;
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.database.FirebaseSettings;
import com.ifsc.expensemonitor.database.Occurrence;
import com.ifsc.expensemonitor.database.SimpleDate;

public class NotificationWorker extends Worker {

    private Context context;
    private int overdueOccurrences = 0;
    private int pendingOccurrences = 0;

    public NotificationWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    @Override
    public Result doWork() {

        DatabaseReference occurrencesRef = FirebaseSettings.getOccurrencesReference();

        occurrencesRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                    String message = overdueOccurrences == 1 ? " despesa atrasada!" : " despesas atrasadas!";
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifications_channel")
                            .setSmallIcon(R.drawable.ic_money)
                            .setContentTitle("Expense Monitor")
                            .setContentText("Você tem " + overdueOccurrences + message)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(true);

                    MainActivity.notificationManager.notify(1, builder.build());
                }

                if (pendingOccurrences > 0) {
                    String message = pendingOccurrences == 1 ? " despesa próxima do vencimento!" : " despesas próximas do vencimento!";
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifications_channel")
                            .setSmallIcon(R.drawable.ic_money)
                            .setContentTitle("Expense Monitor")
                            .setContentText("Você tem " + pendingOccurrences + message)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(true);

                    MainActivity.notificationManager.notify(2, builder.build());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return Result.success();
    }
}

