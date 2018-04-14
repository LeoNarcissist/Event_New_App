package com.example.rift.tifr.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rift.tifr.MainActivity;
import com.example.rift.tifr.data.DatabaseHelper;
import com.example.rift.tifr.Event.Event;
import com.example.rift.tifr.Event.ItemClickListener;
import com.example.rift.tifr.EventDetailActivity;
import com.example.rift.tifr.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.EventViewHolder>  {

    //Creating an arraylist of events objects
    private ArrayList<Event> events=new ArrayList<>();
    private final LayoutInflater inflater;
    View view;
    EventViewHolder holder;
    private Context context;
    private int whichActivity;
    DatabaseHelper mDatabaseHelper;
    private AlertDialog.Builder build;


    public CustomAdapter(Context context, ArrayList<Event> events, int activity){
        whichActivity=activity;
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.events=events;
    }


    //This method inflates view present in the RecyclerView
    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(whichActivity==2)
            view=inflater.inflate(R.layout.event_list_item_une, parent, false);
        else
        view=inflater.inflate(R.layout.event_list_item, parent, false);
        holder=new EventViewHolder(view);
        return holder;
    }

    //Binding the data using get() method of events object
    @Override
    public void onBindViewHolder(final EventViewHolder holder, int position) {
        Event list_items = events.get(position);
        holder.title.setText(list_items.getTitle());
        holder.time.setText(parseTime(list_items.getTime()));
        holder.date.setText(parseDate(list_items.getDate(),whichActivity));



        Glide.with(context)
                .load(list_items.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.event_image)
                .into(holder.picture);


        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, final int position, boolean isLongClick) {
                if (isLongClick && whichActivity==1) {
                    build = new AlertDialog.Builder(context);
                    build.setTitle("Delete Event");
                    build.setMessage("Do you want to delete ?");
                    final String id = events.get(position).getId();
                    build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context.getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                            mDatabaseHelper = new DatabaseHelper(context);
                            mDatabaseHelper.deleteData(id);
                            removeAt(position);
                            dialog.cancel();
                            if(events.size()==0) {
                                Intent intent = new Intent(context,  MainActivity.class);
                                context.startActivity(intent);
                                ((Activity)context).finish();
                            }
                        }
                    });
                    build.setNegativeButton("No", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = build.create();
                    alert.show();

                }
                    else {
                    Event eventCurrent = events.get(position);
                    Intent intent = new Intent(context, EventDetailActivity.class);
                    intent.putExtra("eventObject", eventCurrent);
                    context.startActivity(intent);

                }
            }
        });

}

    public String parseDate(String date, int activity) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern2 = "d\nMMM";
        String outputPattern1="EEE, MMM d, yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat2 = new SimpleDateFormat(outputPattern2);
        SimpleDateFormat outputFormat1 = new SimpleDateFormat(outputPattern1);
        Date formatDate;
        String str= null;

        try {
            formatDate = inputFormat.parse(date);
            if(activity==2)
            str = outputFormat2.format(formatDate);
            else
                str = outputFormat1.format(formatDate);
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

    //Setting the arraylist
    public void setListContent(ArrayList<Event> list_members){
        this.events=list_members;
        notifyItemRangeChanged(0,list_members.size());

    }


    @Override
    public int getItemCount() {
        return events.size();
    }


    //View holder class, where all view components are defined
    class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
    View.OnLongClickListener{
        TextView title,date,time;
        ImageView picture;
        private ItemClickListener itemClickListener;
        public EventViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            title=(TextView)itemView.findViewById(R.id.title);
            date=(TextView)itemView.findViewById(R.id.date);
            time=(TextView)itemView.findViewById(R.id.time);
            picture = (ImageView)itemView.findViewById(R.id.picture);

        }

        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),true);
            return true;
        }
    }

    public void removeAt(int position) {
        events.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, events.size());
    }

}



