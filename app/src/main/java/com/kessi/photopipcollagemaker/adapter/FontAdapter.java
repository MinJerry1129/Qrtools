package com.kessi.photopipcollagemaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kessi.photopipcollagemaker.R;
import com.kessi.photopipcollagemaker.model.FontItem;
import com.kessi.photopipcollagemaker.utils.TextUtils;

import java.util.List;


public class FontAdapter extends ArrayAdapter<FontItem> {
    private LayoutInflater mInflater;
    private Context context;

    public FontAdapter(Context context, List<FontItem> objects) {
        super(context, R.layout.item_font, objects);
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        final FontItem item = getItem(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_font, parent, false);
            holder.textView = (TextView) convertView.findViewById(R.id.fontView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(item.getFontName());
        holder.textView.setTypeface(TextUtils.loadTypeface(context, item.getFontPath()));

        return convertView;
    }

    private class ViewHolder {
        TextView textView;
    }
}
