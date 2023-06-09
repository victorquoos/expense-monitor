package com.ifsc.expensemonitor.ui.addedit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.database.Occurrence;
import com.ifsc.expensemonitor.database.FirebaseSettings;
import com.ifsc.expensemonitor.database.MoneyValue;
import com.ifsc.expensemonitor.database.OccurrenceController;
import com.ifsc.expensemonitor.database.OccurrenceControllerService;
import com.ifsc.expensemonitor.database.SimpleDate;

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

    public void setInitialValues(int month, int year, String key) {
        selectedDate = SimpleDate.today();
        expenseValueEditText.setText("0");
        if (key.isEmpty()) {
            addMode = true;
            materialToolbar.setTitle("Adicionar despesa");
            expenseTypeButtonToggleGroup.check(R.id.singleTypeButton);
            expenseParcelEditText.setText("12");
            expenseIntervalInMonthsEditText.setText("1");
            if (month >= 0 && year >= 0 && (month != selectedDate.getMonth() || year != selectedDate.getYear())) {
                selectedDate.setMonth(month);
                selectedDate.setYear(year);
                selectedDate.setDay(1);
            }
            expenseDateEditText.setText(selectedDate.getFormattedDate());
        } else {
            addMode = false;
            materialToolbar.setTitle("Editar despesa");
            loadExpenseData(key);
        }
    }

    public void loadExpenseData(String key) {
        DatabaseReference occurrenceRef = FirebaseSettings.getOccurrencesReference()
                .child(String.valueOf(year))
                .child(String.valueOf(month))
                .child(key);
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
                selectedDate = occurrence.getDate();
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
            OccurrenceController controller = new OccurrenceController();
            controller.setMaxOccurrences(maxOccurrences);
            controller.setIntervaInlMonths(intervalInMonths);
            controller.setLastEditDate(date);
            controller.setControllDate(date);
            controller.setName(name);
            controller.setValue(value);
            controller.setDescription(description);

            OccurrenceControllerService.save(controller);
            Navigation.findNavController(view).navigateUp();
        }




        /*
        long value = 0L;
        if (!expenseValueEditText.getText().toString().isEmpty()) {
            value = Long.parseLong(expenseValueEditText.getText().toString());
        }
        String name = expenseNameEditText.getText().toString();
        String description = expenseDescriptionEditText.getText().toString();
        SimpleDate date = selectedDate;

        if (name.isEmpty()) {
            expenseNameEditText.setError("Insira um nome válido");
            expenseNameEditText.requestFocus();
        } else if (date == null) {
            expenseDateEditText.setError("Insira uma data válida");
            expenseDateEditText.requestFocus();
        } else {
            Occurrence occurrence = new Occurrence(name, value, date, description);
            if (key.isEmpty()) {
                occurrence.save();
            } else {
                occurrence.setKey(key);
                occurrence.update();
            }
            PagerViewModel pagerViewModel = new ViewModelProvider(requireActivity()).get(PagerViewModel.class);
            pagerViewModel.getTargetMonthYear().setValue(new MonthYear(selectedDate.getMonth(), selectedDate.getYear()));
            Navigation.findNavController(view).navigateUp();
        }
        */
    }
}