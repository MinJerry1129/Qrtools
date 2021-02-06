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

import androidx.fragment.app.Fragment;

import com.kessi.imageeditlibrary.R;


public class ImagesFragment extends Fragment {

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.galleryimages, null);

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

		Cursor cur = getActivity().getContentResolver()
				.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						new String[] { MediaStore.Images.Media.DATA,
								MediaStore.Images.Media.DISPLAY_NAME,
								MediaStore.Images.Media.DATE_TAKEN,MediaStore.Images.Media.SIZE },
						MediaStore.Images.Media.BUCKET_ID + " = ?",
						new String[] { String.valueOf(getArguments().getInt(
								"bucket")) },
						MediaStore.Images.Media.DATE_MODIFIED + " DESC");

		final List<GridItem> images = new ArrayList<GridItem>(cur.getCount());

		if (cur != null) {
			if (cur.moveToFirst()) {
				while (!cur.isAfterLast()) {
					images.add(new GridItem(cur.getString(1), cur.getString(0),cur.getString(2),cur.getLong(3)));
					cur.moveToNext();
				}
			}
			cur.close();
		}

		GridView grid = (GridView) v.findViewById(R.id.grid);
		grid.setAdapter(new GalleryAdapter(getActivity(), images));
		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				((SelectPictureActivity) getActivity()).imageSelected(images
						.get(position).path,images
						.get(position).imageTaken,images
						.get(position).imageSize);
			}
		});
		return v;
	}

}
