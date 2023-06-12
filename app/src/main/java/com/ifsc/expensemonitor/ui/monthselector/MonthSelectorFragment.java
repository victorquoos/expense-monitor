package com.ifsc.expensemonitor.ui.monthselector;

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
import com.ifsc.expensemonitor.ui.pager.PagerViewModel;

public class MonthSelectorFragment extends Fragment {

    private PagerViewModel pagerViewModel;
    MaterialToolbar materialToolbar;
    RecyclerView monthsRecyclerView;
    FloatingActionButton goToCurrentMonthButton;
    int month, year;
    RecyclerView.SmoothScroller smoothScroller;
    int scrollToPosition = -1;

    public static MonthSelectorFragment newInstance() {
        return new MonthSelectorFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month_selector, container, false);
        pagerViewModel = new ViewModelProvider(requireActivity()).get(PagerViewModel.class);

        // Declaração dos componentes da tela
        materialToolbar = view.findViewById(R.id.materialToolbar);
        monthsRecyclerView = view.findViewById(R.id.monthsRecyclerView);
        goToCurrentMonthButton = view.findViewById(R.id.goToCurrentMonthButton);

        // Recuperando os dados da tela anterior
        month = AddEditFragmentArgs.fromBundle(getArguments()).getMonth();
        year = AddEditFragmentArgs.fromBundle(getArguments()).getYear();

        // Botão para voltar para o mês atual
        materialToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        materialToolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        monthsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), getSapnCount(), LinearLayoutManager.VERTICAL, false));
        MonthSelectorSpanSizeLookup spanSizeLookup = new MonthSelectorSpanSizeLookup(getSapnCount());
        ((GridLayoutManager) monthsRecyclerView.getLayoutManager()).setSpanSizeLookup(spanSizeLookup);

        // Criação do SmoothScroller
        smoothScroller = new LinearSmoothScroller(monthsRecyclerView.getContext()) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };

        // Atualiza a lista de meses quando alterado no viewmodel
        pagerViewModel.getListOfMonths().observe(getViewLifecycleOwner(), listOfMonths -> {
            monthsRecyclerView.setAdapter(new MonthYearAdapter(listOfMonths, pagerViewModel));
        });

        // Realiza o scroll para o mês atual quando a lista de meses é atualizada
        monthsRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (monthsRecyclerView.computeVerticalScrollRange() > monthsRecyclerView.getHeight()) {
                    scrollToCurrentMonth();
                    monthsRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        return view;
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
        LinearLayoutManager layoutManager = (LinearLayoutManager) monthsRecyclerView.getLayoutManager();
        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
        int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
        int visibleItems = lastVisiblePosition - firstVisiblePosition + 1;
        int indexCurrentMonth = pagerViewModel.getCurrentMonthIndex();
        indexCurrentMonth += (indexCurrentMonth / 12) + 1;

        if (visibleItems > 15) {
            scrollToPosition = (indexCurrentMonth - (indexCurrentMonth % 13));
        } else {
            scrollToPosition = (indexCurrentMonth - (visibleItems / 2 - getSapnCount() * 2));
        }
        if (scrollToPosition < 0) {
            scrollToPosition = 0;
        }

        if (scrollToPosition < firstVisiblePosition - visibleItems || scrollToPosition > lastVisiblePosition + visibleItems) {
            int intermediatePosition;
            if (scrollToPosition > lastVisiblePosition) {
                intermediatePosition = scrollToPosition - visibleItems;
            } else {
                intermediatePosition = scrollToPosition + visibleItems;
            }
            intermediatePosition = Math.max(0, Math.min(intermediatePosition, monthsRecyclerView.getAdapter().getItemCount() - 1));

            monthsRecyclerView.getLayoutManager().scrollToPosition(intermediatePosition);

            monthsRecyclerView.post(() -> {
                smoothScroller.setTargetPosition(scrollToPosition);
                monthsRecyclerView.getLayoutManager().startSmoothScroll(smoothScroller);
            });
        } else {
            smoothScroller.setTargetPosition(scrollToPosition);
            monthsRecyclerView.getLayoutManager().startSmoothScroll(smoothScroller);
        }
    }
}