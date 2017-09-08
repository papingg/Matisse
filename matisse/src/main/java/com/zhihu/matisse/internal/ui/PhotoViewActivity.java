package com.zhihu.matisse.internal.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zhihu.matisse.R;
import com.zhihu.matisse.internal.entity.Item;
import com.zhihu.matisse.internal.utils.PathUtils;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

/**
 * The type Photo activity.
 */
public class PhotoViewActivity extends Activity {

	private static final String EXTRA_ITEM = "extra-item";
	private Item item;

	public static Intent create(final Context context, Item item) {
		Intent intent = new Intent(context, PhotoViewActivity.class);
		intent.putExtra(EXTRA_ITEM, item);
		return intent;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.fragment_preview_item);

		item = getIntent().getParcelableExtra(EXTRA_ITEM);
		if (item == null) {
			return;
		}

		View videoPlayButton = findViewById(R.id.video_play_button);
		if (item.isVideo()) {
			videoPlayButton.setVisibility(View.VISIBLE);
			videoPlayButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(item.uri, "video/*");
					try {
						startActivity(intent);
					} catch (ActivityNotFoundException e) {
						Toast.makeText(PhotoViewActivity.this, R.string.error_no_video_activity, Toast.LENGTH_SHORT).show();
					}
				}
			});
		} else {
			videoPlayButton.setVisibility(View.GONE);
		}

		ImageViewTouch image = (ImageViewTouch) findViewById(R.id.image_view);
		image.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);

		Glide.with(this)
				.asBitmap()
				.load(PathUtils.getPath(this, item.getContentUri()))
				.into(image);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
