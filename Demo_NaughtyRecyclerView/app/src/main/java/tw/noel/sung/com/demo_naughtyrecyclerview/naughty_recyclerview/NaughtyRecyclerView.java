package tw.noel.sung.com.demo_naughtyrecyclerview.naughty_recyclerview;

import android.content.Context;
import android.graphics.Matrix;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import tw.noel.sung.com.demo_naughtyrecyclerview.naughty_recyclerview.implement.OnReLoadingListener;
import tw.noel.sung.com.demo_naughtyrecyclerview.naughty_recyclerview.recyclerview.CustomRecyclerView;
import tw.noel.sung.com.demo_naughtyrecyclerview.naughty_recyclerview.util.LayoutSizeUtil;
import tw.noel.sung.com.demo_naughtyrecyclerview.naughty_recyclerview.view.ReLoadingView;

/**
 * Created by noel on 2018/3/24.
 */

public class NaughtyRecyclerView extends FrameLayout implements CustomRecyclerView.OnOverScrolledListener, CustomRecyclerView.OnStartReloadingListener, CustomRecyclerView.OnPauseReloadingListener {
    //遞增marginBottom的週期時間
    private int animationTime;
    private FrameLayout.LayoutParams params;
    private boolean isStop;
    private Handler animationHandler;

    private ReLoadingView reLoadingView;
    private CustomRecyclerView recyclerView;
    private Context context;
    private LayoutSizeUtil layoutSizeUtil;
    private int phoneHeight;
    private OnReLoadingListener onReLoadingListener;

    public NaughtyRecyclerView(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    public NaughtyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public NaughtyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    //-------------------
    private void init() {
        setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layoutSizeUtil = new LayoutSizeUtil(context);
        animationHandler = new Handler();
        phoneHeight = layoutSizeUtil.getPhoneSize()[1];
        initRecyclerView();
        initReLoadingView();

        addViews();
    }
    //------------------

    /***
     *  recyclerview設定
     */
    private void initRecyclerView() {
        recyclerView = new CustomRecyclerView(context);
        recyclerView.setOnOverScrolledListener(this);
        recyclerView.setOnStartReloadingListener(this);
        recyclerView.setOnPauseReloadingListener(this);
    }
    //------------------

    /***
     *  ReLoadingView設定
     */
    private void initReLoadingView() {
        params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = phoneHeight;
        params.gravity = Gravity.CENTER_HORIZONTAL;
        reLoadingView = new ReLoadingView(context);
        reLoadingView.setLayoutParams(params);

        reLoadingView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLoading();
            }
        });
    }


    //------------------

    /***
     *  加入 View
     */
    private void addViews() {
        addView(recyclerView);
        addView(reLoadingView);
    }

    //------------------

    /***
     *  對CustomRecyclerView 進行 setAdapter
     * @param adapter
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setNaughtyRecyclerViewAdapter(adapter);
    }
    //------------------

    /***
     * 當滾動至邊界
     * 拖拉的同時使Loading圓圈逆時鐘旋轉
     * 且依照滾動距離設為ReloadView的topMargin 而bottomMargin需定值減少
     * 並且限制以上行為在滾動距離為手機螢幕高度三分之一時停止
     * @param scrollDistance
     */
    @Override
    public void OnOverScrolled(int scrollDistance, int rollDirection) {
        if (scrollDistance < (phoneHeight / 3)) {
            switch (rollDirection) {
                //手指往上
                case CustomRecyclerView.ROLL_UP:
                    reLoadingView.rotateClockWise();
                    params.bottomMargin += scrollDistance;
                    break;
                //手指往下
                case CustomRecyclerView.ROLL_DOWN:
                    reLoadingView.rotateCounterClockWise();
                    params.bottomMargin -= scrollDistance;
                    break;
            }

            params.topMargin = scrollDistance;
            requestLayout();
        }
    }
    //------------------

    /***
     * 當手指放開時 拖拉距離滿足時 進行Loading動畫
     */
    @Override
    public void onStartReloading() {
        recyclerView.setLoading(true);
        reLoadingView.startLoading();
        if (onReLoadingListener != null) {
            onReLoadingListener.onStartReLoading();
        }
    }
    //------------------

    /***
     * 當手指放開時 拖拉距離不足時 回彈 ReLoadingView
     */
    @Override
    public void onPauseReloading() {
        stopLoading();
    }
    //------------------

    /***
     * 停止轉動
     */
    public void stopLoading() {
        recyclerView.setLoading(false);
        reLoadingView.stopAnimation();

        animationTime = 100;
        animationRun.run();

        if (onReLoadingListener != null) {
            onReLoadingListener.onStopReLoading();
        }
    }

    //------------------------
    /***
     *  固定每次增減 50 , 而時間間隔持續除以10, 使增減 50 的行為越來越快
     */
    Runnable animationRun = new Runnable() {
        @Override
        public void run() {
            animationTime /= 10;
            if (params.topMargin - 30 < 0) {
                params.topMargin = 0;
                params.bottomMargin = phoneHeight;
                isStop = true;
            } else {
                params.topMargin -= 30;
                isStop = false;
            }
            requestLayout();

            if (isStop) {
                animationHandler.removeCallbacks(animationRun);
            } else {
                animationHandler.postDelayed(this, animationTime);
            }
        }
    };


    //------------------

    /***
     *  對外接口 開始Reloading 及 停止Loading
     */
    public void setOnReLoadingListener(OnReLoadingListener onReLoadingListener) {
        this.onReLoadingListener = onReLoadingListener;
    }
}
