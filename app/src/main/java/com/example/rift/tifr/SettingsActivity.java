package com.example.rift.tifr;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;
import static com.example.rift.tifr.Event.EventsFragment.FROM_PREF;
import static com.example.rift.tifr.Event.EventsFragment.TO_PREF;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    public Button today,tomorrow, this_week,this_month,select_date,ok,button;
    private int mYear, mMonth, mDay;

    private String date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        this.setFinishOnTouchOutside(false);

        today = (Button) findViewById(R.id.today);
        today.setOnClickListener(this);
        tomorrow = (Button) findViewById(R.id.tomorrow);
        tomorrow.setOnClickListener(this);
        this_week = (Button) findViewById(R.id.this_week);
        this_week.setOnClickListener(this);
        this_month = (Button) findViewById(R.id.this_month);
        this_month.setOnClickListener(this);
        select_date = (Button) findViewById(R.id.select_date);
        select_date.setOnClickListener(this);
        ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        final SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
        switch(v.getId()){
            case R.id.today: {
                SharedPreferences.Editor editor = preference.edit();
                editor.putString(FROM_PREF, "today");
                editor.putString(TO_PREF, "today");
                editor.apply();
                this.finish();
            }
                break;
            case R.id.tomorrow: {
                SharedPreferences.Editor editor = preference.edit();
                editor.putString(FROM_PREF, "tomorrow");
                editor.putString(TO_PREF, "tomorrow");
                editor.apply();
                this.finish();
            }
            break;
            case R.id.this_week: {
                SharedPreferences.Editor editor = preference.edit();
                editor.putString(FROM_PREF, "today");
                editor.putString(TO_PREF, "7days");
                editor.apply();
                this.finish();
            }
            break;
            case R.id.this_month: {
                SharedPreferences.Editor editor = preference.edit();
                editor.putString(FROM_PREF, "today");
                editor.putString(TO_PREF, "31days");
                editor.apply();
                this.finish();
            }
            break;
            case R.id.select_date: {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                date = year+"-"+(monthOfYear + 1)+"-"+dayOfMonth ;
                                SharedPreferences.Editor editor = preference.edit();
                                editor.putString(FROM_PREF, date);
                                editor.putString(TO_PREF, date);
                                editor.apply();
                                finish();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
            break;
            case R.id.ok: {
                Intent resultIntent = new Intent();
                setResult(0, resultIntent);
                this.finish();
            }
            break;
        }
    }

}

