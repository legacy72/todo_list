package com.example.vlad.todolist;

import java.io.Serializable;

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
}
