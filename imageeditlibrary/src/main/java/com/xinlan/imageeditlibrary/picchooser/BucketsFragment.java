package com.xinlan.imageeditlibrary.picchooser;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.kessi.imageeditlibrary.R;


public class BucketsFragment extends Fragment {

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.gallery, null);

        RelativeLayout top = v.findViewById(R.id.top);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                getResources().getDisplayMetrics().widthPixels,
                getResources().getDisplayMetrics().heightPixels * 150 / 1920);
        top.setLayoutParams(params);

        ImageView back = v.findViewById(R.id.btnBack);
        LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(
                getResources().getDisplayMetrics().widthPixels * 90 / 1080,
                getResources().getDisplayMetrics().heightPixels * 90 / 1920);
        back.setLayoutParams(pp);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        String[] projection = new String[]{MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_ID};

        Cursor cur = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " ASC, "
                        + MediaStore.Images.Media.DATE_MODIFIED + " DESC");

        final List<GridItem> buckets = new ArrayList<GridItem>();
        BucketItem lastBucket = null;

        if (cur != null) {
            if (cur.moveToFirst()) {
                while (!cur.isAfterLast()) {
                    if (lastBucket == null
                            || !lastBucket.name.equals(cur.getString(1))) {
                        lastBucket = new BucketItem(cur.getString(1),
                                cur.getString(0), "", cur.getInt(2));
                        buckets.add(lastBucket);
                    } else {
                        lastBucket.images++;
                    }
                    cur.moveToNext();
                }
            }
            cur.close();
        }

        if (buckets.isEmpty()) {
            Toast.makeText(getActivity(), R.string.no_images,
                    Toast.LENGTH_SHORT).show();
            getActivity().finish();
        } else {
            GridView grid = (GridView) v.findViewById(R.id.grid);
            grid.setAdapter(new GalleryAdapter(getActivity(), buckets));
            grid.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    ((SelectPictureActivity) getActivity())
                            .showBucket(((BucketItem) buckets.get(position)).id);
                }
            });
        }
        return v;
    }

}
