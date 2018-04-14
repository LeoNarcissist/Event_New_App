package com.example.rift.tifr.Adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rift.tifr.CategoryActivity;
import com.example.rift.tifr.R;
import com.example.rift.tifr.Event.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ItemRowHolder> {

    private List<Category> categoryList;
    private Context mContext;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.categoryList = categoryList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, final int i) {

        final String categoryName = categoryList.get(i).getTitle();
        final ArrayList events = categoryList.get(i).getEvent();
        itemRowHolder.itemTitle.setText(categoryName);
        CustomAdapter itemListDataAdapter = new CustomAdapter(mContext, events,2);
        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);
         itemRowHolder.recycler_view_list.setNestedScrollingEnabled(false);

        itemRowHolder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CategoryActivity.class);
                intent.putExtra("eventObject", events);
                intent.putExtra("categoryObject", categoryName);
                mContext.startActivity(intent);
             //   Toast.makeText(v.getContext(), "click event on more, "+categoryName , Toast.LENGTH_SHORT).show();

            }
        });

    }
    @Override
    public int getItemCount() {
        return (null != categoryList ? categoryList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        protected TextView itemTitle;
        protected RecyclerView recycler_view_list;
        protected Button btnMore;

        public ItemRowHolder(View view) {
            super(view);

            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);
            this.btnMore= (Button) view.findViewById(R.id.btnMore);

        }
    }
}