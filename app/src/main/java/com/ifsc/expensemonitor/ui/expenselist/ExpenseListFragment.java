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
<<<<<<< HEAD
<<<<<<< HEAD
import com.ifsc.expensemonitor.data.Occurrence;
import com.ifsc.expensemonitor.data.PreferenceUtils;
=======
>>>>>>> 8e57fcd (iniciando menu de opções)
=======
import com.ifsc.expensemonitor.data.Occurrence;
import com.ifsc.expensemonitor.data.PreferenceUtils;
>>>>>>> 13ef0d5 (menu de opções finalizado)

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExpenseListFragment extends Fragment {

    private TextView unpaidValueTextView, totalValueTextView;
    private RecyclerView recyclerView;
    private ExpenseListViewModel mViewModel;
    private ExpenseCardAdapter mAdapter;
    private PreferenceUtils mPreferenceUtils;

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

<<<<<<< HEAD
<<<<<<< HEAD
            mPreferenceUtils = new PreferenceUtils(requireContext());

            mViewModel.getCurrentMonthOccurrences().observe(getViewLifecycleOwner(), occurrences -> {
                mAdapter.setExpenses(sortList(occurrences));
                mAdapter.notifyDataSetChanged();
            });

            mPreferenceUtils.setPreferenceChangeListener((sharedPreferences, key) -> {
                if (PreferenceUtils.ORDINATION.equals(key) || PreferenceUtils.MOVE_PAID_TO_END.equals(key)) {
                    List<Occurrence> occurrences = mViewModel.getCurrentMonthOccurrences().getValue();
                    if (occurrences != null) {
                        sortList(occurrences);
                        mAdapter.setExpenses(sortList(occurrences));
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
=======
=======
            mPreferenceUtils = new PreferenceUtils(requireContext());

>>>>>>> 13ef0d5 (menu de opções finalizado)
            mViewModel.getCurrentMonthOccurrences().observe(getViewLifecycleOwner(), occurrences -> {
                mAdapter.setExpenses(sortList(occurrences));
                mAdapter.notifyDataSetChanged();
            });
>>>>>>> 5f814f6 (checkpoint)

            mPreferenceUtils.setPreferenceChangeListener((sharedPreferences, key) -> {
                if (PreferenceUtils.KEY_ORDENACAO.equals(key) || PreferenceUtils.KEY_MOVE_PAID_TO_END.equals(key)) {
                    List<Occurrence> occurrences = mViewModel.getCurrentMonthOccurrences().getValue();
                    if (occurrences != null) {
                        sortList(occurrences);
                        mAdapter.setExpenses(sortList(occurrences));
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }

        mViewModel.getUnpaidValue().observe(getViewLifecycleOwner(), unpaidValue -> unpaidValueTextView.setText(MoneyValue.format(unpaidValue)));
        mViewModel.getTotalValue().observe(getViewLifecycleOwner(), totalValue -> totalValueTextView.setText(MoneyValue.format(totalValue)));

        return view;
    }

    private List<Occurrence> sortList(List<Occurrence> list) {
        String newOrdenacao = mPreferenceUtils.getOrdenacao();

        switch (newOrdenacao) {
            case PreferenceUtils.SORT_DATE:
                Collections.sort(list, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));
                break;
            case PreferenceUtils.SORT_VALUE_ASC:
                Collections.sort(list, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));
                break;
            case PreferenceUtils.SORT_VALUE_DESC:
                Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
                break;
        }

        if (mPreferenceUtils.getMovePaidToEnd()) {
            Collections.sort(list, (o1, o2) -> {
                if (o1.isPaid() && !o2.isPaid()) {
                    return 1;
                } else if (!o1.isPaid() && o2.isPaid()) {
                    return -1;
                } else {
                    return 0;
                }
            });
        }

        return list;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPreferenceUtils.unregisterPreferenceChangeListener();
    }
}

