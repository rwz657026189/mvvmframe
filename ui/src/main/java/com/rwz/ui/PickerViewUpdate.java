package com.rwz.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by rwz on 2016/8/31.
 * 滚动控件
 */
public class PickerViewUpdate<T extends IPickerEntity> extends View
{

	public static final String TAG = "PickerView";
	/**
	 * text之间间距和minTextSize之比
	 */
	public static final float MARGIN_ALPHA = 2.8f;
	/**
	 * 自动回滚到中间的速度
	 */
	public static final float SPEED = 2;

	private List<IPickerEntity> mDataList;
	/**
	 * 选中的位置，这个位置是mDataList的中心位置，一直不变
	 */
	private int mCurrentSelected;
	private Paint mPaint;

	/**
	 * onDraw()方法生效
	 */
	private float mMaxTextSize = 24;
	private float mMinTextSize = 18;

	private final float mMaxTextAlpha = 255;
	private final float mMinTextAlpha = 120;

	private int mColorText = 0x333333;

	private int mViewHeight;
	private int mViewWidth;

	private float mLastDownY;


	public void setTextSize(int mMaxTextSize,int mMinTextSize){
		this.mMaxTextSize = mMaxTextSize;
		this.mMinTextSize = mMinTextSize;
	}
	/**
	 * 滑动的距离
	 */
	private float mMoveLen = 0;
	private boolean isInit = false;
	private onSelectListener mSelectListener;
	private Timer timer;
	private MyTimerTask mTask;

