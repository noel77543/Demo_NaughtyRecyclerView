package tw.noel.sung.com.demo_naughtyrecyclerview.naughty_recyclerview.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tw.noel.sung.com.demo_naughtyrecyclerview.R;

/**
 * Created by noel on 2018/3/24.
 */

public class ReLoadingView extends RelativeLayout {
    private TextView textLoading;
    //loading 的大小
    private final int LOADING_SIZE = 150;
    //文字大小
    private final int TEXT_SIZE = 20;
    //第幾個文字
    private int textIndex = 0;
    //文字長度
    private int textLength = 0;
    //文字放大倍率
    private final float TEXT_MAGNIFICATION = 1.5f;
    //文字行為重複間隔
    private final int DURATION = 250;
    private Runnable runnable;
    private Handler handler;

    private ImageView imgLoading;
    //轉圈動畫
    private Animation animation;
    //image動畫時間
    private final int ANIMATION_DURATION = 800;
    private Context context;
    private float rotateAngle = 360;


    public ReLoadingView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ReLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ReLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        initImageLoadingCircle();
        initLoadingText();
    }
    //-------------

    /***
     *  載入的圓圈圖片設定
     */
    private void initImageLoadingCircle() {
        imgLoading = new ImageView(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LOADING_SIZE, LOADING_SIZE);
        layoutParams.addRule(CENTER_HORIZONTAL);
        imgLoading.setLayoutParams(layoutParams);
        imgLoading.setBackgroundResource(R.drawable.shape_loading);
        imgLoading.setId(R.id.id_image_loading);
        addView(imgLoading);
    }
    //-------------

    /***
     *  載入的文字設定
     */
    private void initLoadingText() {

        textLoading = new TextView(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(BELOW, R.id.id_image_loading);
        layoutParams.addRule(CENTER_HORIZONTAL);
        textLoading.setText("Loading");
        textLoading.setTextSize(TEXT_SIZE);
        textLoading.setTextColor(context.getResources().getColor(android.R.color.tab_indicator_text));
        textLoading.setLayoutParams(layoutParams);
        addView(textLoading);

        textLength = textLoading.getText().toString().length();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                if (textIndex < textLength) {
                    textLoading.setText(getSpannedText(textIndex));
                    textIndex++;
                } else {
                    textIndex = 0;
                }
                handler.postDelayed(runnable, DURATION);
            }
        };
    }
    //----------------

    /***
     *  調整字串中部分字體大小 或者 顏色
     * @param strIndext
     * @return
     */
    private CharSequence getSpannedText(int strIndext) {
        SpannableStringBuilder builder = new SpannableStringBuilder(textLoading.getText().toString());
        builder.setSpan(new RelativeSizeSpan(TEXT_MAGNIFICATION), strIndext, strIndext + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }


    //---------------

    /***
     *  開始轉圈動畫
     */
    public void startLoading() {
        animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(ANIMATION_DURATION);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setInterpolator(new LinearInterpolator());
        imgLoading.setAnimation(animation);

        animation.startNow();
        handler.postDelayed(runnable, DURATION);
    }

    //--------------------

    /***
     * 停止所有動畫
     */
    public void stopAnimation() {
        handler.removeCallbacks(runnable);
        animation.cancel();
    }
    //--------------------

    /***
     *  在拖拉時 旋轉
     */
    public void rotateImgLoading() {
        imgLoading.setRotation(rotateAngle);
        rotateAngle -= 3;
    }
}
