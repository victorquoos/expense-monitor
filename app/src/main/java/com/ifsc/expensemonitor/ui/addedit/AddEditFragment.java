package com.ifsc.expensemonitor.ui.addedit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.database.MonthYear;
import com.ifsc.expensemonitor.database.Occurrence;
import com.ifsc.expensemonitor.database.FirebaseSettings;
import com.ifsc.expensemonitor.database.MoneyValue;
import com.ifsc.expensemonitor.database.OccurrenceController;
import com.ifsc.expensemonitor.database.OccurrenceControllerService;
import com.ifsc.expensemonitor.database.OccurrenceService;
import com.ifsc.expensemonitor.database.SimpleDate;
import com.ifsc.expensemonitor.ui.pager.PagerViewModel;

import java.util.Objects;

public class AddEditFragment extends Fragment {

    private MaterialToolbar materialToolbar;
    private TextView nameLabelTextView, typeLabelTextView, parcelLabelTextView, intervalInMonthsLabelTextView, valueLabelTextView, dateLabelTextView, descriptionLabelTextView;
    private EditText expenseNameEditText, expenseParcelEditText, expenseIntervalInMonthsEditText, expenseValueEditText, expenseDateEditText, expenseDescriptionEditText;
    private MaterialButtonToggleGroup expenseTypeButtonToggleGroup;
    private MaterialButton singleTypeButton, parceledTypeButton, recurringTypeButton;
    private TextView expenseValueTextView;
    private ExtendedFloatingActionButton saveExpenseButton;
    int month, year;
    private String id;
    private Occurrence occurrence;
    private OccurrenceController occurrenceController;
    SimpleDate selectedDate;
    private boolean addMode = true;

    public static AddEditFragment newInstance() {
        return new AddEditFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit, container, false);

        // Declaração dos componentes da tela
        materialToolbar = view.findViewById(R.id.materialToolbar);
        nameLabelTextView = view.findViewById(R.id.nameLabelTextView);
        typeLabelTextView = view.findViewById(R.id.typeLabelTextView);
        parcelLabelTextView = view.findViewById(R.id.parcelLabelTextView);
        intervalInMonthsLabelTextView = view.findViewById(R.id.intervalInMonthsLabelTextView);
        valueLabelTextView = view.findViewById(R.id.valueLabelTextView);
        dateLabelTextView = view.findViewById(R.id.dateLabelTextView);
        descriptionLabelTextView = view.findViewById(R.id.descriptionLabelTextView);
        expenseNameEditText = view.findViewById(R.id.expenseNameEditText);
        expenseParcelEditText = view.findViewById(R.id.expenseParcelEditText);
        expenseIntervalInMonthsEditText = view.findViewById(R.id.expenseIntervalInMonthsEditText);
        expenseValueEditText = view.findViewById(R.id.expenseValueEditText);
        expenseDateEditText = view.findViewById(R.id.expenseDateEditText);
        expenseDescriptionEditText = view.findViewById(R.id.expenseDescriptionEditText);
        expenseTypeButtonToggleGroup = view.findViewById(R.id.expenseTypeButtonToggleGroup);
        singleTypeButton = view.findViewById(R.id.singleTypeButton);
        parceledTypeButton = view.findViewById(R.id.parceledTypeButton);
        recurringTypeButton = view.findViewById(R.id.recurringTypeButton);
        expenseValueTextView = view.findViewById(R.id.expenseValueTextView);
        saveExpenseButton = view.findViewById(R.id.saveExpenseButton);

        // Recuperando os dados da tela anterior
        month = AddEditFragmentArgs.fromBundle(getArguments()).getMonth();
        year = AddEditFragmentArgs.fromBundle(getArguments()).getYear();
        id = AddEditFragmentArgs.fromBundle(getArguments()).getId();

        // Configuração da toolbar
        materialToolbar.setNavigationOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        // Observador para o botão de tipo de despesa
        expenseTypeButtonToggleGroup.addOnButtonCheckedListener(expenseTypeButtonToggleGroupListener);

        // Configuração do textwatcher do valor da despesa
        expenseValueEditText.addTextChangedListener(expenseValueEditTextWatcher);

        // Configuração do datepicker
        expenseDateEditText.setOnClickListener(v -> openDatePicker());

        // Botão para salvar despesa
        saveExpenseButton.setOnClickListener(v -> saveExpense(view));

        // Configuração dos valores iniciais
        setInitialValues(month, year, id);

