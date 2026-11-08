package com.example.zadanie08;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerCategory;
    private EditText editDescription;
    private RadioGroup radioPriority;
    private CheckBox checkUrgent;
    private Button buttonNext;

    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        editDescription = findViewById(R.id.editDescription);
        radioPriority = findViewById(R.id.radioPriority);
        checkUrgent = findViewById(R.id.checkUrgent);
        buttonNext = findViewById(R.id.buttonNext);

        // Kategorie usterek
        String[] categories = {
                "Wybierz kategorię",
                "Sprzęt komputerowy",
                "Oprogramowanie",
                "Sieć internetowa",
                "Inne"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerCategory.setAdapter(adapter);

        // Odbieranie wyniku z drugiego ekranu
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == RESULT_OK) {

                        Intent data = result.getData();

                        if (data != null) {
                            String number = data.getStringExtra(
                                    SummaryActivity.EXTRA_RESULT_NUMBER
                            );

                            Toast.makeText(
                                    this,
                                    "Zgłoszenie potwierdzone! Numer: " + number,
                                    Toast.LENGTH_LONG
                            ).show();

                            // Czyszczenie formularza
                            editDescription.setText("");
                            spinnerCategory.setSelection(0);
                            radioPriority.clearCheck();
                            checkUrgent.setChecked(false);
                        }

                    } else if (result.getResultCode() == RESULT_CANCELED) {

                        Toast.makeText(
                                this,
                                "Zrezygnowano ze zgłoszenia",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );

        buttonNext.setOnClickListener(v -> openSummary());
    }

    private void openSummary() {

        // Walidacja kategorii
        if (spinnerCategory.getSelectedItemPosition() == 0) {
            Toast.makeText(this,
                    "Wybierz kategorię usterki",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Walidacja opisu
        String description = editDescription.getText().toString().trim();

        if (description.length() < 10) {
            editDescription.setError(
                    "Opis musi mieć minimum 10 znaków"
            );
            return;
        }

        // Walidacja priorytetu
        if (radioPriority.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this,
                    "Wybierz priorytet",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String category = spinnerCategory.getSelectedItem().toString();

        int priority;

        int selectedId = radioPriority.getCheckedRadioButtonId();

        if (selectedId == R.id.radioLow) {
            priority = 1;
        } else if (selectedId == R.id.radioMedium) {
            priority = 2;
        } else {
            priority = 3;
        }

        boolean urgent = checkUrgent.isChecked();

        // Przekazywanie danych do drugiego ekranu
        Intent intent = new Intent(
                MainActivity.this,
                SummaryActivity.class
        );

        intent.putExtra(SummaryActivity.EXTRA_CATEGORY, category);
        intent.putExtra(SummaryActivity.EXTRA_DESCRIPTION, description);
        intent.putExtra(SummaryActivity.EXTRA_PRIORITY, priority);
        intent.putExtra(SummaryActivity.EXTRA_URGENT, urgent);
        intent.putExtra(SummaryActivity.EXTRA_DATE,
                System.currentTimeMillis());

        launcher.launch(intent);
    }
}

