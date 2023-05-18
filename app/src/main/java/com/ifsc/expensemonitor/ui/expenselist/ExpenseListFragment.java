package com.ifsc.expensemonitor.ui.expenselist;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.database.FirebaseSettings;
import com.ifsc.expensemonitor.database.SimpleDate;
import com.ifsc.expensemonitor.expenselist.ExpenseCardAdapter;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.util.Calendar;

public class ExpenseListFragment extends Fragment {

    private ExpenseListViewModel mViewModel;
    private FloatingActionButton addExpenseButton;
    private RecyclerView expensesReciclerView;
    private TextView paidValueTextView, unpaidValueTextView, totalValueTextView, monthTextView, yearTextView;
    private Button filtersButton, optionsButton, previousMonthButton, nextMonthButton, selectMonthButton;

    public static ExpenseListFragment newInstance() {
        return new ExpenseListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense_list, container, false);


        // Declaração dos componentes da tela
        addExpenseButton = view.findViewById(R.id.addExpenseButton);
        expensesReciclerView = view.findViewById(R.id.expensesReciclerView);
        paidValueTextView = view.findViewById(R.id.paidValueTextView);
        unpaidValueTextView = view.findViewById(R.id.unpaidValueTextView);
        totalValueTextView = view.findViewById(R.id.totalValueTextView);
        monthTextView = view.findViewById(R.id.monthTextView);
        yearTextView = view.findViewById(R.id.yearTextView);
        selectMonthButton = view.findViewById(R.id.selectMonthButton);
        previousMonthButton = view.findViewById(R.id.previousMonthButton);
        nextMonthButton = view.findViewById(R.id.nextMonthButton);
        filtersButton = view.findViewById(R.id.filtersButton);
        optionsButton = view.findViewById(R.id.optionsButton);

        // Definição dos valores dos componentes da tela


        // Ações dos botões
        nextMonthButton.setOnClickListener(v -> mViewModel.goToNextMonth());
        previousMonthButton.setOnClickListener(v -> mViewModel.goToPreviousMonth());
        selectMonthButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_expenseListFragment_to_monthListFragment));


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ExpenseListViewModel.class);

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

        // Atualiza a lista de despesas quando alterado no viewmodel
        mViewModel.getCurrentMonthExpenses().observe(getViewLifecycleOwner(), expenses -> {
            expensesReciclerView.setAdapter(new ExpenseCardAdapter(expenses));

        });

        // Atualiza o mes quando alterado no viewmodel
        mViewModel.getMonth().observe(getViewLifecycleOwner(), month -> {
            String monthText = new DateFormatSymbols().getMonths()[month];
            monthText = monthText.substring(0, 1).toUpperCase() + monthText.substring(1).toLowerCase();
            monthTextView.setText(monthText);
        });

        // Atualiza o ano quando alterado no viewmodel
        mViewModel.getYear().observe(getViewLifecycleOwner(), year -> {
            String yearText = String.valueOf(year);
            yearTextView.setText(yearText);
        });

        // Atualiza o valor pago quando alterado no viewmodel
        mViewModel.getPaidValue().observe(getViewLifecycleOwner(), paidValue -> {
            String paidValueText = currencyFormat.format(paidValue);
            paidValueTextView.setText(paidValueText);
        });

        // Atualiza o valor não pago quando alterado no viewmodel
        mViewModel.getUnpaidValue().observe(getViewLifecycleOwner(), unpaidValue -> {
            String unpaidValueText = currencyFormat.format(unpaidValue);
            unpaidValueTextView.setText(unpaidValueText);
        });

        // Atualiza o valor total quando alterado no viewmodel
        mViewModel.getTotalValue().observe(getViewLifecycleOwner(), totalValue -> {
            String totalValueText = currencyFormat.format(totalValue);
            totalValueTextView.setText(totalValueText);
        });
    }


    private void setMonth(int month, int year) {

    }
}