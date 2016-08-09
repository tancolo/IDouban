package com.shrimpcolo.johnnytam.idouban.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhy on 15/8/20.
 * https://github.com/hongyangAndroid/MixtureTextView
 */
public class MixtureTextView extends RelativeLayout {

    private Layout layout = null;

    /**
     * 行高
     */
    private int mLineHeight;

    private int mTextColor = Color.BLACK;
    private int mTextSize = sp2px(14);
    private String mText;

    private int mLineSpace;

    private TextPaint mTextPaint;

    private List<List<Rect>> mDestRects = new ArrayList<List<Rect>>();
    private List<Integer> mCorYs = null;
    private HashSet<Integer> mCorYHashes = new HashSet<Integer>();

    private int mMaxHeight;
    private int mHeightMeasureSpec;
    private int mOriginHeightMeasureMode;
    private int mHeightReMeasureSpec;
    private boolean mNeedReMeasure;
    private boolean mNeedRenderText;

    private int mMinHeight;


    private static int[] ATTRS = new int[]{
            android.R.attr.textSize,//16842901
            android.R.attr.textColor,//16842904
            android.R.attr.text//16843087
    };

    private static final int INDEX_ATTR_TEXT_SIZE = 0;
    private static final int INDEX_ATTR_TEXT_COLOR = 1;
    private static final int INDEX_ATTR_TEXT = 2;

    private Map<Integer, Point> mViewBounds = new HashMap<Integer, Point>();


    public MixtureTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        readAttrs(context, attrs);

        //get text
        if (!TextUtils.isEmpty(mText)) {
            mNeedRenderText = true;
        }

        if (!mNeedRenderText) return;


