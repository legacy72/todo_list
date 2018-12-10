package com.example.vlad.todolist;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Comparator;

public class EventClass implements Serializable {
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

    static Integer[] ConvertDateFromStringToArray(String date){
        String[] split = date.split(" ");

        int day = Integer.valueOf(split[0]);
        String monthStr = split[1];
        int year = Integer.valueOf(split[2]);

        int month = ConvertMonth(monthStr) - 1; // сместить месяц т.к. у них с 0

        Integer[] dateArray = new Integer[3];
        dateArray[0] = year;
        dateArray[1] = month;
        dateArray[2] = day;

        return dateArray;
    }

    static int ConvertMonth(String month){
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

    static class IdComparator implements Comparator<EventClass>
    {
        @Override
        public int compare(EventClass e1, EventClass e2) {
            return e1.id - e2.id;
        }
    }

    static class DateComparator implements Comparator<EventClass>
    {
        @Override
        public int compare(EventClass e1, EventClass e2) {
            Integer[] date = ConvertDateFromStringToArray(e1.date);
            Calendar c1 = Calendar.getInstance();
            c1.set(date[0], date[1] - 1, date[2]);

            date = ConvertDateFromStringToArray(e2.date);
            Calendar c2 = Calendar.getInstance();
            c2.set(date[0], date[1] - 1, date[2]);

            return -c1.compareTo(c2);
        }
    }
}
