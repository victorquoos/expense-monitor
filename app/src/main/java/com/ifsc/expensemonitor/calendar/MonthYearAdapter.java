package com.ifsc.expensemonitor.calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.expenselist.ExpenseListActivity;

import java.text.DateFormatSymbols;
import java.util.List;

public class MonthYearAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MonthYear> months;
    private Activity mActivity;

    public MonthYearAdapter(List<MonthYear> months, Activity activity) {
        this.months = months;
        this.mActivity = activity;
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
            holder.itemView.findViewById(R.id.monthCardView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int month = monthYear.getMonth();
                    int year = monthYear.getYear();

                    Context context = view.getContext();
                    Intent intent = new Intent(context, ExpenseListActivity.class);
                    intent.putExtra("month", month);
                    intent.putExtra("year", year);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            });
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
