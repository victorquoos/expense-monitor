package com.ifsc.expensemonitor.ui.expenselist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.database.Expense;
import com.ifsc.expensemonitor.database.FirebaseSettings;
import com.ifsc.expensemonitor.database.MoneyValue;
import com.ifsc.expensemonitor.database.SimpleDate;

import java.text.NumberFormat;
import java.util.List;

public class ExpenseCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Expense> expenseCards;

    public ExpenseCardAdapter(List<Expense> expenses) {
        this.expenseCards = expenses;
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
        Expense expenseCard = expenseCards.get(position);
        ExpenseCardViewHolder expenseCardViewHolder = (ExpenseCardViewHolder) holder;
        expenseCardViewHolder.bind(expenseCard);
    }

    @Override
    public int getItemCount() {
        return expenseCards.size();
    }

    private static class ExpenseCardViewHolder extends RecyclerView.ViewHolder {
        private TextView expenseNameTextView;
        private TextView expenseValueTextView;
        private TextView expenseDateTextView;
        private TextView expenseStatusTextView;
        private ImageView expenseStatusImageView;
        private CardView expenseCardView;

        public ExpenseCardViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseNameTextView = itemView.findViewById(R.id.expenseNameTextView);
            expenseValueTextView = itemView.findViewById(R.id.expenseValueTextView);
            expenseDateTextView = itemView.findViewById(R.id.expenseDateTextView);
            expenseStatusTextView = itemView.findViewById(R.id.expenseStatusTextView);
            expenseStatusImageView = itemView.findViewById(R.id.expenseStatusImageView);
            expenseCardView = itemView.findViewById(R.id.expenseCardView);
        }

        public void bind(Expense expenseCard) {
            String name = expenseCard.getName();
            Long value = expenseCard.getValue();
            SimpleDate simpleDate = expenseCard.getDate();
            boolean isPaid = expenseCard.isPaid();

            expenseNameTextView.setText(name);
            expenseValueTextView.setText(MoneyValue.format(value));
            expenseDateTextView.setText(simpleDate.getFormattedDate());

            if (isPaid) {
                expenseStatusTextView.setText(R.string.paid);
                expenseStatusImageView.setImageResource(R.drawable.shape_green);
            } else if (simpleDate.isBeforeToday()) {
                expenseStatusTextView.setText(R.string.late);
                expenseStatusImageView.setImageResource(R.drawable.shape_red);
            } else {
                expenseStatusTextView.setText(R.string.pending);
                expenseStatusImageView.setImageResource(R.drawable.shape_yellow);
            }

            expenseCardView.setOnClickListener(v -> { //TODO: implement popup menu
                int month = expenseCard.getDate().getMonth();
                int year = expenseCard.getDate().getYear();
                String key = expenseCard.getKey();
                ExpenseListFragmentDirections.ActionExpenseListFragmentToAddEditFragment action =
                        ExpenseListFragmentDirections.actionExpenseListFragmentToAddEditFragment(month, year, key);
                Navigation.findNavController(v).navigate(action);
            });
        }
    }
}
