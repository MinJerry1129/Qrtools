package com.mobiledevteam.qrtools.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobiledevteam.qrtools.ShareActivity;
import com.mobiledevteam.qrtools.R;
import com.mobiledevteam.qrtools.model.MyAlbumMediaFile;
import com.mobiledevteam.qrtools.utils.AdManager;

import java.util.ArrayList;

public class MyCreationAdapter extends RecyclerView.Adapter<MyCreationAdapter.MyViewHolder> {
    ArrayList<MyAlbumMediaFile> mMediaFile;
    private Activity context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView galleryView;

        public MyViewHolder(View v) {
            super(v);
            galleryView = (ImageView) v.findViewById(R.id.galleryImage);
        }
    }

    public MyCreationAdapter(Activity context, ArrayList<MyAlbumMediaFile> myMediaFile) {
        this.context = context;
        this.mMediaFile = myMediaFile;
    }

    @Override
    public MyCreationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_list, null, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final MyAlbumMediaFile mFile = mMediaFile.get(position);
        Glide.with(context).load(mFile.getMediaUri())
                .into(holder.galleryView);


        holder.galleryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShareActivity.class);
                intent.putExtra("path", mFile.getMediaUri().getAbsolutePath());
                intent.putExtra("isCreation", true);

//                if (!AdManager.isloadFbAd) {
//                    AdManager.adCounter++;
//                    AdManager.showInterAd(context, intent,0);
//                } else {
//                    AdManager.adCounter++;
//                    AdManager.showFbInterAd(context, intent,0);
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMediaFile.size();
    }
}