        mTextPaint = new TextPaint();
        mTextPaint.setDither(true);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
    }

    private void readAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, ATTRS);
        mTextSize = ta.getDimensionPixelSize(INDEX_ATTR_TEXT_SIZE, mTextSize);
        mTextColor = ta.getColor(INDEX_ATTR_TEXT_COLOR, mTextColor);
        mText = ta.getString(INDEX_ATTR_TEXT);
        ta.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (!mNeedRenderText) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        mHeightMeasureSpec = heightMeasureSpec;
        mTextPaint.setTextSize(mTextSize);

        cacuLineHeight();

        if (mNeedReMeasure) {
            super.onMeasure(widthMeasureSpec, mHeightReMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }

    private void cacuLineHeight() {
        layout = new StaticLayout("爱我中华", mTextPaint, 0, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0f, false);
        mLineHeight = layout.getLineBottom(0) - layout.getLineTop(0);
    }


    private boolean mFirstInLayout = true;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mFirstInLayout) {
            mOriginHeightMeasureMode = MeasureSpec.getMode(mHeightMeasureSpec);
            mFirstInLayout = false;
            mMinHeight = getMeasuredHeight();
        }

        super.onLayout(changed, l, t, r, b);

        if (!mNeedRenderText) {
            return;
        }

        getAllYCors();
    }


    private boolean tryDraw(Canvas canvas) {
        boolean kidding = canvas == null;
        int lineHeight = mLineHeight;
        List<List<Rect>> destRects = mDestRects;

        int start = 0;
        int lineSum = 0;
        int fullSize = mText.length();
        for (int i = 0; i < destRects.size(); i++) {
            List<Rect> rs = destRects.get(i);
            Rect r = rs.get(0);
            int rectWidth = r.width();
            int rectHeight = r.height();
            layout = generateLayout(mText.substring(start), rectWidth);
            int lineCount = rectHeight / lineHeight;
            lineCount = layout.getLineCount() < lineCount ? layout.getLineCount() : lineCount;
            if (!kidding) {
                canvas.save();
                canvas.translate(r.left, r.top);
                canvas.clipRect(0, 0, r.width(), layout.getLineBottom(lineCount - 1) - layout.getLineTop(0));
                layout.draw(canvas);
                canvas.restore();
            }
            start += layout.getLineEnd(lineCount - 1);
            lineSum += lineCount;
            if (start >= fullSize) {
                break;
            }
        }


        if (kidding) {
            mMaxHeight += lineSum * lineHeight;

            if ((mMaxHeight > mMinHeight && getHeight() != mMaxHeight) && mOriginHeightMeasureMode != MeasureSpec.EXACTLY) {
                mHeightReMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.EXACTLY);
                mNeedReMeasure = true;
                requestLayout();

                return true;
            }
        }

        return false;

    }

    /**
     * 获取所有的y坐标
     */
    private void getAllYCors() {
        int lineHeight = mLineHeight;

        Set<Integer> corYSet = mCorYHashes;
        corYSet.clear();
        mViewBounds.clear();

        //获得所有的y轴坐标
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View c = getChildAt(i);
            if (c.getVisibility() == View.GONE) continue;

            int top = c.getTop();
            int availableTop = c.getTop() - getPaddingTop();
            availableTop = availableTop / lineHeight * lineHeight;
            top = availableTop + getPaddingTop();

            corYSet.add(top);

            int bottom = c.getBottom();
            int availableBottom = bottom - getPaddingTop();
            availableBottom = availableBottom % lineHeight == 0 ? availableBottom : (availableBottom / lineHeight + 1) * lineHeight;
            bottom = availableBottom + getPaddingTop();

            corYSet.add(bottom);

            mViewBounds.put(i, new Point(top, bottom));
        }
        corYSet.add(getPaddingTop());

        if (mOriginHeightMeasureMode == MeasureSpec.EXACTLY) {
            corYSet.add(getHeight());
        } else {
            corYSet.add(Integer.MAX_VALUE);
        }
        //排序
        List<Integer> corYs = new ArrayList<Integer>(corYSet);
        Collections.sort(corYs);

        mCorYs = corYs;

    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        mMaxHeight = getPaddingBottom() + getPaddingTop();
        initAllNeedRenderRect();
        boolean skipDraw = tryDraw(null);
        if (skipDraw) return;
        tryDraw(canvas);
        super.dispatchDraw(canvas);
    }


    private void initAllNeedRenderRect() {
        int lineHeight = mLineHeight;
        List<List<Rect>> destRects = this.mDestRects;
        List<Integer> corYs = mCorYs;
        destRects.clear();

        int minLeft = getPaddingLeft();
        int maxRight = getWidth() - getPaddingRight();

        //find rect between y1 and y2
        List<Rect> viewRectBetween2Y = null;
        for (int i = 0; i < corYs.size() - 1; i++) {
            int y1 = corYs.get(i);
            int y2 = corYs.get(i + 1);

            viewRectBetween2Y = new ArrayList<Rect>();

            List<Rect> rs = caculateViewYBetween(y1, y2);


            Rect leftFirst = null;
            switch (rs.size()) {
                case 0:
                    viewRectBetween2Y.add(new Rect(minLeft, y1, maxRight, y2));
                    break;
                case 1:
                    leftFirst = rs.get(0);
                    //添加第一个Rect
                    tryAddFirst(leftFirst, viewRectBetween2Y, y1, y2, minLeft);
                    tryAddLast(leftFirst, viewRectBetween2Y, y1, y2, maxRight);
                    break;
                default:
                    //add first
                    leftFirst = rs.get(0);
                    tryAddFirst(leftFirst, viewRectBetween2Y, y1, y2, minLeft);
                    //add mid
                    for (int j = 0; j < rs.size() - 1; j++) {
                        Rect ra = rs.get(j);
                        Rect rb = rs.get(j + 1);

                        if (ra.right < rb.left)
                            viewRectBetween2Y.add(new Rect(ra.right, y1, rb.left, y2));
                    }
                    //add last
                    Rect lastRect = rs.get(rs.size() - 1);
                    tryAddLast(lastRect, viewRectBetween2Y, y1, y2, maxRight);
                    break;
            }
            destRects.add(viewRectBetween2Y);
        }

        //split
        List<List<Rect>> bak = new ArrayList<List<Rect>>(destRects);
        int destRectSize = destRects.size();
        int inc = 0;//索引增量
        for (int i = 0; i < destRectSize; i++) {
            List<Rect> rs = destRects.get(i);
            if (rs.size() > 1) {
                int index = inc + i;
                bak.remove(rs);
                inc--;
                Rect rect1 = rs.get(0);
                int lh = rect1.height() / lineHeight;
                mMaxHeight -= lh * (rs.size() - 1) * lineHeight;
                for (int k = 0; k < lh; k++) {
                    for (int j = 0; j < rs.size(); j++) {
                        inc++;
                        bak.add(index++, Arrays.asList(new Rect(
                                rs.get(j).left,
                                rect1.top + lineHeight * k,
                                rs.get(j).right,
                                rect1.top + lineHeight * k + lineHeight)));
                    }
                }

            }
        }
        mDestRects = bak;
    }

    private void tryAddLast(Rect leftFirst, List<Rect> viewRectBetween2Y, int y1, int y2, int maxRight) {
        if (leftFirst.right < maxRight) {
            viewRectBetween2Y.add(new Rect(leftFirst.right, y1, maxRight, y2));
        }
    }

    private void tryAddFirst(Rect leftFirst, List<Rect> viewRectBetween2Y, int y1, int y2, int minLeft) {
        if (leftFirst.left > minLeft) {
            viewRectBetween2Y.add(new Rect(minLeft, y1, leftFirst.left, y2));
        }
    }

    private StaticLayout generateLayout(String text, int width) {
        return new StaticLayout(text, mTextPaint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0f, false);
    }

    public void setText(String text) {
        if (TextUtils.isEmpty(text)) {
            mNeedRenderText = false;
            requestLayout();
            return;
        }
        mNeedRenderText = true;
        mText = text;
        requestLayout();
        invalidate();
    }

    public void setTextColor(int color) {
        mTextPaint.setColor(color);
        mTextColor = color;
        invalidate();
    }

    public void setTextSize(int unit, int size) {
        switch (unit) {
            case TypedValue.COMPLEX_UNIT_PX:
                mTextSize = size;
                break;
            case TypedValue.COMPLEX_UNIT_DIP:
                mTextSize = dp2px(size);
                break;
            case TypedValue.COMPLEX_UNIT_SP:
                mTextSize = sp2px(size);
                break;
        }
        mTextPaint.setTextSize(mTextSize);
        requestLayout();
        invalidate();
    }

    public void setTextSize(int pxSize) {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, pxSize);
    }

    /**
     * 计算包含在y1到y2间的矩形区域
     *
     * @param y1
     * @param y2
     * @return
     */
    private List<Rect> caculateViewYBetween(int y1, int y2) {
        List<Rect> rs = new ArrayList<>();
        Rect tmp = null;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);

            Point p = mViewBounds.get(i);
            int top = p.x;
            int bottom = p.y;

            if (top <= y1 && bottom >= y2) {
                tmp = new Rect(v.getLeft(), y1, v.getRight(), y2);
                rs.add(tmp);
            }
        }


        //TODO ADD
        Collections.sort(rs, new Comparator<Rect>() {
            @Override
            public int compare(Rect lhs, Rect rhs) {
                if (lhs.left > rhs.left) return 1;
                return -1;
            }
        });


        if (rs.size() >= 2) {
            List<Rect> res = new ArrayList<Rect>(rs);
            Rect pre = rs.get(0), next = rs.get(1);
            //合并
            for (int i = 1; i < rs.size(); i++) {
                //if相交
                if (Rect.intersects(pre, next)) {
                    int left = Math.min(pre.left, next.left);
                    int right = Math.max(pre.right, next.right);

                    res.remove(pre);
                    res.remove(next);
                    res.add(pre = new Rect(left, y1, right, y2));
                } else {
                    pre = next;
                }
                next = rs.get(i);
            }

            rs = res;
        }
        return rs;
    }


    public int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }

    public int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());

    }


    public int getTextSize() {
        return mTextSize;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public String getText() {
        return mText;
    }

}
