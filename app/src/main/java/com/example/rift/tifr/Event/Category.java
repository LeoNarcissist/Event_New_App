package com.example.rift.tifr.Event;

import java.util.ArrayList;


public class Category {



    private String title;
    private ArrayList<Event> event;


    public Category() {

    }
    public Category(String title, ArrayList<Event> event) {
        this.title = title;
        this.event = event;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Event> getEvent() {
        return event;
    }

    public void setEvent(ArrayList<Event> event) {
        this.event = event;
    }


}
