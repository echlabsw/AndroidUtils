public class Indicator extends View {
	private int center;
	private int width;
	private int height;
	private Paint mPaint;
	private int strokeWidth;

	private boolean touched;

	public Indicator(Context cx, int width, int height) {
		super(cx);
		this.width = width;
		this.height = height;
		mPaint = new Paint();
		strokeWidth = 3;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mPaint.setColor(Color.TRANSPARENT);
		int left = center - width / 2;
		int right = center + width / 2;
		canvas.drawRect(new Rect(left + strokeWidth, 0 + strokeWidth, right
				- strokeWidth, height - strokeWidth), mPaint);
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
			if (touched) {
				center = (int) event.getX();
				invalidate();
			}
			break;
		case MotionEvent.ACTION_UP:
			touched = false;
			break;
		}
		return true;
	}

}
