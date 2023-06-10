package com.ifsc.expensemonitor.ui.expenselist;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.data.MoneyValue;

import java.util.ArrayList;

public class ExpenseListFragment extends Fragment {

    private TextView unpaidValueTextView, totalValueTextView;
    private RecyclerView recyclerView;
    private ExpenseListViewModel mViewModel;
    private ExpenseCardAdapter mAdapter;

    public static ExpenseListFragment newInstance(int year, int month) {
        ExpenseListFragment fragment = new ExpenseListFragment();
        Bundle args = new Bundle();
        args.putInt("year", year);
        args.putInt("month", month);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        unpaidValueTextView = view.findViewById(R.id.unpaidValueTextView);
        totalValueTextView = view.findViewById(R.id.totalValueTextView);

        mAdapter = new ExpenseCardAdapter(new ArrayList<>(), getChildFragmentManager());
        recyclerView.setAdapter(mAdapter);

        Bundle args = getArguments();
        if (args != null) {
            int year = args.getInt("year");
            int month = args.getInt("month");

            mViewModel = new ViewModelProvider(this).get(ExpenseListViewModel.class);
            mViewModel.setMonth(String.valueOf(year), String.valueOf(month));

            mViewModel.getCurrentMonthOccurrences().observe(getViewLifecycleOwner(), occurrences -> {
                mAdapter.setExpenses(occurrences);
                mAdapter.notifyDataSetChanged();
            });

            mViewModel.getUnpaidValue().observe(getViewLifecycleOwner(), unpaidValue -> unpaidValueTextView.setText(MoneyValue.format(unpaidValue)));
            mViewModel.getTotalValue().observe(getViewLifecycleOwner(), totalValue -> totalValueTextView.setText(MoneyValue.format(totalValue)));
        }

        return view;
    }
}

