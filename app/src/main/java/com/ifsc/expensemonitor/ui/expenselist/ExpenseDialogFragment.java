package com.ifsc.expensemonitor.ui.expenselist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.database.Expense;
import com.ifsc.expensemonitor.database.MoneyValue;

public class ExpenseDialogFragment extends BottomSheetDialogFragment {
    private Expense expense;

    public ExpenseDialogFragment(Expense expense) {
        this.expense = expense;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense_dialog, container, false);

        TextView expenseNameTextView = view.findViewById(R.id.expenseNameTextView);
        TextView expenseValueTextView = view.findViewById(R.id.expenseValueTextView);
        TextView expenseDateTextView = view.findViewById(R.id.expenseDateTextView);
        TextView expenseDescriptionTextView = view.findViewById(R.id.expenseDescriptionTextView);

        expenseNameTextView.setText(expense.getName());
        expenseValueTextView.setText(MoneyValue.format(expense.getValue()));
        expenseDateTextView.setText(expense.getDate().getFormattedDate());
        expenseDescriptionTextView.setText(expense.getDescription());

        return view;
    }
}
