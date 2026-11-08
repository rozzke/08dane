package com.example.zadanie08;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity2 extends AppCompatActivity {


    public static final String EXTRA_NAME = "com.example.dwaekrany.NAME";
    public static final String EXTRA_EMAIL = "com.example.dwaekrany.EMAIL";
    public static final String EXTRA_COURSE = "com.example.dwaekrany.COURSE";
    public static final String EXTRA_LEVEL = "com.example.dwaekrany.LEVEL";
    public static final String EXTRA_NEWSLETTER = "com.example.dwaekrany.NEWSLETTER";
    public static final String EXTRA_NUMBER = "com.example.dwaekrany.NUMBER";

    private TextView tvSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        tvSummary = findViewById(R.id.tvSummary);
        tvSummary.setText(buildSummary());

        findViewById(R.id.btnConfirm).setOnClickListener(v -> confirm());
        findViewById(R.id.btnEdit).setOnClickListener(v -> finish());
        findViewById(R.id.btnShare).setOnClickListener(v -> share());
    }

    private String buildSummary() {
        Intent intent = getIntent();

        String name = intent.getStringExtra(EXTRA_NAME);
        String email = intent.getStringExtra(EXTRA_EMAIL);
        String course = intent.getStringExtra(EXTRA_COURSE);
        int level = intent.getIntExtra(EXTRA_LEVEL, 0);
        boolean newsletter = intent.getBooleanExtra(EXTRA_NEWSLETTER, false);

        String newsletterText = newsletter
                ? getString(R.string.yes_short)
                : getString(R.string.no_short);

        return getString(R.string.summary_format, name, email, course, level, newsletterText);
    }

    private void confirm() {
        String number = "ZG/" + (1000 + new Random().nextInt(9000));

        Intent result = new Intent();
        result.putExtra(EXTRA_NUMBER, number);
        setResult(RESULT_OK, result);
        finish();                      // dopiero finish odsyła wynik
    }

    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, tvSummary.getText().toString());

        try {
            startActivity(Intent.createChooser(intent, getString(R.string.share_via)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.error_no_app, Toast.LENGTH_SHORT).show();
        }
    }
}