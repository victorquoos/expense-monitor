package com.ifsc.expensemonitor.ui.expenselist;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.data.FirebaseSettings;
import com.ifsc.expensemonitor.data.Occurrence;
import com.ifsc.expensemonitor.data.MoneyValue;
import com.ifsc.expensemonitor.data.OccurrenceController;
import com.ifsc.expensemonitor.data.OccurrenceControllerService;
import com.ifsc.expensemonitor.data.OccurrenceService;

import java.util.ArrayList;
import java.util.Objects;

public class ExpenseDialogFragment extends DialogFragment {
    private Occurrence occurrence;

    public ExpenseDialogFragment(Occurrence occurrence) {
        this.occurrence = occurrence;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_expense_dialog, null);

        TextView expenseNameTextView = view.findViewById(R.id.expenseNameTextView);
        TextView expenseValueTextView = view.findViewById(R.id.expenseValueTextView);
        TextView expenseDateTextView = view.findViewById(R.id.expenseDateTextView);
        TextView expenseDescriptionTextView = view.findViewById(R.id.expenseDescriptionTextView);
        TextView expenseStatusTextView = view.findViewById(R.id.expenseStatusTextView);

        Button editButton = view.findViewById(R.id.editButton);
        Button deleteButton = view.findViewById(R.id.deleteButton);

        Button changeStatusButton = view.findViewById(R.id.changeStatusButton);
        TextView changeStatusBottomTextView = view.findViewById(R.id.changeStatusBottomTextView);
        ImageView changeStatusImageView = view.findViewById(R.id.changeStatusImageView);

        expenseNameTextView.setText(occurrence.getName());
        expenseValueTextView.setText(MoneyValue.format(occurrence.getValue()));
        expenseDateTextView.setText(occurrence.getDate().getFormattedDate());
        expenseDescriptionTextView.setText(occurrence.getDescription());

