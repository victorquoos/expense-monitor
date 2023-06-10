package com.ifsc.expensemonitor.ui.monthselector;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.database.MoneyValue;
import com.ifsc.expensemonitor.database.MonthYear;
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
            holder.itemView.findViewById(R.id.monthCardView).setOnClickListener(view -> {
                pagerViewModel.getTargetMonthYear().setValue(monthYear);
                Navigation.findNavController(view).navigateUp();
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
        private MaterialCardView monthCardView;
        private TextView monthTextView, yearTextView, valueTextView;
        private ImageView statusCircleImageView;

        public MonthViewHolder(@NonNull View itemView) {
            super(itemView);
            monthCardView = itemView.findViewById(R.id.monthCardView);
            monthTextView = itemView.findViewById(R.id.monthTextView);
            yearTextView = itemView.findViewById(R.id.yearTextView);
            valueTextView = itemView.findViewById(R.id.valueTextView);
            statusCircleImageView = itemView.findViewById(R.id.statusCircleImageView);
        }

        public void bind(MonthYear monthYear) {
            String month = new DateFormatSymbols().getMonths()[monthYear.getMonth()];
            month = month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase();
            String year = String.valueOf(monthYear.getYear());
            monthTextView.setText(month);
            yearTextView.setText(year);

            if (monthYear.isCurrentMonth()) {
                monthCardView.setStrokeColor(getAttrColor(monthCardView.getContext(), com.google.android.material.R.attr.colorPrimaryInverse));
                monthCardView.setStrokeWidth(8);
            } else {
                monthCardView.setStrokeColor(Color.TRANSPARENT);
                monthCardView.setStrokeWidth(0);
            }

            if (monthYear.hasValue()){
                valueTextView.setVisibility(View.VISIBLE);
                statusCircleImageView.setVisibility(View.VISIBLE);
                valueTextView.setText(MoneyValue.format(monthYear.getTotalValue()));
                if (monthYear.hasOverdueValue()) {
                    statusCircleImageView.setImageTintList(ColorStateList.valueOf(getAttrColor(valueTextView.getContext(), R.attr.colorRed)));
                } else if (monthYear.hasUnpaidValue()) {
                    statusCircleImageView.setImageTintList(ColorStateList.valueOf(getAttrColor(valueTextView.getContext(), R.attr.colorYellow)));
                } else {
                    statusCircleImageView.setImageTintList(ColorStateList.valueOf(getAttrColor(valueTextView.getContext(), R.attr.colorGreen)));
                }
            } else {
                valueTextView.setVisibility(View.GONE);
                statusCircleImageView.setVisibility(View.GONE);
            }
        }
    }

    public static int getAttrColor(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, android.R.style.Theme);
        contextThemeWrapper.getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }
}
