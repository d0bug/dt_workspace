package com.rtmap.wifipicker.layer;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.view.MotionEvent;

import com.rtm.frm.map.BaseMapLayer;
import com.rtm.frm.map.MapView;
import com.rtm.frm.model.Location;
import com.rtm.frm.model.PointInfo;
import com.rtmap.wifipicker.core.model.RMPoi;
import com.rtmap.wifipicker.util.DTLog;
import com.rtmap.wifipicker.util.DTMathUtils;
import com.rtmap.wifipicker.util.DTStringUtils;

public class PointLayer implements BaseMapLayer {

	private Path mPath;

	private MapView mMapView;
	private Paint mRoutePaint;
	private Paint mRoutePaint2;
	private Paint mRouteBgPaint;
	private Paint mPopupPaint;
	private Paint mTextPaint;
	private OnPointClickListener mClickListener;


	private Bitmap mPointIcon;// 中间点icon

	private ArrayList<RMPoi> mPointList;

	/**
	 * 构造方法
	 * 
	 * @param view
	 *            MapView
	 * @param start
	 *            开始图标
	 * @param end
	 *            结束图标
	 * @param mark
	 *            中间点标记图标
	 */
	public PointLayer(MapView view, Bitmap pointBmp) {
		mMapView = view;
		mPointIcon = pointBmp;
		mPointList = new ArrayList<RMPoi>();
		initLayer(view);
	}

	/**
	 * 添加POI点集合
	 * 
	 * @param points
	 *            本楼层的POI点集合
	 */
	public boolean addPointList(ArrayList<RMPoi> points) {
		if (points == null || points.size() == 0)// key为空返回
			return false;
		mPointList.addAll(points);// 添加地图
		mMapView.refreshMap();
		return true;
	}

	/**
	 * 添加POI点
	 * 
	 * @param point
	 *            本楼层的POI点
	 */
	public boolean addPoint(RMPoi point) {
		if (point == null)// key为空返回
			return false;
		mPointList.add(point);// 添加地图
		mMapView.refreshMap();
		return true;
	}

	/**
	 * 得到Point
	 * 
	 * @param i
	 * @return
	 */
	public RMPoi getPoint(int i) {
		if (i > mPointList.size() - 1)
			return null;
		return mPointList.get(i);
	}
	
	public ArrayList<RMPoi> getPointList() {
		return mPointList;
	}

	/**
	 * 得到点的数量
	 * 
	 * @return
	 */
	public int getPointCount() {
		return mPointList.size();
	}

	/**
	 * 移除点
	 * 
	 * @param index
	 */
	public void clearPoint(int index) {
		if (index > mPointList.size() - 1)
			return;
		mPointList.remove(index);
	}

	/**
	 * 移除点
	 * 
	 * @param point
	 */
	public void clearPoint(RMPoi point) {
		if (mPointList.contains(point))
			mPointList.remove(point);
	}

	/**
	 * 清除所有点
	 */
	public void clearAllPoints() {
		mPointList.clear();
	}

	public OnPointClickListener getOnPointClickListener() {
		return mClickListener;
	}

	/**
	 * 设置点的监听器
	 * 
	 * @param mClickListener
	 */
	public void setOnPointClickListener(OnPointClickListener mClickListener) {
		this.mClickListener = mClickListener;
	}

	private Path makePathDash() {
		Path p = new Path();
		p.moveTo(4, 0);
		p.lineTo(0, -4);
		p.lineTo(8, -4);
		p.lineTo(12, 0);
		p.lineTo(8, 4);
		p.lineTo(0, 4);
		return p;
	}

