package com.xinlan.imageeditlibrary.editimage.adapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.kessi.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.fragment.StickerFragment;


public class StickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public DisplayImageOptions imageOption = new DisplayImageOptions.Builder()
            .cacheInMemory(true).showImageOnLoading(R.drawable.yd_image_tx)
            .build();//

    private StickerFragment mStickerFragment;
    private ImageClick mImageClick = new ImageClick();
    private List<String> pathList = new ArrayList<String>();//

    public StickerAdapter(StickerFragment fragment) {
        super();
        this.mStickerFragment = fragment;
    }

    public class ImageHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public ImageHolder(View itemView) {
            super(itemView);
            this.image = (ImageView) itemView.findViewById(R.id.img);
        }
    }// end inner class

    @Override
    public int getItemCount() {
        return pathList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View v = null;
        v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_sticker_item, parent, false);
        ImageHolder holer = new ImageHolder(v);
        return holer;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageHolder imageHoler = (ImageHolder) holder;
        String path = pathList.get(position);
        ImageLoader.getInstance().displayImage("assets://" + path,
                imageHoler.image, imageOption);
        imageHoler.image.setTag(path);
        imageHoler.image.setOnClickListener(mImageClick);

        LinearLayout.LayoutParams paramsBtn = new LinearLayout.LayoutParams(
                mStickerFragment.getActivity().getResources().getDisplayMetrics().widthPixels * 130 / 1080,
                mStickerFragment.getActivity().getResources().getDisplayMetrics().heightPixels * 130 / 1920);
        paramsBtn.gravity = Gravity.CENTER;
        paramsBtn.leftMargin = 15;
        imageHoler.image.setLayoutParams(paramsBtn);
    }

    public void addStickerImages(String folderPath) {
        pathList.clear();
        try {
            String[] files = mStickerFragment.getActivity().getAssets()
                    .list(folderPath);
            for (String name : files) {
                pathList.add(folderPath + File.separator + name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.notifyDataSetChanged();
    }

    private final class ImageClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            String data = (String) v.getTag();
            //System.out.println("data---->" + data);
            mStickerFragment.selectedStickerItem(data);
        }
    }// end inner class

}// end class
