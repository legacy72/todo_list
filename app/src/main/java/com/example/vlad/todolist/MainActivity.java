package com.example.vlad.todolist;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    LinearLayout linearLayout;
    LinearLayout llLine;
    ArrayList<EventClass> listOfEvents = new ArrayList<EventClass>();
    ArrayList<EventClass> filteredListOfEvents = new ArrayList<EventClass>();
    String countEvents = "0";
    Menu menu;
    Spinner spinner;
    Boolean sort = Boolean.FALSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UpdateCountEvents();
        readFromFile(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        spinner = (Spinner) menu.findItem(R.id.main_spinner).getActionView();
        ArrayAdapter<CharSequence> ada = ArrayAdapter.createFromResource(this,
                R.array.spinner_choices, R.layout.spinner_elem);
        ada.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(ada);
        spinner.setOnItemSelectedListener(this);
        this.menu = menu;
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_sort)
        {
            Log.d("logs", "sort dates");
            sort = !sort;
            PerformSort();
        }
        else if (item.getItemId() == R.id.btn_delete_all_completed)
        {
            ListIterator<EventClass> iter = listOfEvents.listIterator();

            while (iter.hasNext())
            {
                if (iter.next().checked) {
                    iter.remove();
                }
            }

            UpdateCountEvents();
            convertToJsonAndWriteToFile();
            readFromFile(MainActivity.this);

            onItemSelected(null, null, spinner.getSelectedItemPosition(),
                    spinner.getSelectedItemId());
        }
        return super.onOptionsItemSelected(item);
    }

    public void PerformSort()
    {
        MenuItem btn = menu.findItem(R.id.btn_sort);
        filteredListOfEvents = listOfEvents;
        if (!sort)
        {
            Collections.sort(filteredListOfEvents, new EventClass.IdComparator());
            linearLayout= findViewById(R.id.mainLinearLayout);
            linearLayout.removeAllViews();
            for (final EventClass e : filteredListOfEvents) {
                AddEvent(e);
            }
            btn.setIcon(android.R.drawable.checkbox_off_background);
            TextView tvData = findViewById(R.id.tv2);
            tvData.setText("Дата");
        }
        else
        {
            Collections.sort(filteredListOfEvents, new EventClass.DateComparator());
            linearLayout= findViewById(R.id.mainLinearLayout);
            linearLayout.removeAllViews();
            for (final EventClass e : filteredListOfEvents) {
                AddEvent(e);
            }
            btn.setIcon(android.R.drawable.checkbox_on_background);
            TextView tvData = findViewById(R.id.tv2);
            tvData.setText("Дата ▼");
        }
    }


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
            Boolean checked = Boolean.FALSE;

            EventClass e = FillListOfEvents(nameEvent, dateEvent, commentEvent, checked);
            AddEvent(e);

            convertToJsonAndWriteToFile();

        }
        else if (requestCode == 2){
            if (data == null){
                return;
            }

            //try catch костыль - переделать
            try {
                EventClass e = (EventClass) data.getSerializableExtra("event");
                EditChangedEvent(e);
            }
            catch (Exception e){

            }
            try {
                EventClass delE = (EventClass) data.getSerializableExtra("deleteEvent");

                llLine = linearLayout.findViewWithTag("ll" + delE.id);
                linearLayout.removeView(llLine);

                int indexDeletedEvent = getIndexOfEvent(delE.id, listOfEvents);

                listOfEvents.remove(indexDeletedEvent);
                UpdateCountEvents();
                convertToJsonAndWriteToFile();
            }
            catch (Exception e){

            }
        }


    }




    public void UpdateCountEvents(){
        TextView tvForCountEvents = (TextView) findViewById(R.id.countEvents);
        countEvents = String.valueOf(listOfEvents.size());

        tvForCountEvents.setText("Всего событий: " + countEvents);

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
        convertToJsonAndWriteToFile();
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
    public EventClass FillListOfEvents(String nameEvent, String dateEvent, String commentEvent, Boolean checked){
        EventClass e;
        if (listOfEvents.size() == 0) {
            e = new EventClass(1, nameEvent, dateEvent, commentEvent, checked);
            listOfEvents.add(e);
        }
        else{
            EventClass maxId = Collections.max(listOfEvents, new EventComp());
            e = new EventClass(maxId.id + 1, nameEvent, dateEvent, commentEvent, checked);
            listOfEvents.add(e);
        }
        return e;
    }

    //запускаем активити на редакт
    public void StartActivityForEditEvent(View v){

        for(EventClass e : listOfEvents) {
            if(e.id == v.getId() && !e.checked) {
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



    public void convertToJsonAndWriteToFile(){
        JSONObject ob=new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (EventClass eventClass : listOfEvents){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", eventClass.id);
                jsonObject.put("name", eventClass.name);
                jsonObject.put("date", eventClass.date);
                jsonObject.put("comment", eventClass.comment);
                jsonObject.put("checked", eventClass.checked);
            }
            catch (JSONException e){
                Log.e("logs", e.toString());
            }
            jsonArray.put(jsonObject);
        }

        try {
            ob.put("EVENTS", jsonArray);
        } catch (JSONException e){
                Log.e("logs", e.toString());
            }
        writeToFile(ob.toString(), MainActivity.this);
    }


    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("data.json", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(Context context) {
        linearLayout= findViewById(R.id.mainLinearLayout);
        linearLayout.removeAllViews();

        String ret = "";
        try {
            InputStream inputStream = context.openFileInput("data.json");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        Log.d("logs", ret);
        try{
            JSONArray evs = new JSONObject(ret).getJSONArray("EVENTS");
            listOfEvents.clear();
            for (int i = 0; i < evs.length(); i++) {
                EventClass e = FillListOfEvents(
                        ((JSONObject)evs.get(i)).getString("name"),
                        ((JSONObject)evs.get(i)).getString("date"),
                        ((JSONObject)evs.get(i)).getString("comment"),
                        ((JSONObject)evs.get(i)).getBoolean("checked")
                );
                AddEvent(e);
            }
        }
        catch (JSONException ex){
            Log.d("logs", ex.toString());
        }

        return ret;
    }


    public void changeCheckedEvent(EventClass e, Boolean isChecked){
        int eventId = e.id;
        int index = getIndexOfEvent(eventId, listOfEvents);
        e.checked = isChecked;
        listOfEvents.set(index , e);
        convertToJsonAndWriteToFile();
    }

    //лоханская верстка, лучше себя пожалеть и не смотреть код
    public void AddEvent(final EventClass e){
        linearLayout = findViewById(R.id.mainLinearLayout);

        //лайаут для всего списка
        llLine = new LinearLayout(this);
        llLine.setId(e.id);
        llLine.setTag("ll" + e.id);
        llLine.setOrientation(LinearLayout.HORIZONTAL);

        llLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        final CheckBox checkbox = new CheckBox(this);
        checkbox.setId(e.id);
        checkbox.setTag("cb" + e.id);
        checkbox.setLayoutParams(lpForCheckBox);
        checkbox.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        if (e.checked) {
            checkbox.setChecked(true);
        }
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                changeCheckedEvent(e, isChecked);
            }
        });

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

    public void ShowCompletedEvents(String sort){
        linearLayout= findViewById(R.id.mainLinearLayout);
        linearLayout.removeAllViews();

        for (final EventClass e : listOfEvents) {
            if (sort.equals("all")) {
                AddEvent(e);
            }
            else if (sort.equals("solved")) {
                if (e.checked) {
                    AddEvent(e);
                }
            }
            else if (sort.equals("notSolved")) {
                if (!e.checked) {
                    AddEvent(e);
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("log1", "selected spinner pos " + position);
        switch (position)
        {
            case 0:
                ShowCompletedEvents("all");
                Log.d("logs", "sort by all");
                break;
            case 1:
                ShowCompletedEvents("solved");
                Log.d("logs", "sort by solved");
                break;
            case 2:
                ShowCompletedEvents("notSolved");
                Log.d("logs", "sort by not solved");
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
