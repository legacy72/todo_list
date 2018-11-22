package com.example.vlad.todolist;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class AddEvent extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        setTitle("Добавление события");

        ImageButton btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDate = DateFormat.getDateInstance().format(c.getTime());
        TextView tv = findViewById(R.id.textView);
        tv.setText(currentDate);
    }

    public void onClickSaveEvent(View v){
        EditText textInputName = findViewById(R.id.textInputName);
        EditText textInputComment = findViewById(R.id.textInputComment);
        TextView eventDate = findViewById(R.id.textView);




        Intent intent = new Intent();
        intent.putExtra("textInputName", textInputName.getText().toString());
        intent.putExtra("eventDate", eventDate.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
