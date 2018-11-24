package com.example.vlad.todolist;

import java.util.Comparator;

public class EventComp implements Comparator<EventClass> {
    @Override
    public int compare(EventClass e1, EventClass e2) {
        return e1.id > e2.id ? 1 : e1.id == e2.id ? 0 : -1;
    }
}