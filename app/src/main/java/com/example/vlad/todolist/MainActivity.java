package com.example.vlad.todolist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
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

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    LinearLayout llLine;
    ArrayList<EventClass> listOfEvents = new ArrayList<EventClass>();
    String countEvents = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UpdateCountEvents();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//
//
//        switch (id) {
//            case R.id.action_all:
//                infoTextView.setText("Все");
//                return true;
//            case R.id.action_complete:
//                infoTextView.setText("Решенные");
//                return true;
//            case R.id.action_not_complete:
//                infoTextView.setText("Нерешенные");
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }


    public void onClickAddEvent(View v) {
        Intent intent = new Intent(this, AddEvent.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            if (data == null) {
                return;
            }
            String nameEvent = data.getStringExtra("textInputName");
            String dateEvent = data.getStringExtra("eventDate");
            String commentEvent = data.getStringExtra("textInputComment");

            AddEvent(nameEvent, dateEvent, commentEvent);
        }
        else if (requestCode == 2){
            if (data == null){
                return;
            }
            EventClass e = (EventClass) data.getSerializableExtra("event");


            EditChangedEvent(e);
        }


    }


    public void UpdateCountEvents(){
        TextView tvForCountEvents = (TextView) findViewById(R.id.countEvents);
        countEvents = String.valueOf(listOfEvents.size());

        tvForCountEvents.setText("Кол-во событий: " + countEvents);

    }

    //редачим выбранное событие
    public void EditChangedEvent(EventClass e){
        int eventId = e.id;

        TextView tvNameEvent = linearLayout.findViewWithTag("name" + eventId);
        TextView tvDateEvent = linearLayout.findViewWithTag("date" + eventId);

        tvNameEvent.setText(e.name);
        tvDateEvent.setText(e.date);

        int index = getIndexOfEvent(eventId, listOfEvents);
        listOfEvents.set(index , e);
    }


    public int getIndexOfEvent(int id, List<EventClass> listOfEvents)
    {
        Iterator<EventClass> iterator = listOfEvents.iterator();
        int index = 0;
        while (iterator.hasNext())
        {
            EventClass e = iterator.next();
            if (e.id == id)
            {
                return index;
            }
            index++;
        }
        return 0;
    }


    //заполняем лист объектов событий
    public EventClass FillListOfEvents(String nameEvent, String dateEvent, String commentEvent){
        EventClass e;
        if (listOfEvents.size() == 0) {
            e = new EventClass(1, nameEvent, dateEvent, commentEvent, false);
            listOfEvents.add(e);
        }
        else{
            EventClass maxId = Collections.max(listOfEvents, new EventComp());
            e = new EventClass(maxId.id + 1, nameEvent, dateEvent, commentEvent, false);
            listOfEvents.add(e);
        }
        return e;
    }

    //запускаем активити на редакт
    public void StartActivityForEditEvent(View v){

        for(EventClass e : listOfEvents) {
            if(e.id == v.getId()) {
                Intent intent = new Intent(this, EditEvent.class);
                intent.putExtra("event", e);
                startActivityForResult(intent, 2);
            }
        }

    }

    // конверт пикселей в dp
    public static float convertPixelsToDp(float px, Context context){
        return px / context.getResources().getDisplayMetrics().density;
    }

    //лоханская верстка, лучше себя пожалеть и не смотреть код
    public void AddEvent(final String nameEvent, String dateEvent, String commentEvent){

        // создаем объект события
        EventClass e = FillListOfEvents(nameEvent, dateEvent, commentEvent);


        linearLayout = findViewById(R.id.mainLinearLayout);

        //лайаут для всего списка
        llLine = new LinearLayout(this);
        llLine.setId(e.id);
        llLine.setTag("ll" + e.id);
        llLine.setOrientation(LinearLayout.HORIZONTAL);

        llLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast toast = Toast.makeText(getApplicationContext(), String.valueOf(v.getId()), Toast.LENGTH_SHORT);
                // toast.show();
                StartActivityForEditEvent(v);
            }
        });


        LinearLayout.LayoutParams lpForLinearLayoutLine = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lpForLinearLayoutLine.setMargins(10, 10, 10, 0);

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
        checkbox.setTag("cb" + e.id);
        checkbox.setLayoutParams(lpForCheckBox);
        checkbox.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);

        //текствью для названия события
        LinearLayout.LayoutParams lpForTvName = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lpForTvName.weight = 6;
        float margin = 10;
        lpForTvName.setMargins(0, (int)convertPixelsToDp(margin, MainActivity.this), 0, 0);

        TextView tvName = new TextView(this);
        tvName.setTag("name" + e.id);
        tvName.setLayoutParams(lpForTvName);
        tvName.setText(e.name);
        tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);

        //текствью для даты
        LinearLayout.LayoutParams lpForTvDate = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lpForTvDate.weight = 4;
        lpForTvDate.setMargins(0, (int)convertPixelsToDp(margin, MainActivity.this), 0, 0);

        TextView tvDate = new TextView(this);
        tvDate.setId(e.id);
        tvDate.setTag("date" + e.id);
        tvDate.setLayoutParams(lpForTvDate);
        tvDate.setText(e.date);
        tvDate.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);


        llEvent.addView(tvName);
        llEvent.addView(tvDate);


        llLine.addView(checkbox);
        llLine.addView(llEvent);


        linearLayout.addView(llLine);

        UpdateCountEvents();
    }

}
