package com.ifsc.expensemonitor.ui.expenselist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ifsc.expensemonitor.database.Expense;
import com.ifsc.expensemonitor.database.MoneyValue;

public class ExpenseDialogFragment extends DialogFragment {
    private Expense expense;

    public ExpenseDialogFragment(Expense expense) {
        this.expense = expense;
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

        expenseNameTextView.setText(expense.getName());
        expenseValueTextView.setText(MoneyValue.format(expense.getValue()));
        expenseDateTextView.setText(expense.getDate().getFormattedDate());
        expenseDescriptionTextView.setText(expense.getDescription());

        if (expense.isPaid()) {
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
            expense.setPaid(!expense.isPaid());
            expense.update();
            dismiss();
        });

        editButton.setOnClickListener(v -> {
            dismiss();
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);

            Bundle args = new Bundle();
            args.putString("key", expense.getKey());

            navController.navigate(R.id.addEditFragment, args);
        });

        deleteButton.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Excluir despesa")
                    .setMessage("Tem certeza que deseja excluir esta despesa?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        expense.delete();
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
