package com.ifsc.expensemonitor.ui.expenselist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.database.Occurrence;
import com.ifsc.expensemonitor.database.MoneyValue;
import com.ifsc.expensemonitor.database.SimpleDate;

import java.util.List;

public class ExpenseCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Occurrence> occurrenceCards;
    private FragmentManager fragmentManager;

    public ExpenseCardAdapter(List<Occurrence> occurrenceList, FragmentManager fragmentManager) {
        this.occurrenceCards = occurrenceList;
        this.fragmentManager = fragmentManager;
    }

    public void setExpenses(List<Occurrence> occurrences) {
        this.occurrenceCards = occurrences;
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
        Occurrence occurrenceCard = occurrenceCards.get(position);
        ExpenseCardViewHolder expenseCardViewHolder = (ExpenseCardViewHolder) holder;
        expenseCardViewHolder.bind(occurrenceCard, fragmentManager);
    }

    @Override
    public int getItemCount() {
        return occurrenceCards.size();
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

        public void bind(Occurrence occurrenceCard, FragmentManager fragmentManager) {
            String name = occurrenceCard.getName();
            Long value = occurrenceCard.getValue();
            SimpleDate simpleDate = occurrenceCard.getDate();
            boolean isPaid = occurrenceCard.isPaid();

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

            expenseCardView.setOnClickListener(v -> {
                DialogFragment expenseBottomSheetFragment = new ExpenseDialogFragment(occurrenceCard);
                expenseBottomSheetFragment.show(fragmentManager, "expenseDialogFragment");
            });
        }
    }
}