	@Override
	public void initLayer(MapView view) {
		mRoutePaint = new Paint();
		mRoutePaint.setStyle(Style.STROKE);
		mRoutePaint.setStrokeWidth(10);
		mRoutePaint.setAntiAlias(true);
		mRoutePaint.setStrokeCap(Cap.ROUND);
		mRoutePaint.setStrokeJoin(Join.ROUND);
		mRoutePaint.setColor(0x960079C6);
		PathEffect peArray = new PathDashPathEffect

		(

		makePathDash(), // 形状

				18, // 间距

				0,// 首绘制偏移量

				PathDashPathEffect.Style.ROTATE

		);
		mRoutePaint.setPathEffect(peArray);

		mRoutePaint2 = new Paint();
		mRoutePaint2.setStyle(Style.STROKE);
		mRoutePaint2.setStrokeWidth(10);
		mRoutePaint2.setAntiAlias(true);
		mRoutePaint2.setStrokeCap(Cap.ROUND);
		mRoutePaint2.setStrokeJoin(Join.ROUND);
		mRoutePaint2.setColor(Color.rgb(0, 0, 255));
		// mRoutePaint2.setColor(0x960079C6);
		mRoutePaint2.setPathEffect(peArray);

		PathEffect effects = new DashPathEffect(new float[] { 15, 15, 15, 15 },
				1);
		// mRoutePaint2.setPathEffect(effects);
		mRoutePaint2.setAlpha(50);

		mRouteBgPaint = new Paint();
		mRouteBgPaint.setStyle(Style.STROKE);
		mRouteBgPaint.setStrokeWidth(14);
		mRouteBgPaint.setAntiAlias(true);
		mRouteBgPaint.setStrokeCap(Cap.ROUND);
		mRouteBgPaint.setStrokeJoin(Join.ROUND);
		mRouteBgPaint.setColor(Color.WHITE);

		mPath = new Path();

		mTextPaint = new Paint();
		mTextPaint.setTextAlign(Align.CENTER);
		mTextPaint.setColor(Color.BLACK);
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextSize(18); // (Config.getDensity()+1)/2

		mPopupPaint = new Paint();
		mPopupPaint.setTextAlign(Align.CENTER);
		mPopupPaint.setColor(Color.BLACK);
		mPopupPaint.setAntiAlias(true);
		mPopupPaint.setTextSize(MapView.MAPTEXT.getTextsize()/** (Config.getDensity()+1)/2 */
		);

	}

	@Override
	public void onDraw(Canvas canvas) {

		mPath.reset();// 重绘路线图
		for (int i = 0; i < mPointList.size(); i++) {
			RMPoi p = mPointList.get(i);
			PointInfo temppoi = mMapView.fromLocation(new Location(p.getX(), p
					.getY()));

			float pointX = temppoi.getX() - mPointIcon.getWidth() / 2.0f;// 实际点的x
			float pointY = temppoi.getY() - mPointIcon.getHeight() / 2.0f;// 实际点的y
			canvas.drawBitmap(mPointIcon, pointX, pointY, null);// 画出点
			if (!DTStringUtils.isEmpty(p.getName()))
				canvas.drawText(p.getName(), pointX,
						pointY + mPointIcon.getWidth() + 20, mTextPaint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}

	@Override
	public void destroyLayer() {

	}
	private float downX, downY;
	@Override
	public boolean onTap(MotionEvent e) {
		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			downX = e.getX();
			downY = e.getY();
		} else if (e.getAction() == MotionEvent.ACTION_UP
				|| e.getAction() == MotionEvent.ACTION_CANCEL) {// 当手指抬起时的
			DTLog.e("e.getX : " + e.getX() + "   e.getY : " + e.getY());
			RMPoi clickPoint = null;
			String key = null;
			float p2p = -1;// 触点与地图上的点两个点之间的距离
			if (Math.abs(e.getX() - downX) < 20
					&& Math.abs(e.getY() - downY) < 20) {// 如果按下与抬起距离在20像素范围内，可视为点击
			for (int i = 0; i < mPointList.size(); i++) {
				RMPoi p = mPointList.get(i);
				PointInfo temppoi = mMapView.fromLocation(new Location(p.getX(), p
						.getY()));
				if (temppoi.getX() < 0 || temppoi.getY() < 0)// 屏幕外的不用计算
					continue;
				float reduceX = Math.abs(temppoi.getX() - e.getX());
				float reduceY = Math.abs(temppoi.getY() - e.getY());
				if (reduceX > 20 || reduceY > 20)// 超出手指同一水平线范围
					continue;
				float dis = DTMathUtils.distance(e.getX(), e.getY(),
						temppoi.getX(), temppoi.getY());// 计算两点之间的距离
				if (p2p < 0 || p2p > dis) {// 距离比他大
					clickPoint = p;// 保存距离范围内的点
					p2p = dis;
				}
			}
			if (p2p > -1 && mClickListener != null) {// 说明点击在点的范围内
				DTLog.e("点击事件");
				mClickListener.onClick(clickPoint, key);
			}
			}
		}
		return false;
	}

	/**
	 * 清空所有路线list
	 */
	@Override
	public void clearLayer() {
		mPointList.clear();
		// mMapView.getTapPOILayer().setDisableTap(false);
		mMapView.popuindex = 0;
	}

	/**
	 * 是否有数据
	 */
	@Override
	public boolean hasData() {
		return (mPointList != null && mPointList.size() != 0);
	}

	public static interface OnPopupPositionChangedListener {
		public void onPopupPositionChanged(int position);
	}

	public static interface OnFloorChangedListener {
		public void onFloorChanged(String floor);
	}

	public static interface OnIsEndListener {
		public void onisend();
	}

	public void setRoutePaint(Paint mPaint) {
		mRoutePaint = mPaint;
	}

	public void setOtherFloorRoutePaint(Paint mPaint) {
		mRoutePaint2 = mPaint;
	}

}
