package com.ifsc.expensemonitor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;

public class MonthYearAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MonthYear> months;

    public MonthYearAdapter(List<MonthYear> months) {
        this.months = months;
    }

    @Override
    public int getItemViewType(int position) {
        MonthYear monthYear = months.get(position);
        return monthYear.getMonth() == Calendar.JANUARY ? 0 : 1;
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
            MonthViewHolder monthViewHolder = (MonthViewHolder) holder;
            monthViewHolder.bind(monthYear);
        }
    }

    @Override
    public int getItemCount() {
        return months.size();
    }

    public static class YearSeparatorViewHolder extends RecyclerView.ViewHolder {
        private TextView yearTextView;

        public YearSeparatorViewHolder(@NonNull View itemView) {
            super(itemView);
            yearTextView = itemView.findViewById(R.id.year_text_view);
        }

        public void bind(MonthYear monthYear) {
            String year = String.valueOf(monthYear.getYear());
            yearTextView.setText(year);
        }
    }

    public static class MonthViewHolder extends RecyclerView.ViewHolder {
        private TextView monthTextView;

        public MonthViewHolder(@NonNull View itemView) {
            super(itemView);
            monthTextView = itemView.findViewById(R.id.month_text_view);
        }

        public void bind(MonthYear monthYear) {
            String month = new DateFormatSymbols().getMonths()[monthYear.getMonth()];
            monthTextView.setText(month);
        }
    }
}
