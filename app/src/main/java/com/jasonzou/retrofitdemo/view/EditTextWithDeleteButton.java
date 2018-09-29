package com.jasonzou.retrofitdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.jasonzou.retrofitdemo.R;


/**
 * 项目名称：无
 * 类描述：
 * 创建人：jasonzou
 * 创建时间：2017/8/6 08:46
 * 修改人：jasonzou
 * 修改时间：2017/8/6 08:46
 * 修改备注：
 */
public class EditTextWithDeleteButton extends AppCompatEditText {
    private final static String TAG = "EditTextWithDel";
    private Context mContext;
    private Drawable mDelImg;
    private int delImgWidth, delImgHeight;
    private TextWatcher mTextWatcher;


    public EditTextWithDeleteButton(Context context) {
        super(context);
        mContext = context;
        init(context, null);
    }

    public EditTextWithDeleteButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(context, attrs);
    }

    public EditTextWithDeleteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context, attrs);
    }

    private static int dip2px(Context context, int dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        super.addTextChangedListener(watcher);
        this.mTextWatcher = watcher;
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray mTypedArray = mContext.obtainStyledAttributes(attrs, R.styleable.EditTextWithDeleteButton);
        mDelImg = mTypedArray.getDrawable(R.styleable.EditTextWithDeleteButton_delImg);
        delImgWidth = mTypedArray.getDimensionPixelOffset(R.styleable.EditTextWithDeleteButton_delImgWidth, dip2px(context, 5));
        delImgHeight = mTypedArray.getDimensionPixelOffset(R.styleable.EditTextWithDeleteButton_delImgHeight, dip2px(context, 5));
        mTypedArray.recycle();


        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mTextWatcher != null) {
                    mTextWatcher.onTextChanged(s, start, before, count);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (mTextWatcher != null) {
                    mTextWatcher.beforeTextChanged(s, start, count, after);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
                if (mTextWatcher != null) {
                    mTextWatcher.afterTextChanged(s);
                }
            }
        });
    }

    //设置删除图片
    private void setDrawable() {
        if (length() > 0)
            setCompoundDrawables(null, null, mDelImg, null);
        else
            setCompoundDrawables(null, null, null, null);

    }

    // 处理删除事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDelImg != null && event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            Log.e(TAG, "eventX = " + eventX + "; eventY = " + eventY);
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - dip2px(mContext, 20) - getPaddingRight();//响应点击的区域 20dp
            rect.right -= getPaddingRight();
            if (rect.contains(eventX, eventY))
                setText("");
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mDelImg != null) {
            setCompoundDrawablePadding(dip2px(mContext, 10));
            mDelImg.setBounds(0, 0, dip2px(mContext, delImgWidth), dip2px(mContext, delImgHeight));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setDrawable();
    }
}
