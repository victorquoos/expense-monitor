package com.ifsc.expensemonitor.ui.monthlist;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.calendar.CustomSpanSizeLookup;
import com.ifsc.expensemonitor.calendar.MonthYear;
import com.ifsc.expensemonitor.calendar.MonthYearAdapter;
import com.ifsc.expensemonitor.ui.addedit.AddEditFragmentArgs;
import com.ifsc.expensemonitor.ui.expenselist.ExpenseCardAdapter;
import com.ifsc.expensemonitor.ui.start.WelcomeViewModel;

import java.util.List;

public class MonthListFragment extends Fragment {

    private MonthListViewModel mViewModel;
    MaterialToolbar materialToolbar;
    RecyclerView monthsRecyclerView;
    FloatingActionButton goToCurrentMonthButton;
    int month, year;

    public static MonthListFragment newInstance() {
        return new MonthListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month_list, container, false);

        // Declaração dos componentes da tela
        materialToolbar = view.findViewById(R.id.materialToolbar);
        monthsRecyclerView = view.findViewById(R.id.monthsRecyclerView);
        goToCurrentMonthButton = view.findViewById(R.id.goToCurrentMonthButton);

        // Recuperando os dados da tela anterior
        month = AddEditFragmentArgs.fromBundle(getArguments()).getMonth();
        year = AddEditFragmentArgs.fromBundle(getArguments()).getYear();

        monthsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false));
        CustomSpanSizeLookup spanSizeLookup = new CustomSpanSizeLookup(getSapnCount());
        ((GridLayoutManager) monthsRecyclerView.getLayoutManager()).setSpanSizeLookup(spanSizeLookup);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MonthListViewModel.class);

        // Atualiza a lista de meses quando alterado no viewmodel
        mViewModel.getMonthList().observe(getViewLifecycleOwner(), monthList -> {
            monthsRecyclerView.setAdapter(new MonthYearAdapter(monthList));
        });
    }

    private int getSapnCount() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int columnWidth = (int) getResources().getDimension(R.dimen.calendar_column_width);
        int screenWidth = displayMetrics.widthPixels;
        int spanCount = screenWidth / columnWidth;
        if (spanCount < 1) {
            spanCount = 1;
        }
        return spanCount;
    }

}