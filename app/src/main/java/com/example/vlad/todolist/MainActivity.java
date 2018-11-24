package com.example.vlad.todolist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EventListener;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    LinearLayout llLine;
    LinearLayout[] massOfLayouts;
    List<LinearLayout> listOfLayouts = new ArrayList<>();
    ArrayList<EventClass> listOfEvents = new ArrayList<EventClass>();

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


    }




    public void AddEvent(final String nameEvent, String dateEvent){
        // создаем объект события
        EventClass e = FillListOfEvents(nameEvent, dateEvent);




        linearLayout = findViewById(R.id.mainLinearLayout);

        //лайаут для всего списка
        llLine = new LinearLayout(this);
        llLine.setId(e.id);
        llLine.setOrientation(LinearLayout.HORIZONTAL);

        llLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), String.valueOf(v.getId()), Toast.LENGTH_SHORT);
                toast.show();
            }
        });


        LinearLayout.LayoutParams lpForLinearLayoutLine = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        llLine.setLayoutParams(lpForLinearLayoutLine);

        //лайаут для строки без чекбокса
        LinearLayout llEvent = new LinearLayout(this);
        llEvent.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        llEvent.setLayoutParams(lp);



        //параметры лэйаута для чекбокса
        LinearLayout.LayoutParams lpForCheckBox = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        //чекбокс
        CheckBox checkbox = new CheckBox(this);
        checkbox.setId(e.id);
        checkbox.setLayoutParams(lpForCheckBox);

        //текствью для названия события
        LinearLayout.LayoutParams lpForTvName = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lpForTvName.weight = 6;
        lpForTvName.setMargins(0, 5, 0, 0);

        TextView tvName = new TextView(this);
        tvName.setId(e.id);
        tvName.setLayoutParams(lpForTvName);
        tvName.setText(e.name);
        tvName.setTextSize(14);

        //текствью для даты
        LinearLayout.LayoutParams lpForTvDate = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lpForTvDate.weight = 4;
        lpForTvDate.setMargins(0, 5, 0, 0);

        TextView tvDate = new TextView(this);
        tvDate.setId(e.id);
        tvDate.setLayoutParams(lpForTvDate);
        tvDate.setText(e.date);
        tvDate.setTextSize(14);


        llEvent.addView(tvName);
        llEvent.addView(tvDate);


        llLine.addView(checkbox);
        llLine.addView(llEvent);


        linearLayout.addView(llLine);


        // добавить в список строк
        listOfLayouts.add(llLine);


        
       


    }

    //заполняем лист объектов событий
    public EventClass FillListOfEvents(String nameEvent, String dateEvent){
        EventClass e;
        if (listOfEvents.size() == 0) {
            e = new EventClass(1, nameEvent, dateEvent, "", false);
            listOfEvents.add(e);
        }
        else{
            EventClass maxId = Collections.max(listOfEvents, new EventComp());
            e = new EventClass(maxId.id + 1, nameEvent, dateEvent, "", false);
            listOfEvents.add(e);
        }
        return e;
    }

    class EventComp implements Comparator<EventClass>{
        @Override
        public int compare(EventClass e1, EventClass e2) {
            return e1.id > e2.id ? 1 : e1.id == e2.id ? 0 : -1;
        }
    }

    class EventClass {

        int id;
        String name;
        String date;
        String comment;
        boolean checked;


        EventClass(int _id, String _name, String _date, String _comment, boolean _checked) {
            id = _id;
            name = _name;
            date = _date;
            comment = _comment;
            checked = _checked;
        }

    }
}
