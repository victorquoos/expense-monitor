package com.ifsc.expensemonitor.ui.pager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.ui.expenselist.ExpenseListFragmentDirections;
import com.ifsc.expensemonitor.ui.monthlist.MonthYear;

import java.text.DateFormatSymbols;
import java.util.Objects;

public class PagerFragment extends Fragment {
    private PagerViewModel pagerViewModel;
    private TextView monthTextView, yearTextView;
    private Button filtersButton, optionsButton, previousMonthButton, nextMonthButton, selectMonthButton;
    private FloatingActionButton addExpenseButton;
    private ViewPager2 viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager, container, false);

        // Declaração dos componentes da tela
        monthTextView = view.findViewById(R.id.monthTextView);
        yearTextView = view.findViewById(R.id.yearTextView);
        selectMonthButton = view.findViewById(R.id.selectMonthButton);
        previousMonthButton = view.findViewById(R.id.previousMonthButton);
        nextMonthButton = view.findViewById(R.id.nextMonthButton);
        filtersButton = view.findViewById(R.id.filtersButton);
        optionsButton = view.findViewById(R.id.optionsButton);
        addExpenseButton = view.findViewById(R.id.addExpenseButton);
        viewPager = view.findViewById(R.id.viewPager);

        // Inicialização do ViewModel
        pagerViewModel = new ViewModelProvider(requireActivity()).get(PagerViewModel.class);
        pagerViewModel.startListenerToGetYears();

        // Navega para a tela de criação de despesa
        addExpenseButton.setOnClickListener(v -> {
            int month = pagerViewModel.getVisibleMonth();
            int year = pagerViewModel.getVisibleYear();
            PagerFragmentDirections.ActionPagerFragmentToAddEditFragment action =
                    PagerFragmentDirections.actionPagerFragmentToAddEditFragment(month, year, "");
            Navigation.findNavController(v).navigate(action);
        });

        // Atualiza o viewpager quando a lista de meses é atualizada
        pagerViewModel.getListOfMonths().observe(getViewLifecycleOwner(), monthYears -> {
            if (monthYears != null) {
                initPager();
            }
        });

        // Atualiza o viewpager quando o mês visível é atualizado
        pagerViewModel.getVisiblePageIndex().observe(getViewLifecycleOwner(), index -> {
            if (index != null) {
                viewPager.setCurrentItem(index, true);
            }
        });

        // Atualiza o mês visível quando o viewpager é atualizado
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Integer currentPage = pagerViewModel.getVisiblePageIndex().getValue();
                if (currentPage == null || currentPage != position) {
                    pagerViewModel.getVisiblePageIndex().setValue(position);
                }
            }
        });

        // Atualiza o texto do mês e ano quando o mês visível é atualizado
        pagerViewModel.getVisiblePageIndex().observe(getViewLifecycleOwner(), index -> {
            if (index != null) {
                MonthYear monthYear = Objects.requireNonNull(pagerViewModel.getListOfMonths().getValue()).get(index);
                String monthText = new DateFormatSymbols().getMonths()[monthYear.getMonth()];
                monthText = monthText.substring(0, 1).toUpperCase() + monthText.substring(1).toLowerCase();
                monthTextView.setText(monthText);
                String yearText = String.valueOf(monthYear.getYear());
                yearTextView.setText(yearText);
            }
        });

        // Atualiza o mês visível quando o botão de mês anterior é clicado
        previousMonthButton.setOnClickListener(v -> {
            int index = Objects.requireNonNull(pagerViewModel.getVisiblePageIndex().getValue());
            if (index > 0) {
                pagerViewModel.getVisiblePageIndex().setValue(index - 1);
            }
        });

        // Atualiza o mês visível quando o botão de próximo mês é clicado
        nextMonthButton.setOnClickListener(v -> {
            int index = Objects.requireNonNull(pagerViewModel.getVisiblePageIndex().getValue());
            if (index < Objects.requireNonNull(pagerViewModel.getListOfMonths().getValue()).size() - 1) {
                pagerViewModel.getVisiblePageIndex().setValue(index + 1);
            }
        });

        // Navega para a tela de seleção de mês
        selectMonthButton.setOnClickListener(v -> {
            int month = pagerViewModel.getVisibleMonth();
            int year = pagerViewModel.getVisibleYear();
            PagerFragmentDirections.ActionPagerFragmentToMonthListFragment action =
                    PagerFragmentDirections.actionPagerFragmentToMonthListFragment(month, year);
            Navigation.findNavController(v).navigate(action);
        });

        return view;
    }

    private void initPager() {
        viewPager.setAdapter(new FragmentStateAdapter(getChildFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                MonthYear monthYear = Objects.requireNonNull(pagerViewModel.getListOfMonths().getValue()).get(position);
                return MonthFragment.newInstance(monthYear.getYear(), monthYear.getMonth());
            }

            @Override
            public int getItemCount() {
                return Objects.requireNonNull(pagerViewModel.getListOfMonths().getValue()).size();
            }
        });

        viewPager.setCurrentItem(pagerViewModel.getCurrentMonthIndex().getValue(), false);
    }
}

