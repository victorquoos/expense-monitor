package com.ifsc.expensemonitor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class MonthsAdapter extends RecyclerView.Adapter<MonthsAdapter.MonthsViewHolder> {

    private List<Calendar> months;

    public MonthsAdapter(List<Calendar> months) {
        this.months = months;
    }

    @NonNull
    @Override
    public MonthsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_months, parent, false);
        return new MonthsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthsViewHolder holder, int position) {
        Calendar month = months.get(position);

        // Obtém o nome do mês em minúsculas e capitaliza a primeira letra
        String mes = DateFormatSymbols.getInstance().getMonths()[month.get(Calendar.MONTH)].toLowerCase();
        mes = mes.substring(0, 1).toUpperCase() + mes.substring(1);

        holder.txtMonth.setText(mes + "\n" + month.get(Calendar.YEAR));
    }

    @Override
    public int getItemCount() {
        return months.size();
    }

    public void setData(List<Calendar> newMonths) {
        months.clear();
        months.addAll(newMonths);
        notifyDataSetChanged();
    }

    static class MonthsViewHolder extends RecyclerView.ViewHolder {
        TextView txtMonth;

        MonthsViewHolder(View itemView) {
            super(itemView);
            txtMonth = itemView.findViewById(R.id.txtMonth);
        }

    }
}
