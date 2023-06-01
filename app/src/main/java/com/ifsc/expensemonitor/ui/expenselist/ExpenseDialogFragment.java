package com.ifsc.expensemonitor.ui.expenselist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

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
        Button changeStatusButton = view.findViewById(R.id.changeStatusButton);

        expenseNameTextView.setText(expense.getName());
        expenseValueTextView.setText(MoneyValue.format(expense.getValue()));
        expenseDateTextView.setText(expense.getDate().getFormattedDate());
        expenseDescriptionTextView.setText(expense.getDescription());

        if (expense.isPaid()) {
            expenseStatusTextView.setText("PAGO");
            changeStatusButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Red));
            changeStatusButton.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(requireContext(), R.drawable.ic_close), null);
            changeStatusButton.setText("MARCAR COMO NÃO PAGO");
        } else {
            expenseStatusTextView.setText("NÃO PAGO");
            changeStatusButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Green));
            changeStatusButton.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(requireContext(), R.drawable.ic_done), null);
            changeStatusButton.setText("MARCAR COMO PAGO");
        }

        if (expenseDescriptionTextView.getText().toString().isEmpty()) {
            view.findViewById(R.id.expenseDescriptionLinearLayout).setVisibility(View.GONE);
        }

        changeStatusButton.setOnClickListener(v -> {
            expense.setPaid(!expense.isPaid());
            expense.update();
            dismiss();
        });

        return new MaterialAlertDialogBuilder(requireContext())
                .setView(view)
                .create();
    }
}
