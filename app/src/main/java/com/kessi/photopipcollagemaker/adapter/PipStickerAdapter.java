package com.kessi.photopipcollagemaker.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kessi.photopipcollagemaker.Activities.PIPActivity;
import com.kessi.photopipcollagemaker.R;


public class PipStickerAdapter extends RecyclerView.Adapter<PipStickerAdapter.ViewHolder> {
    Context context;
    String[] emojies;

    public PipStickerAdapter(Context context, String[] emojies) {
        this.context = context;
        this.emojies = emojies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.sticker_row_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String sticker = emojies[position];
        Log.e("path", sticker);

        Glide.with(context).load(Uri.parse("file:///android_asset/stickers/" + sticker)).into(holder.emogies);
        holder.emoji_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PIPActivity) context).setEmojiesSticker(sticker);
            }
        });
    }

    @Override
    public int getItemCount() {
        return emojies.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout emoji_layout;
        ImageView emogies;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            emoji_layout = itemView.findViewById(R.id.emoji_layout);
            emogies = itemView.findViewById(R.id.emogies);
        }
    }
}


