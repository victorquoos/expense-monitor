package com.ifsc.expensemonitor.ui.pager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.ui.expenselist.ExpenseCardAdapter;
import com.ifsc.expensemonitor.ui.expenselist.ExpenseListViewModel;

import java.text.DateFormatSymbols;
import java.util.ArrayList;

public class MonthFragment extends Fragment {

    private RecyclerView recyclerView;
    private ExpenseListViewModel mViewModel;
    private ExpenseCardAdapter mAdapter;

    public static MonthFragment newInstance(int year, int month) {
        MonthFragment fragment = new MonthFragment();
        Bundle args = new Bundle();
        args.putInt("year", year);
        args.putInt("month", month);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        mViewModel = new ViewModelProvider(this).get(ExpenseListViewModel.class);

        mAdapter = new ExpenseCardAdapter(new ArrayList<>(), getChildFragmentManager());
        recyclerView.setAdapter(mAdapter);

        Bundle args = getArguments();
        if (args != null) {
            int year = args.getInt("year");
            int month = args.getInt("month");

            mViewModel.goToMonth(month, year);

            mViewModel.getCurrentMonthExpenses().observe(getViewLifecycleOwner(), expenses -> {
                mAdapter.setExpenses(expenses);
                mAdapter.notifyDataSetChanged();
            });
        }

        return view;
    }
}

