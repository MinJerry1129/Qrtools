package com.xinlan.imageeditlibrary.editimage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.kessi.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.fragment.PaintFragment;

public class ColorListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_COLOR = 1;
    public static final int TYPE_MORE = 2;

    public interface IColorListAction{
        void onColorSelected(final int position,final int color);
        void onMoreSelected(final int position);
    }

    private PaintFragment mContext;
    private int[] colorsData;

    private IColorListAction mCallback;


    public ColorListAdapter(PaintFragment frg, int[] colors,IColorListAction action) {
        super();
        this.mContext = frg;
        this.colorsData = colors;
        this.mCallback = action;
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder {
        View colorPanelView;

        public ColorViewHolder(View itemView) {
            super(itemView);
            this.colorPanelView = itemView.findViewById(R.id.color_panel_view);
        }
    }// end inner class

    public class MoreViewHolder extends RecyclerView.ViewHolder {
        View moreBtn;
        public MoreViewHolder(View itemView) {
            super(itemView);
            this.moreBtn = itemView.findViewById(R.id.color_panel_more);
        }

    }//end inner class

    @Override
    public int getItemCount() {
        return colorsData.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return colorsData.length == position ? TYPE_MORE : TYPE_COLOR;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == TYPE_COLOR) {
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.view_color_panel, parent,false);
            viewHolder = new ColorViewHolder(v);
        } else if (viewType == TYPE_MORE) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_color_more_panel,parent,false);
            viewHolder = new MoreViewHolder(v);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if(type == TYPE_COLOR){
            onBindColorViewHolder((ColorViewHolder)holder,position);
        }else if(type == TYPE_MORE){
            onBindColorMoreViewHolder((MoreViewHolder)holder,position);
        }
    }

    private void onBindColorViewHolder(final ColorViewHolder holder,final int position){
        holder.colorPanelView.setBackgroundColor(colorsData[position]);
        holder.colorPanelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCallback!=null){
                    mCallback.onColorSelected(position,colorsData[position]);
                }
            }
        });

        RelativeLayout.LayoutParams paramsBtn = new RelativeLayout.LayoutParams(
                mContext.getActivity().getResources().getDisplayMetrics().widthPixels * 100 / 1080,
                mContext.getActivity().getResources().getDisplayMetrics().heightPixels * 100 / 1920);
        paramsBtn.leftMargin = 10;
        holder.colorPanelView.setLayoutParams(paramsBtn);
    }

    private void onBindColorMoreViewHolder(final MoreViewHolder holder,final int position){
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCallback!=null){
                    mCallback.onMoreSelected(position);
                }
            }
        });
    }

}// end class
