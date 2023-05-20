package com.ifsc.expensemonitor.ui.monthlist;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.ui.addedit.AddEditFragmentArgs;

public class MonthListFragment extends Fragment {

    private MonthListViewModel mViewModel;
    MaterialToolbar materialToolbar;
    RecyclerView monthsRecyclerView;
    FloatingActionButton goToCurrentMonthButton;
    int month, year;
    RecyclerView.SmoothScroller smoothScroller;
    int scrollToPosition = -1;

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

        // Botão para voltar para o mês atual
        goToCurrentMonthButton.setOnClickListener(v -> scrollToCurrentMonth());

        monthsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), getSapnCount(), LinearLayoutManager.VERTICAL, false));
        CustomSpanSizeLookup spanSizeLookup = new CustomSpanSizeLookup(getSapnCount());
        ((GridLayoutManager) monthsRecyclerView.getLayoutManager()).setSpanSizeLookup(spanSizeLookup);

        // Criação do SmoothScroller
        smoothScroller = new LinearSmoothScroller(monthsRecyclerView.getContext()) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };

        monthsRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (monthsRecyclerView.computeVerticalScrollRange() > monthsRecyclerView.getHeight()) {
                    scrollToCurrentMonth();  // Chamando scrollToCurrentMonth
                    monthsRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

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

    private void scrollToCurrentMonth() {
        smoothScroller.setTargetPosition(getScrollPosition());
        monthsRecyclerView.getLayoutManager().startSmoothScroll(smoothScroller);
    }

    private int getScrollPosition() {
        int indexCurrentMonth = mViewModel.getIndexOfCurrentMonth();
        if (indexCurrentMonth < 0) {
            return 0;
        }
        if (scrollToPosition < 0) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) monthsRecyclerView.getLayoutManager();
            int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
            int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
            int visibleItems = lastVisiblePosition - firstVisiblePosition + 1;

            if (visibleItems > 15) {
                scrollToPosition = (indexCurrentMonth - (indexCurrentMonth % 13));
            } else {
                scrollToPosition = (indexCurrentMonth - (visibleItems / 2 - getSapnCount() * 2));
            }
            if (scrollToPosition < 0) {
                scrollToPosition = 0;
            }
        }
        return scrollToPosition;
    }
}