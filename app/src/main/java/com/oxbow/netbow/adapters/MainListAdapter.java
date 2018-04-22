package com.oxbow.netbow.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.oxbow.netbow.R;
import com.oxbow.netbow.data.Serie;

import java.util.ArrayList;
import java.util.List;

public class MainListAdapter extends ArrayAdapter<Serie> {
    private Context context;
    private List<Serie> series;

    public MainListAdapter(Context context, List<Serie> series) {
        super(context,R.layout.item_main_series,series);
        this.context = context;
        this.series = series;
    }

    public class ViewHolder {
        ImageView serieCover;
        TextView title,categories, bigTitle, description;
        FrameLayout minFrame;
        RelativeLayout bigFrame;
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Serie serie = getItem(position);
        ViewHolder holder = new ViewHolder();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_main_series,null);

            holder.serieCover = view.findViewById(R.id.tileCover);
            holder.title = view.findViewById(R.id.serieTitle);
            holder.categories = view.findViewById(R.id.serieCategories);
            holder.bigTitle = view.findViewById(R.id.bigTitle);
            holder.description = view.findViewById(R.id.tileDescription);
            holder.bigFrame = view.findViewById(R.id.itemDetails);
            holder.minFrame = view.findViewById(R.id.littleLabel);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        assert serie != null;
        holder.serieCover.setImageBitmap(BitmapFactory.decodeByteArray(serie.serieImage,0,serie.serieImage.length));
        holder.title.setText(serie.serieName);
        holder.categories.setText(serie.firstCategory + ", " + serie.secondCategory);
        holder.bigTitle.setText(serie.serieName);
        holder.description.setText(serie.serieDescription);
        final ViewHolder holder1 = holder;
        holder.minFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder1.bigFrame.setVisibility(View.VISIBLE);
                holder1.minFrame.setVisibility(View.INVISIBLE);
            }
        });
        holder.bigFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder1.bigFrame.setVisibility(View.INVISIBLE);
                holder1.minFrame.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    @Override
    public int getCount() {
        return series.size();
    }

    @Override
    public Serie getItem(int i) {
        return series.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    
    @Override
    public void add(Serie serie) {
        series.add(serie);
        notifyDataSetChanged();
        super.add(serie);
    }
    
    @Override
    public void remove(Serie serie) {
        series.remove(serie);
        notifyDataSetChanged();
        super.remove(serie);
    }
}