	final Handler updateHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			if (Math.abs(mMoveLen) < SPEED)
			{
				mMoveLen = 0;
				if (mTask != null)
				{
					mTask.cancel();
					mTask = null;
					performSelect();
				}
			} else
				// 这里mMoveLen / Math.abs(mMoveLen)是为了保有mMoveLen的正负号，以实现上滚或下滚
				mMoveLen = mMoveLen - mMoveLen / Math.abs(mMoveLen) * SPEED;
			invalidate();
		}

	};

	public PickerViewUpdate(Context context)
	{
		super(context);
		init();
	}

	public PickerViewUpdate(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public void setOnSelectListener(onSelectListener listener)
	{
		mSelectListener = listener;
	}

	private void performSelect()
	{
		if (mSelectListener != null && isValid(mCurrentSelected)){
			//mCurrentSelected == size()/2;
			mSelectListener.onSelect(mDataList.get(mCurrentSelected));
		}
	}

	public void setData(List<? extends IPickerEntity> datas)
	{
		if (datas == null) {
			datas = new ArrayList<>();
		}
		if (mDataList == null) {
			mDataList = new ArrayList<>();
		} else {
			mDataList.clear();
		}
		mDataList.addAll(datas);
		mCurrentSelected = datas.size() / 2;
		invalidate();
	}

	/**
	 * 选择选中的item的index
	 *
	 * @param selected
	 */
	public void setSelected(int selected)
	{
		if (!isValid(selected)) {
			return;
		}
		mCurrentSelected = selected;
		int distance = mDataList.size() / 2 - mCurrentSelected;
		if (distance < 0)
			for (int i = 0; i < -distance; i++)
			{
				moveHeadToTail();
				mCurrentSelected--;
			}
		else if (distance > 0)
			for (int i = 0; i < distance; i++)
			{
				moveTailToHead();
				mCurrentSelected++;
			}
		/*if (mSelectListener != null)
			mSelectListener.onSelect(mDataList.get(mCurrentSelected),mCurrentSelected);*/
		invalidate();
	}

	private boolean isValid(int pos) {
		if (mDataList == null || mDataList.isEmpty()) {
			return false;
		} else {
			int size = mDataList.size();
			return pos >= 0 && size > pos;
		}
	}

	/**
	 * 选择选中的内容
	 *
	 * @param mSelectItem
	 */
	public void setSelected(IPickerEntity mSelectItem)
	{
		if (mSelectItem == null || mDataList == null) {
			return;
		}
		for (int i = 0; i < mDataList.size(); i++) {
			IPickerEntity entity = mDataList.get(i);
			if (entity.equals(mSelectItem))
			{
				setSelected(i);
				break;
			}
		}
	}

	private void moveHeadToTail()
	{
		if (!isValid(0)) {
			return;
		}
		IPickerEntity head = mDataList.get(0);
		mDataList.remove(0);
		mDataList.add(head);
	}

	private void moveTailToHead()
	{
		if (!isValid(1)) {
			return;
		}
		IPickerEntity tail = mDataList.get(mDataList.size() - 1);
		mDataList.remove(mDataList.size() - 1);
		mDataList.add(0, tail);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mViewHeight = getMeasuredHeight()-10;
		mViewWidth = getMeasuredWidth();
		// 按照View的高度计算字体大小
		mMaxTextSize = mViewHeight / 7f;
		mMinTextSize = mMaxTextSize / 1.5f;
		isInit = true;
		invalidate();
	}

	private void init()
	{
		timer = new Timer();
		mDataList = new ArrayList<>();
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Style.FILL);
		mPaint.setTextAlign(Align.CENTER);
		mColorText = getResources().getColor(R.color.text_black_color);
		mPaint.setColor(mColorText);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		// 根据index绘制view
		if (isInit)
			drawData(canvas);
	}

	private void drawData(Canvas canvas)
	{
		if (mDataList == null || mDataList.isEmpty()) {
			return;
		}
		// 先绘制选中的text再往上往下绘制其余的text
		float scale = parabola(mViewHeight / 4.0f, mMoveLen);
		float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
		mPaint.setTextSize(size);
		mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
		// text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
		float x = (float) (mViewWidth / 2.0);
		float y = (float) (mViewHeight / 2.0 + mMoveLen);
		FontMetricsInt fmi = mPaint.getFontMetricsInt();
		float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));

		IPickerEntity entity = mDataList.get(mCurrentSelected);
		canvas.drawText(entity.getText(), x, baseline, mPaint);
		// 绘制上方data
		for (int i = 1; (mCurrentSelected - i) >= 0; i++)
		{
			drawOtherText(canvas, i, -1);
		}
		// 绘制下方data
		for (int i = 1; (mCurrentSelected + i) < mDataList.size(); i++)
		{
			drawOtherText(canvas, i, 1);
		}

	}

	/**
	 * @param canvas
	 * @param position
	 *            距离mCurrentSelected的差值
	 * @param type
	 *            1表示向下绘制，-1表示向上绘制
	 */
	private void drawOtherText(Canvas canvas, int position, int type)
	{
		float d = MARGIN_ALPHA * mMinTextSize * position + type
				* mMoveLen;
		float scale = parabola(mViewHeight / 4.0f, d);
		float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
		mPaint.setTextSize(size);
		mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
		float y = (float) (mViewHeight / 2.0 + type * d);
		FontMetricsInt fmi = mPaint.getFontMetricsInt();
		float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
		IPickerEntity entity = mDataList.get(mCurrentSelected + type * position);
		canvas.drawText(entity.getText(),
				(float) (mViewWidth / 2.0), baseline, mPaint);
	}

	/**
	 * 抛物线
	 *
	 * @param zero
	 *            零点坐标
	 * @param x
	 *            偏移量
	 * @return scale
	 */
	private float parabola(float zero, float x)
	{
		float f = (float) (1 - Math.pow(x / zero, 2));
		return f < 0 ? 0 : f;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		switch (event.getActionMasked())
		{
			case MotionEvent.ACTION_DOWN:
				doDown(event);
				break;
			case MotionEvent.ACTION_MOVE:
				doMove(event);
				break;
			case MotionEvent.ACTION_UP:
				doUp(event);
				break;
		}
		return true;
	}

	private void doDown(MotionEvent event)
	{
		if (mTask != null)
		{
			mTask.cancel();
			mTask = null;
		}
		mLastDownY = event.getY();
	}

	private void doMove(MotionEvent event)
	{

		mMoveLen += (event.getY() - mLastDownY);

		if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2)
		{
			// 往下滑超过离开距离
			moveTailToHead();
			mMoveLen = mMoveLen - MARGIN_ALPHA * mMinTextSize;
		} else if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2)
		{
			// 往上滑超过离开距离
			moveHeadToTail();
			mMoveLen = mMoveLen + MARGIN_ALPHA * mMinTextSize;
		}

		mLastDownY = event.getY();
		invalidate();
	}

	private void doUp(MotionEvent event)
	{
		// 抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
		if (Math.abs(mMoveLen) < 0.0001)
		{
			mMoveLen = 0;
			return;
		}
		if (mTask != null)
		{
			mTask.cancel();
			mTask = null;
		}
		mTask = new MyTimerTask(updateHandler);
		timer.schedule(mTask, 0, 10);
	}

	class MyTimerTask extends TimerTask
	{
		final Handler handler;

		public MyTimerTask(Handler handler)
		{
			this.handler = handler;
		}

		@Override
		public void run()
		{
			handler.sendMessage(handler.obtainMessage());
		}

	}

	public int getCurrentSelected() {
		return mCurrentSelected;
	}

	public interface onSelectListener<T>
	{
		void onSelect(T text);
	}
}
