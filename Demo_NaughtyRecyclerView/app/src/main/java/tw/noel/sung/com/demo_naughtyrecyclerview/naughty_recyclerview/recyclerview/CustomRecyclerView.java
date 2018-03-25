package tw.noel.sung.com.demo_naughtyrecyclerview.naughty_recyclerview.recyclerview;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import tw.noel.sung.com.demo_naughtyrecyclerview.naughty_recyclerview.recyclerview.layoutmanager.CustomLinearLayoutManager;

/**
 * Created by noel on 2018/3/24.
 */

public class CustomRecyclerView extends RecyclerView {

    //手指移動方向 向上 向下
    public static final int ROLL_UP = 9487;
    public static final int ROLL_DOWN = 7777;

    @IntDef({ROLL_UP, ROLL_DOWN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RollDirection {

    }

    //滾動方向
    private int rollDirection;

    private OnOverScrolledListener onOverScrolledListener;
    private OnStartReloadingListener onStartReloadingListener;
    private OnPauseReloadingListener onPauseReloadingListener;
    private CustomLinearLayoutManager customLinearLayoutManager;
    private Context context;
    //是否動畫進行中
    private boolean isLoading;
    //用以判斷是否滾動至邊界
    private boolean isOverScrolled;
    //用來鎖定 抓取開始OverScrolled的Y座標
    private boolean isStartCalculate = true;
    //觸發距離
    private final int MIN_DISTANCE = 500;
    private int lastY;
    private int lastMoveY = 0;

    public CustomRecyclerView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    //-------------------

    /***
     *  init
     */
    private void init() {
        //隱藏邊界陰影
        setOverScrollMode(OVER_SCROLL_NEVER);
        setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        customLinearLayoutManager = new CustomLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        setLayoutManager(customLinearLayoutManager);
        initListener();
    }
    //---------------

    /***
     *  滾動監聽
     */
    private void initListener() {
        addOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 滾至上方邊界
                if (!recyclerView.canScrollVertically(-1)) {
                    isOverScrolled = true;
                }
                //往下滾
                else if (dy > 0) {
                    isOverScrolled = false;
                }
            }
        });
    }
    //---------------

    /***
     *   setAdapter
     * @param adapter
     */
    public void setNaughtyRecyclerViewAdapter(RecyclerView.Adapter adapter) {
        setAdapter(adapter);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isLoading) {
            int y = (int) ev.getY();
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastY = y;
                    break;
                //當手指移動
                case MotionEvent.ACTION_MOVE:

                    //如果往下滑的話關閉isOverScrolled
                    if (lastY > y) {
                        isOverScrolled = false;
                    }
                    //當滾至邊界時 isOverScrolled會是true 這時候開始將ReLoadView由上而下出現(增加其marginTop)
                    if (isOverScrolled) {
                        //當滾至邊界 不允許recyclerview滾動
                        customLinearLayoutManager.setScrollEnabled(false);
                        //取得滾至邊界那瞬間的Y座標 並且設為lastY然後鎖定
                        if (isStartCalculate) {
                            lastY = y;
                            isStartCalculate = false;
                        }
                        Log.e("y", "" + y);
                        Log.e("lastY", "" + lastY);

                        rollDirection = y > lastMoveY ? ROLL_DOWN : ROLL_UP;
                        onOverScrolledListener.OnOverScrolled((y - lastY), rollDirection);
                        //在開始拖拉圓圈的時候 記錄每一個上一次的Y座標 以便下一刻進入MotionEvent.ACTION_MOVE判斷拖拉方向
                        lastMoveY = y;
                    }
                    break;
                //當手指抬起
                case MotionEvent.ACTION_UP:
                    if (!isStartCalculate && y - lastY > MIN_DISTANCE) {
                        onStartReloadingListener.onStartReloading();
                    } else {
                        onPauseReloadingListener.onPauseReloading();
                    }
                    //解綁
                    isStartCalculate = true;
                    //當手指抬起 允許recyclerview滾動
                    customLinearLayoutManager.setScrollEnabled(true);
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    //---------------

    public interface OnOverScrolledListener {
        void OnOverScrolled(int scrollDistance, @RollDirection int rollDirection);
    }

    public void setOnOverScrolledListener(OnOverScrolledListener onOverScrolledListener) {
        this.onOverScrolledListener = onOverScrolledListener;
    }

    //---------------

    /***
     * 當拖拉距離達成條件時
     */
    public interface OnStartReloadingListener {
        void onStartReloading();
    }

    public void setOnStartReloadingListener(OnStartReloadingListener onStartReloadingListener) {
        this.onStartReloadingListener = onStartReloadingListener;
    }
    //---------------------

    /***
     *  當拖拉距離不足時
     */
    public interface OnPauseReloadingListener {
        void onPauseReloading();
    }

    public void setOnPauseReloadingListener(OnPauseReloadingListener onPauseReloadingListener) {
        this.onPauseReloadingListener = onPauseReloadingListener;
    }


    //---------------

    /***
     * 開關
     * @return
     */
    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
