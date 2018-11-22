package com.example.vlad.todolist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        TextView infoTextView = (TextView) findViewById(R.id.tv1);

        switch (id) {
            case R.id.action_all:
                infoTextView.setText("Все");
                return true;
            case R.id.action_complete:
                infoTextView.setText("Решенные");
                return true;
            case R.id.action_not_complete:
                infoTextView.setText("Нерешенные");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickEditEvent(View v) {
        Intent intent = new Intent(this, EditEvent.class);
        startActivityForResult(intent, 1);
    }


    public void onClickAddEvent(View v) {
        Intent intent = new Intent(this, AddEvent.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return ;
        }
        String nameEvent = data.getStringExtra("textInputName");
        String dateEvent = data.getStringExtra("eventDate");

        AddEvent(nameEvent, dateEvent);

        Toast toast = Toast.makeText(getApplicationContext(), nameEvent, Toast.LENGTH_SHORT);
        toast.show();

    }




    public void AddEvent(String nameEvent, String dateEvent){
        linearLayout = findViewById(R.id.mainLinearLayout);

        //лайаут для всего списка
        LinearLayout llLine = new LinearLayout(this);
        llLine.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams lpForLinearLayoutLine = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        llLine.setLayoutParams(lpForLinearLayoutLine);

        //лайаут для строки без чекбокса
        LinearLayout llEvent = new LinearLayout(this);
        llEvent.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        llEvent.setLayoutParams(lp);

        //чекбокс
        CheckBox checkbox = new CheckBox(this);
        checkbox.setLayoutParams(lp);

        //текствью для названия события
        LinearLayout.LayoutParams lpForTvName = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 4f);
        lpForTvName.weight = 4;
        lpForTvName.setMargins(0, 50, 0, 0);

        TextView tvName = new TextView(this);
        tvName.setLayoutParams(lpForTvName);
        tvName.setText(nameEvent);
        tvName.setTextSize(14);

        //текствью для даты
        LinearLayout.LayoutParams lpForTvDate = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        lpForTvDate.weight = 1;
        lpForTvDate.setMargins(0, 5, 0, 0);

        TextView tvDate = new TextView(this);
        tvDate.setLayoutParams(lpForTvDate);
        tvDate.setText(dateEvent);
        tvDate.setTextSize(14);


        llEvent.addView(tvName);
        llEvent.addView(tvDate);


        llLine.addView(checkbox);
        llLine.addView(llEvent);


        linearLayout.addView(llLine);
    }


    public class Event {

        String name;
        int price;
        boolean box;


        Event(String _describe, int _price, boolean _box) {
            name = _describe;
            price = _price;
            box = _box;
        }
    }
}
