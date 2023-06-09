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
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.database.Occurrence;
import com.ifsc.expensemonitor.database.MoneyValue;

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
            //occurrence.update();
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
                    .setMessage("Tem certeza que deseja excluir esta despesa?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        //occurrence.delete();
                        dismiss();
                    })
                    .setNegativeButton("Não", null)
                    .show();
        });

        return new MaterialAlertDialogBuilder(requireContext())
                .setView(view)
                .create();
    }
}
