package tw.noel.sung.com.demo_naughtyrecyclerview.naughty_recyclerview.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by noel on 2018/3/24.
 */

public class CustomRecyclerView extends RecyclerView {
    private OnOverScrolledListener onOverScrolledListener;
    private OnStartReloadingListener onStartReloadingListener;

    private Context context;
    //是否動畫進行中
    private boolean isLoading;
    //用以判斷是否滾動至邊界
    private boolean isOverScrolled;
    //用來鎖定 抓取開始OverScrolled的Y座標
    private boolean isStartCalculate;
    //觸發距離
    private final int MIN_DISTANCE = 200;

    private int lastY;

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
        setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
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
                        //取得滾至邊界那瞬間的Y座標 並且設為lastY然後鎖定
                        if (!isStartCalculate) {
                            isStartCalculate = true;
                            lastY = y;
                        }
                        onOverScrolledListener.OnOverScrolled(y - lastY);
                    }
                    break;
                //當手指抬起
                case MotionEvent.ACTION_UP:
                    //解綁
                    isStartCalculate = false;
                    if (y - lastY > MIN_DISTANCE) {
                        onStartReloadingListener.OnStartReloading();
                    }
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    //---------------

    public interface OnOverScrolledListener {
        void OnOverScrolled(int scrollDistance);
    }

    public void setOnOverScrolledListener(OnOverScrolledListener onOverScrolledListener) {
        this.onOverScrolledListener = onOverScrolledListener;
    }

    //---------------
    public interface OnStartReloadingListener {
        void OnStartReloading();
    }

    public void setOnStartReloadingListener(OnStartReloadingListener onStartReloadingListener) {
        this.onStartReloadingListener = onStartReloadingListener;
    }
    //---------------

    /***
     * 開關
     * @return
     */
    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
