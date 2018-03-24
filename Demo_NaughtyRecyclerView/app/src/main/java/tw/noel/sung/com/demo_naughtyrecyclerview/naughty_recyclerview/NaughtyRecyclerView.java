package tw.noel.sung.com.demo_naughtyrecyclerview.naughty_recyclerview;

import android.content.Context;
import android.graphics.Matrix;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import tw.noel.sung.com.demo_naughtyrecyclerview.naughty_recyclerview.recyclerview.CustomRecyclerView;
import tw.noel.sung.com.demo_naughtyrecyclerview.naughty_recyclerview.util.LayoutSizeUtil;
import tw.noel.sung.com.demo_naughtyrecyclerview.naughty_recyclerview.view.ReLoadingView;

/**
 * Created by noel on 2018/3/24.
 */

public class NaughtyRecyclerView extends FrameLayout implements CustomRecyclerView.OnOverScrolledListener, CustomRecyclerView.OnStartReloadingListener {
    private ReLoadingView reLoadingView;
    private CustomRecyclerView recyclerView;
    private Context context;
    private LayoutSizeUtil layoutSizeUtil;
    private int phoneHeight;

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
    }
    //------------------

    /***
     *  ReLoadingView設定
     */
    private void initReLoadingView() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.bottomMargin = phoneHeight;
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        reLoadingView = new ReLoadingView(context);
        reLoadingView.setLayoutParams(layoutParams);
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
    public void OnOverScrolled(int scrollDistance) {
        if (scrollDistance < (phoneHeight / 3)) {
            reLoadingView.rotateImgLoading();

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) reLoadingView.getLayoutParams();
            params.bottomMargin -= 50;
            params.topMargin = scrollDistance;
            requestLayout();
        }
    }
    //------------------

    /***
     * 當手指放開時 進行Loading動畫
     */
    @Override
    public void OnStartReloading() {
        recyclerView.setLoading(true);
        reLoadingView.startLoading();
    }

    //------------------

    /***
     * 停止轉動
     */
    public void stopLoading() {
        recyclerView.setLoading(false);
        reLoadingView.stopAnimation();
    }
}
