package com.ifsc.expensemonitor.ui.pager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.database.FirebaseSettings;
import com.ifsc.expensemonitor.ui.monthlist.MonthYear;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PagerFragment extends Fragment {
    private ViewPager2 viewPager;
    private int totalMonths;
    private int firstYearValue, lastYearValue;
    private int currentMonthPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager, container, false);

        // Encontra o ViewPager no layout
        viewPager = view.findViewById(R.id.viewPager);

        // Aqui, faça a consulta ao Firebase para obter o primeiro e o último ano
        // Então, calcule o total de meses e inicialize o ViewPager
        DatabaseReference yearsReference = FirebaseSettings.getUserReference().child("expenses");
        yearsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Integer> years = new ArrayList<>();
                for (DataSnapshot year : snapshot.getChildren()) {
                    String yearText = year.getKey();
                    if (yearText != null && yearText.startsWith("year")) {
                        int yearNumber = Integer.parseInt(yearText.substring(4));
                        years.add(yearNumber);
                    }
                }
                Collections.sort(years);

                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH);

                if (!years.isEmpty()) {
                    firstYearValue = Math.min(years.get(0), currentYear - 1);
                    lastYearValue = Math.max(years.get(years.size() - 1), currentYear + 1);
                } else {
                    firstYearValue = currentYear - 1;
                    lastYearValue = currentYear + 1;
                }

                totalMonths = (lastYearValue - firstYearValue + 1) * 12;
                currentMonthPage = (currentYear - firstYearValue) * 12 + currentMonth;

                initPager();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void initPager() {
        viewPager.setAdapter(new FragmentStateAdapter(getChildFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                int yearForPosition = firstYearValue + position / 12;
                int monthForPosition = position % 12;
                return MonthFragment.newInstance(yearForPosition, monthForPosition);
            }

            @Override
            public int getItemCount() {
                return totalMonths;
            }
        });

        viewPager.setCurrentItem(currentMonthPage, false);
    }
}

