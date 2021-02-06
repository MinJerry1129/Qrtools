package com.kessi.photopipcollagemaker.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.kessi.photopipcollagemaker.R;
import com.kessi.photopipcollagemaker.model.TemplateItem;
import com.tonicartos.superslim.GridSLM;
import com.tonicartos.superslim.LinearSLM;

import java.util.ArrayList;

public class TemplateAdapter extends RecyclerView.Adapter<TemplateViewHolder> {
    public static final int VIEW_TYPE_HEADER = 0x01;

    public static final int VIEW_TYPE_CONTENT = 0x00;

    private static final int LINEAR = 0;

    private final ArrayList<TemplateItem> mItems;

    private int mHeaderDisplay;

    private boolean mMarginsFixed;

    private final Context mContext;
    private TemplateViewHolder.OnTemplateItemClickListener mOnTemplateItemClickListener;
    private LayoutInflater mInflater;
    boolean isPip;

    public TemplateAdapter(Context context, int headerMode, ArrayList<TemplateItem> data,
                           TemplateViewHolder.OnTemplateItemClickListener listener, boolean isPip) {
        mContext = context;
        mHeaderDisplay = headerMode;
        mOnTemplateItemClickListener = listener;
        this.isPip = isPip;
        mItems = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
        //Insert headers into list of items.
        String lastHeader = "";
        int sectionManager = -1;
        int headerCount = 0;
        int sectionFirstPosition = 0;
        for (int i = 0; i < data.size(); i++) {
            final TemplateItem item = data.get(i);
            String header = "" + item.getPhotoItemList().size();
            if (!TextUtils.equals(lastHeader, header)) {
                TemplateItem headerItem = new TemplateItem();
                sectionManager = 1;//(sectionManager + 1) % 2;
                sectionFirstPosition = i + headerCount;
                lastHeader = header;
                headerCount += 1;

                headerItem.setIsHeader(true);
                headerItem.setHeader(header);
                headerItem.setSectionManager(sectionManager);
                headerItem.setSectionFirstPosition(sectionFirstPosition);
                mItems.add(headerItem);
            }
            item.setSectionFirstPosition(sectionFirstPosition);
            item.setSectionManager(sectionManager);
            item.setIsHeader(false);
            mItems.add(item);
        }
    }

    public void setData(ArrayList<TemplateItem> data){
        //Insert headers into list of items.
        mItems.clear();
        String lastHeader = "";
        int sectionManager = -1;
        int headerCount = 0;
        int sectionFirstPosition = 0;
        for (int i = 0; i < data.size(); i++) {
            final TemplateItem item = data.get(i);
            String header = "" + item.getPhotoItemList().size();
            if (!TextUtils.equals(lastHeader, header)) {
                TemplateItem headerItem = new TemplateItem();
                sectionManager = 1;//(sectionManager + 1) % 2;
                sectionFirstPosition = i + headerCount;
                lastHeader = header;
                headerCount += 1;

                headerItem.setIsHeader(true);
                headerItem.setHeader(header);
                headerItem.setSectionManager(sectionManager);
                headerItem.setSectionFirstPosition(sectionFirstPosition);
                mItems.add(headerItem);
            }
            item.setSectionFirstPosition(sectionFirstPosition);
            item.setSectionManager(sectionManager);
            item.setIsHeader(false);
            mItems.add(item);
        }
        notifyDataSetChanged();
    }

    @Override
    public TemplateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_HEADER) {
            view = mInflater.inflate(R.layout.header_item, parent, false);
        } else {
            if (isPip){
                view = mInflater.inflate(R.layout.item_template, parent, false);
            }else {
                view = mInflater.inflate(R.layout.item_pip, parent, false);
            }
        }
        return new TemplateViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(TemplateViewHolder holder, int position) {
        final TemplateItem item = mItems.get(position);
        final View itemView = holder.itemView;

        holder.bindItem(item, mOnTemplateItemClickListener);

        final GridSLM.LayoutParams lp = GridSLM.LayoutParams.from(itemView.getLayoutParams());
        // Overrides xml attrs, could use different layouts too.
        if (item.isHeader()) {
            lp.headerDisplay = mHeaderDisplay;
            if (lp.isHeaderInline() || (mMarginsFixed && !lp.isHeaderOverlay())) {
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            }

            lp.headerEndMarginIsAuto = !mMarginsFixed;
            lp.headerStartMarginIsAuto = !mMarginsFixed;
        }
        lp.setSlm(item.getSectionManager() == LINEAR ? LinearSLM.ID : GridSLM.ID);
        if (isPip) {
            lp.setColumnWidth(mContext.getResources().getDimensionPixelSize(R.dimen.grid_column_width));
        }else {
            lp.setColumnWidth(mContext.getResources().getDimensionPixelSize(R.dimen.pip_grid_column_width));
        }
        lp.setFirstPosition(item.getSectionFirstPosition());
        itemView.setLayoutParams(lp);
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).isHeader() ? VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setHeaderDisplay(int headerDisplay) {
        mHeaderDisplay = headerDisplay;
        notifyHeaderChanges();
    }

    public void setMarginsFixed(boolean marginsFixed) {
        mMarginsFixed = marginsFixed;
        notifyHeaderChanges();
    }

    private void notifyHeaderChanges() {
        for (int i = 0; i < mItems.size(); i++) {
            TemplateItem item = mItems.get(i);
            if (item.isHeader()) {
                notifyItemChanged(i);
            }
        }
    }
}
