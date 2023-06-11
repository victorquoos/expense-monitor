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
import com.ifsc.expensemonitor.database.FirebaseSettings;
import com.ifsc.expensemonitor.database.Occurrence;
import com.ifsc.expensemonitor.database.MoneyValue;
import com.ifsc.expensemonitor.database.OccurrenceController;
import com.ifsc.expensemonitor.database.OccurrenceControllerService;
import com.ifsc.expensemonitor.database.OccurrenceService;

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

        DatabaseReference ref = FirebaseSettings.getOccurrencesReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Occurrence> occurrences = new ArrayList<>();
                for (DataSnapshot yearSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot monthSnapshot : yearSnapshot.getChildren()) {
                        for (DataSnapshot occurrenceSnapshot : monthSnapshot.getChildren()) {
                            Occurrence occurrenceData = occurrenceSnapshot.getValue(Occurrence.class);
                            if (Objects.equals(occurrence.getGroupId(), occurrenceData.getGroupId())) {
                                occurrences.add(occurrenceData);
                            }
                        }
                    }
                }
                // tratar aqui
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Excluir despesa")
                    .setMessage("Deseja excluir todas as despesas, apenas essa ou todas as seguintes?")
                    .setPositiveButton("Todas as seguintes", (dialog, which) -> {
                        deleteAllFollowingOccurrences(occurrence);
                        dismiss();
                    })
                    .setNeutralButton("Apenas essa", (dialog, which) -> {
                        deleteOnlyThis(occurrence);
                        dismiss();
                    })
                    .setNegativeButton("Todas", (dialog, which) -> {
                        deleteAllOccurrences(occurrence);
                        dismiss();
                    })
                    .show();

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
                        int controllIndex = occurrenceController.getControllIndex();
                        int maxOccurrences = occurrenceController.getMaxOccurrences();

                        controllerRef.child("controllIndex").setValue(controllIndex - 1);
                        if (maxOccurrences != -1) {
                            controllerRef.child("maxOccurrences").setValue(maxOccurrences - 1);
                        }
                        occurrenceController.generateOccurrences();
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
