package com.ifsc.expensemonitor.expenselist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ifsc.expensemonitor.R;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

public class ExpenseCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ExpenseCard> expenses;

    public ExpenseCardAdapter(List<ExpenseCard> expenses) {
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_expense_card, parent, false);
        return new ExpenseCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ExpenseCard expenseCard = expenses.get(position);
        ExpenseCardViewHolder expenseCardViewHolder = (ExpenseCardViewHolder) holder;
        expenseCardViewHolder.bind(expenseCard);
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    private static class ExpenseCardViewHolder extends RecyclerView.ViewHolder {

        private TextView expenseNameTextView;
        private TextView expenseValueTextView;
        private TextView expenseDateTextView;
        private TextView expenseStatusTextView;

        public ExpenseCardViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseNameTextView = itemView.findViewById(R.id.expenseNameTextView);
            expenseValueTextView = itemView.findViewById(R.id.expenseValueTextView);
            expenseDateTextView = itemView.findViewById(R.id.expenseDateTextView);
            expenseStatusTextView = itemView.findViewById(R.id.expenseStatusTextView);
        }

        public void bind(ExpenseCard expenseCard) {
            String name = expenseCard.getName();
            Double value = expenseCard.getValue();
            Date date = expenseCard.getDate();
            boolean isPaid = expenseCard.isPaid();

            expenseNameTextView.setText(name);
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
            expenseValueTextView.setText(currencyFormat.format(value));
            expenseDateTextView.setText(date.toString());

            if (date.before(new Date())) {
                expenseStatusTextView.setText(R.string.late);
            } else {
                if (!isPaid) {
                    expenseStatusTextView.setText(R.string.pending);
                } else {
                    expenseStatusTextView.setText(R.string.paid);
                }
            }
        }
    }
}