package com.ifsc.expensemonitor.ui.monthlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.ui.expenselist.ExpenseListViewModel;
import com.ifsc.expensemonitor.ui.pager.PagerViewModel;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class MonthYearAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MonthYear> months;
    private PagerViewModel pagerViewModel;

    public MonthYearAdapter(List<MonthYear> months, PagerViewModel pagerViewModel) {
        this.pagerViewModel = pagerViewModel;
        this.months = new ArrayList<>();

        for (int i = 0; i < months.size(); i++) {
            MonthYear monthYear = months.get(i);
            if (monthYear.getMonth() == 0 && (i == 0 || months.get(i - 1).getMonth() != 0)) {
                // Adiciona um separador de ano antes de cada Janeiro.
                this.months.add(new MonthYear(-1, monthYear.getYear()));
            }
            this.months.add(monthYear);
        }
    }

    @Override
    public int getItemViewType(int position) {
        MonthYear monthYear = months.get(position);
        return monthYear.getMonth() == -1 ? 0 : 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == 0) {
            View view = inflater.inflate(R.layout.item_year_separator, parent, false);
            return new YearSeparatorViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_month, parent, false);
            return new MonthViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MonthYear monthYear = months.get(position);

        if (holder instanceof YearSeparatorViewHolder) {
            YearSeparatorViewHolder yearSeparatorViewHolder = (YearSeparatorViewHolder) holder;
            yearSeparatorViewHolder.bind(monthYear);
        } else if (holder instanceof MonthViewHolder) {
            int monthPosition = position - countYearSeparatorsUpToPosition(position);
            holder.itemView.findViewById(R.id.monthCardView).setOnClickListener(view -> {
                if (monthPosition != RecyclerView.NO_POSITION) {
                    pagerViewModel.getVisiblePageIndex().setValue(monthPosition);
                    Navigation.findNavController(view).navigateUp();
                }
            });
            MonthViewHolder monthViewHolder = (MonthViewHolder) holder;
            monthViewHolder.bind(monthYear);
        }
    }

    private int countYearSeparatorsUpToPosition(int position) {
        int count = 0;
        for (int i = 0; i <= position; i++) {
            if (months.get(i).getMonth() == -1) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int getItemCount() {
        return months.size();
    }

    public static class YearSeparatorViewHolder extends RecyclerView.ViewHolder {
        private TextView yearTextView;

        public YearSeparatorViewHolder(@NonNull View itemView) {
            super(itemView);
            yearTextView = itemView.findViewById(R.id.yearTextView);
        }

        public void bind(MonthYear monthYear) {
            String year = String.valueOf(monthYear.getYear());
            yearTextView.setText(year);
        }
    }

    public static class MonthViewHolder extends RecyclerView.ViewHolder {
        private TextView monthTextView;
        private TextView yearTextView;
        private ImageView todayIconImageView;

        public MonthViewHolder(@NonNull View itemView) {
            super(itemView);
            monthTextView = itemView.findViewById(R.id.monthTextView);
            yearTextView = itemView.findViewById(R.id.yearTextView);
            todayIconImageView = itemView.findViewById(R.id.todayIconImageView);
        }

        public void bind(MonthYear monthYear) {
            String month = new DateFormatSymbols().getMonths()[monthYear.getMonth()];
            month = month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase();
            String year = String.valueOf(monthYear.getYear());
            monthTextView.setText(month);
            yearTextView.setText(year);

            if (monthYear.isCurrentMonth()) {
                todayIconImageView.setVisibility(View.VISIBLE);
            } else {
                todayIconImageView.setVisibility(View.INVISIBLE);
            }
        }
    }
}
