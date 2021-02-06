package com.kessi.photopipcollagemaker.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.kessi.photopipcollagemaker.R;
import com.kessi.photopipcollagemaker.model.ImageModel;
import com.kessi.photopipcollagemaker.myinterface.OnListAlbum;

import java.util.ArrayList;


public class ListAlbumAdapter extends ArrayAdapter<ImageModel> {
    Context context;
    ArrayList<ImageModel> data = new ArrayList();
    int layoutResourceId;
    OnListAlbum onListAlbum;
    int pHeightItem = 0;

    static class RecordHolder {
        ImageView click;
        ImageView imageItem;
        RelativeLayout layoutRoot;

        RecordHolder() {
        }
    }

    public ListAlbumAdapter(Context context, int layoutResourceId, ArrayList<ImageModel> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.pHeightItem = (int) (getDisplayInfo((Activity) context).widthPixels / 4.5);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        RecordHolder holder;
        View row = convertView;
        if (row == null) {
            row = ((Activity) this.context).getLayoutInflater().inflate(this.layoutResourceId, parent, false);
            holder = new RecordHolder();
            holder.imageItem = (ImageView) row.findViewById(R.id.imageItem);
            holder.click = (ImageView) row.findViewById(R.id.click);
            holder.layoutRoot = (RelativeLayout) row.findViewById(R.id.layoutRoot);
            holder.layoutRoot.getLayoutParams().height = this.pHeightItem;
            holder.imageItem.getLayoutParams().width = this.pHeightItem;
            holder.imageItem.getLayoutParams().height = this.pHeightItem;
            holder.click.getLayoutParams().width = this.pHeightItem;
            holder.click.getLayoutParams().height = this.pHeightItem;
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }
        final ImageModel item = (ImageModel) this.data.get(position);

        Glide.with(context).load(item.getPathFile()).placeholder(R.drawable.piclist_icon_default).into(holder.imageItem);

        row.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ListAlbumAdapter.this.onListAlbum != null) {
                    ListAlbumAdapter.this.onListAlbum.OnItemListAlbumClick(item);
                }
            }
        });
        return row;
    }

    public OnListAlbum getOnListAlbum() {
        return this.onListAlbum;
    }

    public void setOnListAlbum(OnListAlbum onListAlbum) {
        this.onListAlbum = onListAlbum;
    }

    public static DisplayMetrics getDisplayInfo(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }
}
