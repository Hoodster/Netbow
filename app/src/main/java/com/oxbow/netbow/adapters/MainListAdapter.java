package com.oxbow.netbow.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oxbow.netbow.R;
import com.oxbow.netbow.data.Serie;
import com.oxbow.netbow.imdb.TMDdConnect;

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
        RatingBar rating;
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Serie serie = getItem(position);
        ViewHolder holder = new ViewHolder();
        TMDdConnect td = new TMDdConnect();

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
            holder.rating = view.findViewById(R.id.tileRating);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        assert serie != null;
        
        holder.serieCover.setImageBitmap(serie.seriePoster);
        holder.title.setText(serie.serieTitle);
        //RESIZE TITLE IF IS OUT OF SCOPE
        while (holder.title.getLineCount() > 2) {
            DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
            float titleSize = holder.title.getTextSize() / metrics.density;
            holder.title.setTextSize(titleSize - 1);
        }
        //END COMMENT
        if (serie.secondGenre != null)
            holder.categories.setText(serie.firstGenre + ", " + serie.secondGenre);
        else
            holder.categories.setText(serie.firstGenre);
        holder.bigTitle.setText(serie.serieTitle);
        holder.description.setText(td.cutOverview(serie.serieDescription));
        holder.rating.setRating(serie.serieRating / 2);
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
