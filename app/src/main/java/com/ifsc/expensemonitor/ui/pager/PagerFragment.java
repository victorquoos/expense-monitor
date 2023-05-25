package com.ifsc.expensemonitor.ui.pager;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import com.ifsc.expensemonitor.ui.monthlist.MonthYear;

import java.text.DateFormatSymbols;
import java.util.Objects;

public class PagerFragment extends Fragment {
    private PagerViewModel pagerViewModel;
    private TextView monthTextView, yearTextView;
    private Button filtersButton, optionsButton, previousMonthButton, nextMonthButton, selectMonthButton;
    private FloatingActionButton addExpenseButton;
    private ViewPager2 viewPager;
    private boolean isFirstLoad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager, container, false);
        pagerViewModel = new ViewModelProvider(requireActivity()).get(PagerViewModel.class);
        isFirstLoad = true;

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

        // Atualiza o viewpager quando a lista de meses é atualizada
        pagerViewModel.getListOfMonths().observe(getViewLifecycleOwner(), monthYears -> {
            if (monthYears != null) {
                initPager();
            }
        });

        // Realiza um smoothScroll para um mês alvo se não for o primeiro carregamento
        pagerViewModel.getTargetPageIndex().observe(getViewLifecycleOwner(), index -> {
            if (index != null && !isFirstLoad) {
                viewPager.setCurrentItem(index, true);
                pagerViewModel.getTargetPageIndex().setValue(null);
            }
        });

        // Atualiza o mês visível quando o viewpager é atualizado
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                MonthYear currentMonth = Objects.requireNonNull(pagerViewModel.getListOfMonths().getValue()).get(position);
                if (!currentMonth.equals(pagerViewModel.getLastVisibleMonthYear().getValue())) {
                    pagerViewModel.getLastVisibleMonthYear().setValue(currentMonth);
                }
            }
        });

        // Navega para a tela de criação de despesa
        addExpenseButton.setOnClickListener(v -> {
            int month = Objects.requireNonNull(pagerViewModel.getLastVisibleMonthYear().getValue()).getMonth();
            int year = Objects.requireNonNull(pagerViewModel.getLastVisibleMonthYear().getValue()).getYear();
            PagerFragmentDirections.ActionPagerFragmentToAddEditFragment action =
                    PagerFragmentDirections.actionPagerFragmentToAddEditFragment(month, year, "");
            Navigation.findNavController(v).navigate(action);
        });


        // Atualiza o texto do mês e ano quando o mês visível é atualizado //todo
        pagerViewModel.getLastVisibleMonthYear().observe(getViewLifecycleOwner(), monthYear -> {
            if (monthYear != null) {
                String monthText = new DateFormatSymbols().getMonths()[monthYear.getMonth()];
                monthText = monthText.substring(0, 1).toUpperCase() + monthText.substring(1).toLowerCase();
                monthTextView.setText(monthText);
                String yearText = String.valueOf(monthYear.getYear());
                yearTextView.setText(yearText);
            }
        });

        // Atualiza o mês visível quando o botão de mês anterior é clicado
        previousMonthButton.setOnClickListener(v -> {
            int currentPage = viewPager.getCurrentItem();
            if (currentPage > 0) {
                pagerViewModel.getTargetPageIndex().setValue(currentPage - 1);
            }
        });

        // Atualiza o mês visível quando o botão de próximo mês é clicado
        nextMonthButton.setOnClickListener(v -> {
            int currentPage = viewPager.getCurrentItem();
            if (currentPage < Objects.requireNonNull(pagerViewModel.getListOfMonths().getValue()).size() - 1) {
                pagerViewModel.getTargetPageIndex().setValue(currentPage + 1);
            }
        });

        // Navega para a tela de seleção de mês
        selectMonthButton.setOnClickListener(v -> {
            int month = Objects.requireNonNull(pagerViewModel.getLastVisibleMonthYear().getValue()).getMonth();
            int year = Objects.requireNonNull(pagerViewModel.getLastVisibleMonthYear().getValue()).getYear();
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

        final ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Objects.requireNonNull(viewPager.getAdapter()).getItemCount() < 1) {
                    return;
                }
                if (viewPager.getChildCount() > 0) {
                    if (isFirstLoad) {
                        isFirstLoad = false;
                        if (pagerViewModel.isFirstTime()) {
                            pagerViewModel.setFirstTime(false);
                            if (pagerViewModel.getInitialPageIndex().getValue() != null) {
                                viewPager.setCurrentItem(pagerViewModel.getInitialPageIndex().getValue(), false);
                            } else {
                                viewPager.setCurrentItem(0, false);
                            }
                        } else {
                            Integer targetIndex = pagerViewModel.getTargetPageIndex().getValue();
                            pagerViewModel.getInitialPageIndex().setValue(null);
                            MonthYear targetMonthYear = pagerViewModel.getTargetMonthYear().getValue();
                            pagerViewModel.getTargetMonthYear().setValue(null);
                            if (targetIndex != null) {
                                viewPager.setCurrentItem(targetIndex, false);
                            } else if (targetMonthYear != null) {
                                int index = Objects.requireNonNull(pagerViewModel.getListOfMonths().getValue()).indexOf(targetMonthYear);
                                if (index != -1) {
                                    viewPager.setCurrentItem(index, false);
                                } else {
                                    viewPager.setCurrentItem(0, false);
                                }
                            } else {
                                MonthYear lastVisibleMonth = pagerViewModel.getLastVisibleMonthYear().getValue();
                                if (Objects.requireNonNull(pagerViewModel.getListOfMonths().getValue()).contains(lastVisibleMonth)) {
                                    viewPager.setCurrentItem(pagerViewModel.getListOfMonths().getValue().indexOf(lastVisibleMonth), false);
                                } else {
                                    viewPager.setCurrentItem(0, false);
                                }
                            }
                        }
                    } else {

                    }
                    viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                // TODO: implementar a logica para caso a lista atualize
                // Mandar o usuário para a ultima página que ele estava
                // Ou para a página do mês atual
                // Ou para a primeira página
            }
        };
        viewPager.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
    }
}