        if (occurrence.isPaid()) {
            expenseStatusTextView.setText("PAGO");
            changeStatusImageView.setImageResource(R.drawable.ic_close);
            changeStatusButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Red));
            changeStatusBottomTextView.setText("NÃO PAGO");
        } else {
            expenseStatusTextView.setText("NÃO PAGO");
            changeStatusImageView.setImageResource(R.drawable.ic_done);
            changeStatusButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Green));
            changeStatusBottomTextView.setText("PAGO");
        }

        if (expenseDescriptionTextView.getText().toString().isEmpty()) {
            view.findViewById(R.id.expenseDescriptionLinearLayout).setVisibility(View.GONE);
        }

        changeStatusButton.setOnClickListener(v -> {
            occurrence.setPaid(!occurrence.isPaid());
            OccurrenceService.update(occurrence);
            dismiss();
        });

        editButton.setOnClickListener(v -> {
            dismiss();
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);

            Bundle args = new Bundle();
            args.putInt("year", occurrence.getDate().getYear());
            args.putInt("month", occurrence.getDate().getMonth());
            args.putString("id", occurrence.getId());

            navController.navigate(R.id.addEditFragment, args);
        });

        deleteButton.setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());

            DatabaseReference controllerRef = FirebaseSettings.getOccurrenceControllersReference().child(occurrence.getGroupId());
            controllerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    OccurrenceController controller = snapshot.getValue(OccurrenceController.class);
                    if (controller != null) {
                        builder.setTitle("Excluir despesa");

                        Runnable positiveAction = () -> {
                            deleteOnlyThis(occurrence);
                            dismiss();
                        };

                        if (controller.getMaxOccurrences() == 1) {
                            builder.setMessage("Deseja excluir esta despesa?")
                                    .setPositiveButton("Sim", (dialog, which) -> positiveAction.run())
                                    .setNegativeButton("Não", (dialog, which) -> dismiss());
                        } else if (occurrence.getIndex() == 0 || occurrence.getIndex() + 1 == controller.getMaxOccurrences()) {
                            builder.setMessage("Deseja excluir todas as despesas ou apenas esta?")
                                    .setPositiveButton("Apenas esta", (dialog, which) -> positiveAction.run())
                                    .setNegativeButton("Todas", (dialog, which) -> {
                                        deleteAllOccurrences(occurrence);
                                        dismiss();
                                    });
                        } else {
                            builder.setMessage("Deseja excluir todas as despesas, todas as seguintes ou apenas esta?")
                                    .setPositiveButton("Apenas esta", (dialog, which) -> positiveAction.run())
                                    .setNeutralButton("Todas as seguintes", (dialog, which) -> {
                                        deleteAllFollowingOccurrences(occurrence);
                                        dismiss();
                                    })
                                    .setNegativeButton("Todas", (dialog, which) -> {
                                        deleteAllOccurrences(occurrence);
                                        dismiss();
                                    });
                        }

                        builder.show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

        return new MaterialAlertDialogBuilder(requireContext())
                .setView(view)
                .create();
    }

    private void deleteAllOccurrences(Occurrence occurrence) {
        String groupId = occurrence.getGroupId();
        DatabaseReference controllerRef = FirebaseSettings.getOccurrenceControllersReference().child(groupId);
        controllerRef.removeValue();
        DatabaseReference occurrencesRef = FirebaseSettings.getOccurrencesReference();
        occurrencesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot yearSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot monthSnapshot : yearSnapshot.getChildren()) {
                        for (DataSnapshot occurrenceSnapshot : monthSnapshot.getChildren()) {
                            if (Objects.equals(occurrenceSnapshot.child("groupId").getValue(), groupId)) {
                                occurrenceSnapshot.getRef().removeValue();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteAllFollowingOccurrences(Occurrence occurrence) {
        String groupId = occurrence.getGroupId();
        int index = occurrence.getIndex();

        DatabaseReference controllerRef = FirebaseSettings.getOccurrenceControllersReference().child(groupId);
        controllerRef.child("controllIndex").setValue(index);
        controllerRef.child("maxOccurrences").setValue(index);

        DatabaseReference occurrencesRef = FirebaseSettings.getOccurrencesReference();
        occurrencesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot yearSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot monthSnapshot : yearSnapshot.getChildren()) {
                        for (DataSnapshot occurrenceSnapshot : monthSnapshot.getChildren()) {
                            if (Objects.equals(occurrenceSnapshot.child("groupId").getValue(), groupId)) {
                                if (Integer.parseInt(Objects.requireNonNull(occurrenceSnapshot.child("index").getValue()).toString()) >= index) {
                                    occurrenceSnapshot.getRef().removeValue();
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteOnlyThis(Occurrence occurrence) {
        String groupId = occurrence.getGroupId();
        int index = occurrence.getIndex();


        DatabaseReference occurrencesRef = FirebaseSettings.getOccurrencesReference();
        occurrencesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot yearSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot monthSnapshot : yearSnapshot.getChildren()) {
                        for (DataSnapshot occurrenceSnapshot : monthSnapshot.getChildren()) {
                            if (Objects.equals(occurrenceSnapshot.child("groupId").getValue(), groupId)) {
                                int occurrenceIndex = Integer.parseInt(Objects.requireNonNull(occurrenceSnapshot.child("index").getValue()).toString());

                                if (occurrenceIndex == index) {
                                    occurrenceSnapshot.getRef().removeValue();
                                } else if (occurrenceIndex > index) {
                                    DatabaseReference indexRef = occurrenceSnapshot.child("index").getRef();
                                    indexRef.setValue(occurrenceIndex - 1);
                                }
                            }
                        }
                    }
                }
                DatabaseReference controllerRef = FirebaseSettings.getOccurrenceControllersReference().child(groupId);
                controllerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        OccurrenceController occurrenceController = snapshot.getValue(OccurrenceController.class);

                        occurrenceController.setControllIndex(occurrenceController.getControllIndex() - 1);
                        if (occurrenceController.getMaxOccurrences() != -1) {
                            occurrenceController.setMaxOccurrences(occurrenceController.getMaxOccurrences() - 1);
                        }
                        occurrenceController.setLastEditDate(occurrence.getDate());
                        occurrenceController.setLastEditIndex(occurrence.getIndex());

                        OccurrenceControllerService.update(occurrenceController);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
