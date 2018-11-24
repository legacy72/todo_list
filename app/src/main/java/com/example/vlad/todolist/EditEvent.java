package com.example.vlad.todolist;

import android.app.DatePickerDialog;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditEvent extends AppCompatActivity {
    EventClass e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        setTitle("Редактирование события");

        setEventParams();

        ImageButton btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                showDatePickerDialog(e.date);
            }
        });
    }


    private void showDatePickerDialog(String date) {
        int day = 1;
        int month = 1;
        int year = 2018;
        try {
            String[] split = date.split(" ");
            day = Integer.valueOf(split[0]);
            String monthStr = split[1];
            year = Integer.valueOf(split[2]);

            month = ConvertMonth(monthStr) - 1; // костыль чтобы сместить месяц т.к. у них с 0
        }
        catch (Exception e){
            //типо экзепшн
        }
        Log.d("logs", String.valueOf(month));

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Log.d("logs", "dmmm: " + String.valueOf(Calendar.MONTH));

                String currentDate = DateFormat.getDateInstance().format(c.getTime());
                TextView tv = findViewById(R.id.dateEvent);
                tv.setText(currentDate);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                dateSetListener, year, month, day);
        datePickerDialog.show();
    }

    //велосипед, но лень искать, т.к. на первых 3 ссылках не было ответа
    public int ConvertMonth(String month){
        int m = 0;
        switch (month){
            case "янв.":
                m = 1;
                break;
            case "февр.":
                m = 2;
                break;
            case "мар.":
                m = 3;
                break;
            case "апр.":
                m = 4;
                break;
            case "мая":
                m = 5;
                break;
            case "июн.":
                m = 6;
                break;
            case "июл.":
                m = 7;
                break;
            case "авг.":
                m = 8;
                break;
            case "сент.":
                m = 9;
                break;
            case "окт.":
                m = 10;
                break;
            case "нояб.":
                m = 11;
                break;
            case "дек.":
                m = 12;
                break;
            default:
                break;
        }
        return m;
    }

    public void setEventParams(){
        Intent intent = getIntent();
        e = (EventClass) intent.getSerializableExtra("event");

        EditText etName = findViewById(R.id.nameEvent);
        etName.setText(e.name);

        EditText etComment = findViewById(R.id.commentEvent);
        etComment.setText(e.comment);

        TextView tvDate = findViewById(R.id.dateEvent);
        tvDate.setText(e.date);



    }

    public void acceptChanges(){
        EditText etName = findViewById(R.id.nameEvent);
        EditText etComment = findViewById(R.id.commentEvent);
        TextView tvDate = findViewById(R.id.dateEvent);

        e.name = etName.getText().toString();
        e.comment = etComment.getText().toString();
        e.date = tvDate.getText().toString();

    }

    public void backToMainActivity(View v){
        acceptChanges();

        Intent intent = new Intent();
        intent.putExtra("event", e);
        setResult(RESULT_OK, intent);
        finish();
    }

}
