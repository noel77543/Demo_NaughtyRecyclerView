package tw.noel.sung.com.demo_naughtyrecyclerview.naughty_recyclerview.recyclerview.layoutmanager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by noel on 2018/3/25.
 */

public class CustomLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}
