package com.xinlan.imageeditlibrary.editimage.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.kessi.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.fragment.StickerFragment;


public class StickerTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String[] stickerPath = {"stickers/type1", "stickers/type2", "stickers/type3", "stickers/type4", "stickers/type5", "stickers/type6"};
    public static final String[] stickerPathName = {"Love", "Heart", "Romantic", "Couple", "Quote", "B'Day"};
    int[] typeIcon = {R.drawable.love, R.drawable.heart, R.drawable.romentic, R.drawable.couple, R.drawable.quotes, R.drawable.b_day};
    private StickerFragment mStickerFragment;
    private ImageClick mImageClick = new ImageClick();

    public StickerTypeAdapter(StickerFragment fragment) {
        super();
        this.mStickerFragment = fragment;
    }

    public class ImageHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public ImageView text;

        public ImageHolder(View itemView) {

            super(itemView);
            this.icon = (ImageView) itemView.findViewById(R.id.icon);
            this.text = (ImageView) itemView.findViewById(R.id.text);
        }
    }// end inner class

    @Override
    public int getItemCount() {
        return stickerPathName.length;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View v = null;
        v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_sticker_type_item, parent, false);
        ImageHolder holer = new ImageHolder(v);
        return holer;
    }

    /**
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageHolder imageHoler = (ImageHolder) holder;
        // imageHoler.icon.setImageResource(R.drawable.ic_launcher);
//        String name = stickerPathName[position];
//        imageHoler.text.setText(name);
        imageHoler.text.setImageResource(typeIcon[position]);
        imageHoler.text.setTag(stickerPath[position]);
        imageHoler.text.setOnClickListener(mImageClick);
        LinearLayout.LayoutParams paramsBtn = new LinearLayout.LayoutParams(
                mStickerFragment.getActivity().getResources().getDisplayMetrics().widthPixels * 140 / 1080,
                mStickerFragment.getActivity().getResources().getDisplayMetrics().heightPixels * 140 / 1920);
        paramsBtn.gravity = Gravity.CENTER;
        imageHoler.text.setLayoutParams(paramsBtn);
    }


    private final class ImageClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            String data = (String) v.getTag();
            // System.out.println("data---->" + data);
            mStickerFragment.swipToStickerDetails(data);
        }
    }// end inner class
}// end class
