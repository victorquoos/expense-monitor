package com.ifsc.expensemonitor.ui.pager;

<<<<<<< HEAD
<<<<<<< HEAD
=======
import android.content.Context;
import android.content.SharedPreferences;
>>>>>>> 8e57fcd (iniciando menu de opções)
=======
>>>>>>> 13ef0d5 (menu de opções finalizado)
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.data.PreferenceUtils;
import com.ifsc.expensemonitor.ui.expenselist.ExpenseListFragment;
<<<<<<< HEAD
<<<<<<< HEAD
import com.ifsc.expensemonitor.data.MonthYear;
=======
import com.ifsc.expensemonitor.database.MonthYear;
>>>>>>> 5f814f6 (checkpoint)
=======
import com.ifsc.expensemonitor.data.MonthYear;
>>>>>>> 8e57fcd (iniciando menu de opções)

import java.text.DateFormatSymbols;
import java.util.List;
import java.util.Objects;

public class PagerFragment extends Fragment {
    private PagerViewModel pagerViewModel;
    private TextView monthTextView, yearTextView;
    private Button optionsButton, settingsButton, previousMonthButton, nextMonthButton, selectMonthButton;
    private FloatingActionButton addExpenseButton;
    private ViewPager2 viewPager;
    private boolean isLoading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager, container, false);

        View loadingView = inflater.inflate(R.layout.layout_loading, container, false);
        ViewGroup root = (ViewGroup) container.getRootView();
        root.addView(loadingView);

        // Inicialização do viewmodel
        pagerViewModel = new ViewModelProvider(requireActivity()).get(PagerViewModel.class);

        // Declaração dos componentes da tela
        monthTextView = view.findViewById(R.id.monthTextView);
        yearTextView = view.findViewById(R.id.yearTextView);
        selectMonthButton = view.findViewById(R.id.selectMonthButton);
        previousMonthButton = view.findViewById(R.id.previousMonthButton);
        nextMonthButton = view.findViewById(R.id.nextMonthButton);
        optionsButton = view.findViewById(R.id.optionsButton);
        settingsButton = view.findViewById(R.id.settingsButton);
        addExpenseButton = view.findViewById(R.id.addExpenseButton);
        viewPager = view.findViewById(R.id.viewPager);

        // Atualiza o viewpager quando a lista de meses é atualizada
        pagerViewModel.getListOfMonths().observe(getViewLifecycleOwner(), monthYears -> {
            if (monthYears != null) {
                isLoading = true;
                initPager(root, loadingView);
            }
        });

        // Realiza um smoothScroll para um mês alvo se não for o primeiro carregamento
        pagerViewModel.getTargetPageIndex().observe(getViewLifecycleOwner(), index -> {
            if (index != null && !isLoading) {
                viewPager.setCurrentItem(index, true);
                pagerViewModel.getTargetPageIndex().postValue(null);
            }
        });

        // Atualiza o mês visível quando o viewpager é atualizado
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (!isLoading) {
                    MonthYear currentMonth = Objects.requireNonNull(pagerViewModel.getListOfMonths().getValue()).get(position);
                    pagerViewModel.getLastVisibleMonthYear().setValue(currentMonth);
                }
            }
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

        // Navega para a tela de criação de despesa
        addExpenseButton.setOnClickListener(v -> {
            int month = Objects.requireNonNull(pagerViewModel.getLastVisibleMonthYear().getValue()).getMonth();
            int year = Objects.requireNonNull(pagerViewModel.getLastVisibleMonthYear().getValue()).getYear();
            PagerFragmentDirections.ActionPagerFragmentToAddEditFragment action =
                    PagerFragmentDirections.actionPagerFragmentToAddEditFragment(month, year, "");
            Navigation.findNavController(v).navigate(action);
        });

        // Pop up de opções
        optionsButton.setOnClickListener(v -> showOptionsPopup());

<<<<<<< HEAD
<<<<<<< HEAD
        // Tela de configurações
        settingsButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_pagerFragment_to_settingsFragment));

=======
>>>>>>> 8e57fcd (iniciando menu de opções)
=======
        // Tela de configurações
        settingsButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_pagerFragment_to_settingsFragment));

