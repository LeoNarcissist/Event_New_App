package com.example.rift.tifr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rift.tifr.Event.Event;
import com.example.rift.tifr.Event.ExpandableTextView;
import com.example.rift.tifr.data.DatabaseHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class EventDetailActivity extends AppCompatActivity {

    TextView titlePrimaryView;
    TextView dateView;
    TextView timeView;
    Button buttonView;
    TextView roomView;
    ImageView imageTextView;
    DatabaseHelper mDatabaseHelper;
    View noDescriptionView;
    private AlertDialog.Builder build;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final Event currentEvent = (Event) intent.getSerializableExtra("eventObject");


        setTitle(currentEvent.getTitle());

        String title_primary = currentEvent.getTitle();

        String room = currentEvent.getRoom();


        titlePrimaryView = (TextView) findViewById(R.id.title_primary);
        titlePrimaryView.setText(title_primary);

        imageTextView = (ImageView) findViewById(R.id.tifr_pic);
        imageTextView.setContentDescription(title_primary);
        Glide.with(this)
                .load(currentEvent.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.tifr)
                .into( imageTextView);


        roomView = (TextView) findViewById(R.id.room);
        roomView.setText(room);

        String date=parseDate(currentEvent.getDate())+"\n"+parseTime(currentEvent.getTime())+"\n"+"to"+"\n"+
                parseDate(currentEvent.geteDate())+"\n"+parseTime(currentEvent.geteTime());
        dateView = (TextView) findViewById(R.id.date_time);
        dateView.setText(date);


        noDescriptionView = findViewById(R.id.description_layout);
        String description = currentEvent.getDescription();
        ExpandableTextView expandableTextView = (ExpandableTextView) findViewById(R.id.description);
        if (description != null && !description.isEmpty() && !description.equalsIgnoreCase("null")) {
            noDescriptionView.setVisibility(View.VISIBLE);
            expandableTextView.setText(description);
        }
        else
            noDescriptionView.setVisibility(View.GONE);



        buttonView = (Button) findViewById(R.id.button);
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(currentEvent.getUrl());
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(uri);
                startActivity(i);

            }
        });

        buttonView = (Button) findViewById(R.id.mailButton);
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("message/rfc822");
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Permission to attend the event.");
                intent.putExtra(Intent.EXTRA_TEXT, "Sir/Ma'am,\nI would like to attend the event(" + currentEvent.getTitle() +
                        ").(Enter details about your qualifications)\nThank You.");//message is your details
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                try {
                    startActivity(Intent.createChooser(intent, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        buttonView = (Button) findViewById(R.id.calendarButton);
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Date=currentEvent.getDate()+currentEvent.getTime();
                String eDate=currentEvent.geteDate()+currentEvent.geteTime();

                Calendar beginTime = new GregorianCalendar();
                SimpleDateFormat timer = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss", Locale.ENGLISH);
                try {
                    beginTime.setTime(timer.parse(Date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar endTime = new GregorianCalendar();
                SimpleDateFormat etimer = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss", Locale.ENGLISH);
                try {
                    endTime.setTime(etimer.parse(eDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.Events.TITLE, currentEvent.getTitle())
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,beginTime.getTimeInMillis())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        });


        mDatabaseHelper = new DatabaseHelper(this);

        buttonView = (Button) findViewById(R.id.add_button);

        buttonView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                insertNotification(currentEvent.getId(), currentEvent.getTitle(), currentEvent.getDate(),
                        currentEvent.getTime(), currentEvent.getDescription(), currentEvent.getRoom(),
                        currentEvent.getUrl(),currentEvent.getImage(),currentEvent.geteDate(),
                        currentEvent.geteTime());
            }
        });
    }

    public void insertNotification(String id, String title, String date, String time, String description,
                                   String room, String url, String image, String edate, String etime) {
        int insertData = mDatabaseHelper.insertNotification(id, title, date, time, description,
                room, url, image, edate, etime);

        if (insertData == 1) {
            toastMessage("Event added");
        } else if (insertData == 2) {
            toastMessage("List has reached it's maximum limit. Delete some events.");
        } else if (insertData == 0) {
            toastMessage("Already added");
        }
    }


    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }



    //  private ShareActionProvider mShareActionProvider;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = getIntent();
        final Event currentEventUne = (Event) intent.getSerializableExtra("eventObject");
        switch (item.getItemId()) {
            case R.id.mShare:
                Intent i = new Intent(
                        android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(
                        android.content.Intent.EXTRA_TEXT, currentEventUne.getUrl()+"\n"+currentEventUne.getTitle()+"\nLet's go for this event.");
                startActivity(Intent.createChooser(i, "Share Via"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public String parseDate(String date) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "EEE, MMM d, yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date formatDate;
        String str= null;

        try {
            formatDate = inputFormat.parse(date);
            str = outputFormat.format(formatDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String parseTime(String time) {
        String inputPattern = "HH:mm:ss";
        String outputPattern = "h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date formatTime;
        String str=null;

        try {
            formatTime = inputFormat.parse(time);
            str = outputFormat.format(formatTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
