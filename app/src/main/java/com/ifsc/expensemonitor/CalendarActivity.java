package com.ifsc.expensemonitor;

import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MonthsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        recyclerView = findViewById(R.id.recyclerView);

        // Define o número de colunas do GridLayoutManager dinamicamente com base no tamanho do item e do tamanho da tela
        int columnWidth = (int) getResources().getDimension(R.dimen.calendar_column_width);
        int screenWidth = getScreenWidth();
        int spanCount = screenWidth / columnWidth;
        if (spanCount < 1) {
            spanCount = 1;
        }
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));

        // Cria uma lista de anos e meses de 1950 a 100 years no futuro
        List<Calendar> yearsMeses = new ArrayList<>();
        Calendar calendarAtual = Calendar.getInstance();
        int currentMonth = calendarAtual.get(Calendar.YEAR);
        int currentYear = calendarAtual.get(Calendar.MONTH);

        for (int year = 1950; year <= currentMonth + 100; year++) {
            for (int month = 0; month < 12; month++) {
                Calendar yearMonth = Calendar.getInstance();
                yearMonth.set(year, month, 1);
                yearsMeses.add(yearMonth);
            }
        }

        // Encontra o índice do mês atual na lista de years e monthes
        int indexCurrentMonth = -1;
        for (int i = 0; i < yearsMeses.size(); i++) {
            Calendar yearMonth = yearsMeses.get(i);
            if (yearMonth.get(Calendar.YEAR) == currentMonth && yearMonth.get(Calendar.MONTH) == currentYear) {
                indexCurrentMonth = i;
                break;
            }
        }

        adapter = new MonthsAdapter(yearsMeses);
        recyclerView.setAdapter(adapter);

        // Define o RecyclerView para exibir o mês atual
        if (indexCurrentMonth >= 0) {
            recyclerView.scrollToPosition(indexCurrentMonth);
        }
    }

    private int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}
