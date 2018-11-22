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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //addEvent();
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
