package com.example.rent_it;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

public class UploadNewProduct extends AppCompatActivity {

    String selectedDates;
    Button uploadImage, buttonDatePicker,buttonDatePicker2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_new_product);

        uploadImage = findViewById(R.id.btnUploadImage);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UploadNewProduct.this, UploadImage.class));
            }
        });

        //choose dates functionality
        buttonDatePicker = findViewById(R.id.buttonDatePicker);
        buttonDatePicker2 = findViewById(R.id.buttonDatePicker2);
        buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(buttonDatePicker);
            }
        });
        buttonDatePicker2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(buttonDatePicker2);
            }
        });
    }

    private void showDatePickerDialog(Button buttonDatePickerx) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Handle the selected date
                selectedDates = dayOfMonth + "/" + (month + 1) + "/" + year;
                //buttonDatePicker.setText(selectedDates);
                buttonDatePickerx.setText(selectedDates);
            }
        };
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        // Create and show the DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                UploadNewProduct.this,
                dateSetListener,
                year, month, day
        );
        datePickerDialog.show();
    }
}