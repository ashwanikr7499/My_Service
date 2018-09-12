package com.example.win10.my_service;

import java.util.Date;

public class ReminderRecord
{
    Date time;
    String text;
    public ReminderRecord(Date t, String text)
    {
        time = t;
        this.text = text;
    }
}