        return view;
    }

    private final MaterialButtonToggleGroup.OnButtonCheckedListener expenseTypeButtonToggleGroupListener = (group, checkedId, isChecked) -> {
        if (isChecked) {
            if (checkedId == R.id.singleTypeButton) {
                parcelLabelTextView.setVisibility(View.GONE);
                expenseParcelEditText.setVisibility(View.GONE);
                intervalInMonthsLabelTextView.setVisibility(View.GONE);
                expenseIntervalInMonthsEditText.setVisibility(View.GONE);
            } else if (checkedId == R.id.parceledTypeButton) {
                parcelLabelTextView.setVisibility(View.VISIBLE);
                expenseParcelEditText.setVisibility(View.VISIBLE);
                intervalInMonthsLabelTextView.setVisibility(View.VISIBLE);
                expenseIntervalInMonthsEditText.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.recurringTypeButton) {
                parcelLabelTextView.setVisibility(View.GONE);
                expenseParcelEditText.setVisibility(View.GONE);
                intervalInMonthsLabelTextView.setVisibility(View.VISIBLE);
                expenseIntervalInMonthsEditText.setVisibility(View.VISIBLE);
            }
        }
    };

    private final TextWatcher expenseValueEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String string = s.toString();

            if (string.startsWith("0")) {
                string = string.replaceAll("^0+", "");
                expenseValueEditText.setText(string);
            }
            try {
                expenseValueTextView.setText(MoneyValue.format(string));
            } catch (NumberFormatException e) {
                expenseValueTextView.setText(MoneyValue.format(0));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void openDatePicker() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Selecione uma data"); //TODO: make a string resource
        builder.setSelection(selectedDate.getDateInMillis());
        MaterialDatePicker<Long> datePicker = builder.build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            selectedDate.setDate(selection);
            expenseDateEditText.setText(selectedDate.getFormattedDate());
        });

        datePicker.show(getParentFragmentManager(), datePicker.toString());
    }

    public void setInitialValues(int month, int year, String id) {
        expenseTypeButtonToggleGroup.check(R.id.singleTypeButton);
        expenseValueEditText.setText("0");
        expenseParcelEditText.setText("12");
        expenseIntervalInMonthsEditText.setText("1");
        if (id.isEmpty()) {
            addMode = true;
            materialToolbar.setTitle("Adicionar despesa");
            selectedDate = SimpleDate.today();
            if (month >= 0 && year >= 0 && (month != selectedDate.getMonth() || year != selectedDate.getYear())) {
                selectedDate.setMonth(month);
                selectedDate.setYear(year);
                selectedDate.setDay(1);
            }
            expenseDateEditText.setText(selectedDate.getFormattedDate());
        } else {
            addMode = false;
            materialToolbar.setTitle("Editar despesa");
            loadExpenseData(id);
        }
    }

    public void loadExpenseData(String id) {
        DatabaseReference occurrenceRef = FirebaseSettings.getOccurrencesReference()
                .child(String.valueOf(year))
                .child(String.valueOf(month))
                .child(id);
        occurrenceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                occurrence = snapshot.getValue(Occurrence.class);
                DatabaseReference occurrenceControllerRef = FirebaseSettings.getOccurrenceControllersReference()
                        .child(occurrence.getGroupId());
                occurrenceControllerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        occurrenceController = snapshot.getValue(OccurrenceController.class);
                        if (occurrenceController != null) {
                            if (occurrenceController.getMaxOccurrences() == 1) {
                                expenseTypeButtonToggleGroup.check(R.id.singleTypeButton);
                            } else if (occurrenceController.getMaxOccurrences() > 1) {
                                expenseTypeButtonToggleGroup.check(R.id.parceledTypeButton);
                                expenseParcelEditText.setText(String.valueOf(occurrenceController.getMaxOccurrences()));
                            } else if (occurrenceController.getMaxOccurrences() == -1) {
                                expenseTypeButtonToggleGroup.check(R.id.recurringTypeButton);
                                expenseParcelEditText.setText(String.valueOf(occurrence.getIndex()+1));
                            }
                            expenseIntervalInMonthsEditText.setText(String.valueOf(occurrenceController.getIntervaInlMonths()));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("AddEditViewModel", "Erro ao obter controlador de despesa", error.toException());
                    }
                });

                expenseValueEditText.setText(String.valueOf(occurrence.getValue()));
                expenseNameEditText.setText(occurrence.getName());
                expenseDescriptionEditText.setText(occurrence.getDescription());
                expenseDateEditText.setText(occurrence.getDate().getFormattedDate());
                selectedDate = occurrence.getDate().clone();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AddEditViewModel", "Erro ao obter despesa", error.toException());
            }
        });
    }

    private void saveExpense(View view) {
        // obtem os dados inseridos pelo usuário
        String name = expenseNameEditText.getText().toString();
        if (name.isEmpty()){
            Toast.makeText(getContext(), "Insira um nome para a despesa", Toast.LENGTH_SHORT).show();
            return;
        }
        Long value = 0L;
        if (!expenseValueEditText.getText().toString().isEmpty()) {
            value = Long.parseLong(expenseValueEditText.getText().toString());
        }
        SimpleDate date = selectedDate;
        String description = expenseDescriptionEditText.getText().toString();

        int checkedId = expenseTypeButtonToggleGroup.getCheckedButtonId();
        int maxOccurrences = 0;
        int intervalInMonths = 0;

        int singleTypeButtonId = R.id.singleTypeButton;
        int parceledTypeButtonId = R.id.parceledTypeButton;
        int recurringTypeButtonId = R.id.recurringTypeButton;
        if (checkedId == singleTypeButtonId) {
            maxOccurrences = 1;
            intervalInMonths = 1;
        } else if (checkedId == parceledTypeButtonId) {
            maxOccurrences = Integer.parseInt(expenseParcelEditText.getText().toString());
            intervalInMonths = Integer.parseInt(expenseIntervalInMonthsEditText.getText().toString());
        } else if (checkedId == recurringTypeButtonId) {
            maxOccurrences = -1;
            intervalInMonths = Integer.parseInt(expenseIntervalInMonthsEditText.getText().toString());
        } else {
            Toast.makeText(getContext(), "Selecione um tipo de despesa", Toast.LENGTH_SHORT).show();
            return;
        }

        if (addMode) {
            createNew(maxOccurrences, intervalInMonths, date, name, value, description, view);
        } else if (occurrence.getIndex()+1 == occurrenceController.getMaxOccurrences()) {
            editAllNext(maxOccurrences, intervalInMonths, date, name, value, description, view);
        } else {
            long finalValue = value;
            int finalMaxOccurrences = maxOccurrences;
            int finalIntervalInMonths = intervalInMonths;
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Editar despesa")
                    .setMessage("Deseja editar apenas esta despesa ou todas as seguintes?")
                    .setPositiveButton("Todas as seguintes", (dialog, which) -> editAllNext(finalMaxOccurrences, finalIntervalInMonths, date, name, finalValue, description, view))
                    .setNegativeButton("Apenas esta", (dialog, which) -> editOnlyThis(date, name, finalValue, description, view))
                    .show();
        }

    }

    private void createNew(int maxOccurrences, int intervalInMonths, SimpleDate date, String name, Long value, String description, View view) {
        OccurrenceController controller = new OccurrenceController();
        controller.setMaxOccurrences(maxOccurrences);
        controller.setIntervaInlMonths(intervalInMonths);
        controller.setLastEditDate(date);
        controller.setControllDate(date);
        controller.setName(name);
        controller.setValue(value);
        controller.setDescription(description);

        OccurrenceControllerService.save(controller);
        PagerViewModel pagerViewModel = new ViewModelProvider(requireActivity()).get(PagerViewModel.class);
        pagerViewModel.getTargetMonthYear().setValue(new MonthYear(selectedDate.getMonth(), selectedDate.getYear()));
        Navigation.findNavController(view).navigateUp();
    }

    private void editAllNext(int finalMaxOccurrences, int finalIntervalInMonths, SimpleDate date, String name, long finalValue, String description, View view) {
        occurrence.setName(name);
        occurrence.setValue(finalValue);
        occurrence.setDate(date);
        occurrence.setDescription(description);

        occurrenceController.setMaxOccurrences(finalMaxOccurrences);
        occurrenceController.setIntervaInlMonths(finalIntervalInMonths);
        occurrenceController.setLastEditDate(date);
        occurrenceController.setControllDate(date);
        occurrenceController.setLastEditIndex(occurrence.getIndex());
        occurrenceController.setControllIndex(occurrence.getIndex());
        occurrenceController.setName(name);
        occurrenceController.setValue(finalValue);
        occurrenceController.setDescription(description);

        String groupId = occurrenceController.getGroupId();
        // remove todas as despesas do grupo que possuem o index maior ou igual ao controller
        DatabaseReference occurrencesRef = FirebaseSettings.getOccurrencesReference();
        occurrencesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // percorre todos os anos
                for (DataSnapshot yearSnapshot : snapshot.getChildren()) {
                    // percorre todos os meses
                    for (DataSnapshot monthSnapshot : yearSnapshot.getChildren()) {
                        // percorre todas as despesas
                        for (DataSnapshot occurrenceSnapshot : monthSnapshot.getChildren()) {
                            if (Objects.equals(occurrenceSnapshot.child("groupId").getValue(), groupId)) {
                                Integer index = occurrenceSnapshot.child("index").getValue(Integer.class);
                                if (index != null){
                                    if (index >= occurrenceController.getControllIndex()) {
                                        // remove a despesa
                                        occurrenceSnapshot.getRef().removeValue();
                                    }
                                }
                            }
                        }
                    }
                }
                // atualiza o controller
                OccurrenceControllerService.update(occurrenceController);
                // volta para a tela de despesas
                PagerViewModel pagerViewModel = new ViewModelProvider(requireActivity()).get(PagerViewModel.class);
                pagerViewModel.getTargetMonthYear().setValue(new MonthYear(selectedDate.getMonth(), selectedDate.getYear()));
                Navigation.findNavController(view).navigateUp();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void editOnlyThis(SimpleDate date, String name, Long value, String description, View view) {
        occurrence.setName(name);
        occurrence.setValue(value);
        occurrence.setDate(date);
        occurrence.setDescription(description);
        OccurrenceService.update(occurrence);

        occurrenceController.setLastEditDate(date);
        occurrenceController.setLastEditIndex(occurrence.getIndex());
        OccurrenceControllerService.update(occurrenceController);

        PagerViewModel pagerViewModel = new ViewModelProvider(requireActivity()).get(PagerViewModel.class);
        pagerViewModel.getTargetMonthYear().setValue(new MonthYear(selectedDate.getMonth(), selectedDate.getYear()));
        Navigation.findNavController(view).navigateUp();
    }
}