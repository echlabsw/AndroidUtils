package com.echlabsw.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ImageToolBar extends FrameLayout {

	private Context cx;
	private RelativeLayout outerLayout;

	private LinearLayout bitmapLayout;

	private Indicator indicator;

	private int count;

	public ImageToolBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		cx = context;
		outerLayout = new RelativeLayout(cx);
		outerLayout.setLayoutParams(new FrameLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		addView(outerLayout);
	}

	public void setup(Bitmap bitmap[], int bitmapItemWidth) {
		count = bitmap.length;
		setBitmap(bitmap);
		indicator = new Indicator(cx, 50, 100, count, bitmapItemWidth);
		addView(indicator);
	}

	private void setBitmap(Bitmap bitmap[]) {
		ImageView img = null;
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 100);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		bitmapLayout = new LinearLayout(cx);
		bitmapLayout.setLayoutParams(lp);
		bitmapLayout.setOrientation(LinearLayout.HORIZONTAL);
		for (Bitmap b : bitmap) {
			img = new ImageView(cx);
			img.setLayoutParams(new LinearLayout.LayoutParams(80, 100));
			img.setImageBitmap(b);
			bitmapLayout.addView(img);
		}
		outerLayout.addView(bitmapLayout);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// Log.d("test", "w: " + bitmapLayout.getMeasuredWidth());
		// Log.d("test", "h: " + bitmapLayout.getMeasuredHeight());
		Rect r = new Rect();
		bitmapLayout.getGlobalVisibleRect(r);
		// Log.d("test", "start left: " + r.left);
		// Log.d("test", "end right: " + r.right);
		indicator.setBound(r);
	}

	private class Indicator extends View {
		private int center;
		private int left;
		private int right;
		private int width;
		private int height;
		private Paint mPaint;
		private int strokeWidth;
		private int minX;
		private int maxX;

		private int itemsCount;

		private boolean touched;

		private int actualItemIndicated;

		private Rect rectIndicator;

		public Indicator(Context cx, int width, int height, int itemsCount,
				int widthItem) {
			super(cx);
			this.width = width;
			this.height = height;
			mPaint = new Paint();
			strokeWidth = 3;
			this.itemsCount = itemsCount;
		}

		public void setBound(Rect r) {
			minX = r.left;
			maxX = r.right;
			center = minX;
			invalidate();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			mPaint.setColor(Color.TRANSPARENT);
			left = center - width / 2;
			right = center + width / 2;
			rectIndicator = new Rect(left + strokeWidth, 0 + strokeWidth, right
					- strokeWidth, height - strokeWidth);
			canvas.drawRect(rectIndicator, mPaint);
			mPaint.setColor(Color.GRAY);
			mPaint.setStrokeWidth(strokeWidth);
			// top
			canvas.drawLine(left, 0, right, 0, mPaint);
			// left
			canvas.drawLine(left, 0, left, height, mPaint);
			// bottom
			canvas.drawLine(left, height, right, height, mPaint);
			// right
			canvas.drawLine(right, 0, right, height, mPaint);

			int index = computeActualItemIndicated();
			if (actualItemIndicated != index) {
				actualItemIndicated = index;
				notifyIndexChanged();
			}
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (event.getY() >= 0 && event.getY() <= height) {
					touched = true;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				int x = (int) event.getX();
				if (touched && x >= minX && x <= maxX) {
					center = x;
					invalidate();
				}
				break;
			case MotionEvent.ACTION_UP:
				touched = false;
				break;
			}
			return true;
		}

		/**
		 * Calcola l'indice dell'item attualmente sotto l'indicatore
		 * 
		 * actualXposition / intervalWidth
		 */
		private int computeActualItemIndicated() {
			int intervalWidth = (maxX - minX) / itemsCount;
			int index = (int) Math.floor((rectIndicator.centerX() - minX)
					/ intervalWidth);
			if (index >= itemsCount) {
				return itemsCount - 1;
			} else {
				return index;
			}
		}

		public void notifyIndexChanged() {
			Log.d("test", "actualItemIndicated: " + actualItemIndicated);
		}
	}

}