>>>>>>> fb51114 (muita coisa)
        return view;
    }

    private void initPager(ViewGroup root, View loadingView) {

        viewPager.setAdapter(new FragmentStateAdapter(getChildFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                MonthYear monthYear = Objects.requireNonNull(pagerViewModel.getListOfMonths().getValue()).get(position);
                return ExpenseListFragment.newInstance(monthYear.getYear(), monthYear.getMonth());
            }

            @Override
            public int getItemCount() {
                return Objects.requireNonNull(pagerViewModel.getListOfMonths().getValue()).size();
            }
        });

        final ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                RecyclerView.Adapter<?> adapter = viewPager.getAdapter();
                if (adapter == null || adapter.getItemCount() < 1) {
                    return;
                }

                if (viewPager.getChildCount() > 0) {
                    int startAtPage = determineStartPage();
                    pagerViewModel.getLastVisibleMonthYear().setValue(Objects.requireNonNull(pagerViewModel.getListOfMonths().getValue()).get(startAtPage));

                    isLoading = false;
                    pagerViewModel.setFirstTime(false);
                    viewPager.setCurrentItem(startAtPage, false);
                    viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    root.removeView(loadingView);
                }
            }
        };

        viewPager.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
    }

    private int determineStartPage() {
        List<MonthYear> listOfMonths = pagerViewModel.getListOfMonths().getValue();
        if (listOfMonths == null) {
            return 0;
        }

        if (isLoading) {
            if (pagerViewModel.isFirstTime()) {
                return pagerViewModel.getCurrentMonthIndex();
            } else {
                Integer targetIndex = pagerViewModel.getTargetPageIndex().getValue();
                pagerViewModel.getTargetPageIndex().setValue(null);
                if (targetIndex != null) {
                    return targetIndex;
                }

                MonthYear targetMonthYear = pagerViewModel.getTargetMonthYear().getValue();
                pagerViewModel.getTargetMonthYear().setValue(null);
                if (targetMonthYear != null && listOfMonths.contains(targetMonthYear)) {
                    return listOfMonths.indexOf(targetMonthYear);
                }

                MonthYear lastVisibleMonth = pagerViewModel.getLastVisibleMonthYear().getValue();
                if (lastVisibleMonth != null && listOfMonths.contains(lastVisibleMonth)) {
                    return listOfMonths.indexOf(lastVisibleMonth);
                }
            }
        } else {
            MonthYear lastVisibleMonth = pagerViewModel.getLastVisibleMonthYear().getValue();
            if (lastVisibleMonth != null && listOfMonths.contains(lastVisibleMonth)) {
                return listOfMonths.indexOf(lastVisibleMonth);
            }
        }

        return pagerViewModel.getCurrentMonthIndex();
    }

    private void showOptionsPopup() {
        // Get the root view of the fragment or activity
        ViewGroup rootView = (ViewGroup) requireView().getRootView();

        // Inflate the popup layout with the root view as the parent
        View popupView = getLayoutInflater().inflate(R.layout.view_options, rootView, false);

        // Find the UI elements inside the popup
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        // Get the optionsButton from your fragment
        RadioGroup radioGroupSort = popupView.findViewById(R.id.sortRadioGroup);
        RadioButton radioSortDate = popupView.findViewById(R.id.sortByDateRadioButton);
        RadioButton radioSortValueAsc = popupView.findViewById(R.id.sortByValueAscRadioButton);
        RadioButton radioSortValueDesc = popupView.findViewById(R.id.sortByValueDescRadioButton);
        CheckBox checkMovePaidToBottom = popupView.findViewById(R.id.movePaidToBottomCheckBox);

        // Get the optionsButton from your fragment
        Button optionsButton = requireView().findViewById(R.id.optionsButton);

        // Calculate the display coordinates for the popup
        int offsetX = optionsButton.getLeft();
        int offsetY = optionsButton.getTop();

        // Display the popup at the desired coordinates
        popupWindow.showAsDropDown(optionsButton, offsetX, offsetY);

        // Set the popup to close when the user taps outside of it
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Use the PreferenceUtils class to get the current settings
        PreferenceUtils preferenceUtils = new PreferenceUtils(requireContext());
        String ordenacao = preferenceUtils.getOrdenacao();
        boolean movePaidToEnd = preferenceUtils.getMovePaidToEnd();

        // Set the selected options in the popup
        switch (ordenacao) {
<<<<<<< HEAD
<<<<<<< HEAD
            case PreferenceUtils.SORT_DATE:
                radioSortDate.setChecked(true);
                break;
            case PreferenceUtils.SORT_VALUE_ASC:
                radioSortValueAsc.setChecked(true);
                break;
            case PreferenceUtils.SORT_VALUE_DESC:
=======
            case "data_asc":
=======
            case PreferenceUtils.SORT_DATE:
>>>>>>> 13ef0d5 (menu de opções finalizado)
                radioSortDate.setChecked(true);
                break;
            case PreferenceUtils.SORT_VALUE_ASC:
                radioSortValueAsc.setChecked(true);
                break;
<<<<<<< HEAD
            case "valor_desc":
>>>>>>> 8e57fcd (iniciando menu de opções)
=======
            case PreferenceUtils.SORT_VALUE_DESC:
>>>>>>> 13ef0d5 (menu de opções finalizado)
                radioSortValueDesc.setChecked(true);
                break;
        }

        checkMovePaidToBottom.setChecked(movePaidToEnd);

        // Apply the settings automatically
        radioGroupSort.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.sortByDateRadioButton) {
<<<<<<< HEAD
<<<<<<< HEAD
                preferenceUtils.setOrdenacao(PreferenceUtils.SORT_DATE);
            } else if (checkedId == R.id.sortByValueAscRadioButton) {
                preferenceUtils.setOrdenacao(PreferenceUtils.SORT_VALUE_ASC);
            } else if (checkedId == R.id.sortByValueDescRadioButton) {
                preferenceUtils.setOrdenacao(PreferenceUtils.SORT_VALUE_DESC);
=======
                preferenceUtils.setOrdenacao("data_asc");
=======
                preferenceUtils.setOrdenacao(PreferenceUtils.SORT_DATE);
>>>>>>> 13ef0d5 (menu de opções finalizado)
            } else if (checkedId == R.id.sortByValueAscRadioButton) {
                preferenceUtils.setOrdenacao(PreferenceUtils.SORT_VALUE_ASC);
            } else if (checkedId == R.id.sortByValueDescRadioButton) {
<<<<<<< HEAD
                preferenceUtils.setOrdenacao("valor_desc");
>>>>>>> 8e57fcd (iniciando menu de opções)
=======
                preferenceUtils.setOrdenacao(PreferenceUtils.SORT_VALUE_DESC);
>>>>>>> 13ef0d5 (menu de opções finalizado)
            }
        });

        checkMovePaidToBottom.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceUtils.setMovePaidToEnd(isChecked);
        });
    }

}
