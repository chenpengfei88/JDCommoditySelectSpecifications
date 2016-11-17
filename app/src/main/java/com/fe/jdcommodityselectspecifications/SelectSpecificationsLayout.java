package com.fe.jdcommodityselectspecifications;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by chenpengfei on 2016/11/11.
 */
public class SelectSpecificationsLayout extends LinearLayout {

    private Matrix mTopMatrix;
    private int offset = 40;
    //动画时长
    private int duration = 150;
    public View mTopView;
    public View mBottomView;
    private float[] src = new float[8];
    private float[] dst = new float[8];
    private int mTopWidth, mTopHeight;
    private boolean init = true;

    /**
     * TopView的不同状态
     */
    private static final int STATUS_ONE = 1;
    private static final int STATUS_TWO = 2;
    private static final int STATUS_THREE = 3;
    private static final int STATUS_FOUR = 4;

    public SelectSpecificationsLayout(Context context) {
        super(context);
    }

    public SelectSpecificationsLayout(final Context context, AttributeSet attrs) {
        super(context, attrs);
        mTopMatrix = new Matrix();
    }

    public SelectSpecificationsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SelectSpecificationsLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTopView = getChildAt(0);
        mBottomView = getChildAt(1);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mTopWidth = mTopView.getWidth();
        mTopHeight = mTopView.getHeight();
    }

    public void moveView() {
        init = false;
        initTopViewLocation();
        startAnimation(STATUS_ONE);
    }

    public void resetView() {
        startAnimation(STATUS_THREE);
    }

    private void initTopViewLocation() {
        setTopViewSrcLocation(0, 0, mTopWidth, 0, mTopWidth, mTopHeight, 0, mTopHeight);
    }

    private void setTopViewSrcLocation(
                                       float topLeftX, float topLeftY, float topRightX, float topRightY, float bottomRightX,
                                       float bottomRightY, float bottomLeftX, float bottomLeftY) {
        src[0] = topLeftX;
        src[1] = topLeftY;
        src[2] = topRightX;
        src[3] = topRightY;
        src[4] = bottomRightX;
        src[5] = bottomRightY;
        src[6] = bottomLeftX;
        src[7] = bottomLeftY;
    }

    private void setTopViewDstLocation(
                                        float topLeftX, float topLeftY, float topRightX, float topRightY, float bottomRightX,
                                        float bottomRightY, float bottomLeftX, float bottomLeftY) {
        dst[0] = topLeftX;
        dst[1] = topLeftY;
        dst[2] = topRightX;
        dst[3] = topRightY;
        dst[4] = bottomRightX;
        dst[5] = bottomRightY;
        dst[6] = bottomLeftX;
        dst[7] = bottomLeftY;
    }

    private void setViewLocation(int status, float moveOffset) {
        float leftTopX = 0, leftTopY = 0, rightTopX = 0, rightTopY = 0, rightBottomX = 0, rightBottomY = 0, leftBottomX = 0, leftBottomY = 0;
        switch (status) {
            case STATUS_ONE:
                leftTopX = leftTopY = moveOffset;
                rightTopX =  mTopWidth - moveOffset;
                rightTopY = moveOffset;
                rightBottomX = mTopWidth;
                rightBottomY = mTopHeight;
                leftBottomX = 0;
                leftBottomY = rightBottomY;
                break;
            case STATUS_TWO:
                leftTopX = leftTopY = offset;
                rightTopX = mTopWidth - offset;
                rightTopY = offset;
                rightBottomX = mTopWidth - moveOffset;
                rightBottomY = mTopHeight - moveOffset;
                leftBottomX = moveOffset;
                leftBottomY = rightBottomY;
                break;
            case STATUS_THREE:
                leftTopX = leftTopY = offset;
                rightTopX = mTopWidth - offset;
                rightTopY = offset;
                rightBottomX = mTopWidth - offset + moveOffset;
                rightBottomY = mTopHeight - offset + moveOffset;
                leftBottomX = offset - moveOffset;
                leftBottomY = rightBottomY;
                break;
            case STATUS_FOUR:
                leftTopX = leftTopY = (offset - moveOffset);
                rightTopX = mTopWidth - offset + moveOffset;
                rightTopY = offset - moveOffset;
                rightBottomX = mTopWidth;
                rightBottomY = mTopHeight;
                leftBottomX = 0;
                leftBottomY = rightBottomY;
                break;
        }
        setTopViewDstLocation(leftTopX, leftTopY, rightTopX, rightTopY, rightBottomX, rightBottomY, leftBottomX, leftBottomY);
    }

    private void startAnimation(final int status) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float moveOffset = ((float) animation.getAnimatedValue() * offset);
                setViewLocation(status, moveOffset);
                postInvalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(status == STATUS_ONE || status == STATUS_THREE) {
                    startAnimation(status == STATUS_ONE ? STATUS_TWO : STATUS_FOUR);
                }
            }
        });
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if(!init) {
            canvas.save();
            mTopMatrix.reset();
            mTopMatrix.setPolyToPoly(src, 0, dst, 0, src.length >> 1);
            canvas.concat(mTopMatrix);
            drawChild(canvas, mTopView, getDrawingTime());
            canvas.restore();
            drawChild(canvas, mBottomView, getDrawingTime());
            return;
        }
        super.dispatchDraw(canvas);
    }
}
