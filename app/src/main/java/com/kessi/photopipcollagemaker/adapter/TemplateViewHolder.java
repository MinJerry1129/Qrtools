package com.kessi.photopipcollagemaker.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kessi.photopipcollagemaker.R;
import com.kessi.photopipcollagemaker.model.TemplateItem;
import com.kessi.photopipcollagemaker.utils.ImageUtils;


/**
 * Simple view holder for a single text view.
 */
public class TemplateViewHolder extends RecyclerView.ViewHolder {
    public interface OnTemplateItemClickListener {
        void onTemplateItemClick(final TemplateItem item);
    }

    private ImageView mImageView;
    private TextView mTextView;
    private int mViewType = TemplateAdapter.VIEW_TYPE_CONTENT;

    TemplateViewHolder(View view, int viewType) {
        super(view);
        mViewType = viewType;
        mTextView =  view.findViewById(R.id.text);
        mImageView = (ImageView) view.findViewById(R.id.imageView);
    }

    public void bindItem(final TemplateItem item, final OnTemplateItemClickListener listener) {
        if (mViewType == TemplateAdapter.VIEW_TYPE_HEADER && mTextView != null) {
            mTextView.setText(item.getHeader());
        } else if (mImageView != null) {
            ImageUtils.loadImageWithPicasso(mImageView.getContext(), mImageView, item.getPreview());
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onTemplateItemClick(item);
                    }
                }
            });
        }
    }
}