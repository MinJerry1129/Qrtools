package com.kessi.photopipcollagemaker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kessi.photopipcollagemaker.Activities.CollageMakerlActivity;
import com.kessi.photopipcollagemaker.R;


public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {
    Context context;
    String[] colors;
    int selectPos = 8;
    public ColorAdapter(Context context, String[] colors) {
        this.context = context;
        this.colors = colors;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.color_row_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String color = colors[position];
        holder.colorImg.setBackgroundColor(Color.parseColor(color));

        if (position == selectPos) {
            holder.mSelectedView.setVisibility(View.VISIBLE);
        } else {
            holder.mSelectedView.setVisibility(View.GONE);
        }

        holder.color_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPos = position;
                ((CollageMakerlActivity) context).setBGColor(color);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return colors.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView color_layout;
        ImageView colorImg;
        private View mSelectedView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            color_layout = itemView.findViewById(R.id.color_layout);
            colorImg = itemView.findViewById(R.id.colorImg);
            mSelectedView = itemView.findViewById(R.id.selectedView);
        }
    }
}


