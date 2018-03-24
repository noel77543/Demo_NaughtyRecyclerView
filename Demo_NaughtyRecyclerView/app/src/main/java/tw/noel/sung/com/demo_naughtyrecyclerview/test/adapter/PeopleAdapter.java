package tw.noel.sung.com.demo_naughtyrecyclerview.test.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.noel.sung.com.demo_naughtyrecyclerview.R;
import tw.noel.sung.com.demo_naughtyrecyclerview.test.model.People;

/**
 * Created by noel on 2018/3/24.
 */

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {

    private ArrayList<People> peoples;
    private LayoutInflater inflater;
    private OnMyItemClickedListener onMyItemClickedListener;

    public PeopleAdapter(Context context) {
        peoples = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<People> peoples) {
        this.peoples = peoples;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.list_people, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        People people = peoples.get(position);
        holder.textView.setText(people.toString());
    }


    @Override
    public int getItemCount() {
        return peoples.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view)
        TextView textView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onMyItemClickedListener != null){
                        onMyItemClickedListener.onMyItemClicked();
                    }
                }
            });
        }
    }

    //-------------
    public interface OnMyItemClickedListener {
        void onMyItemClicked();
    }

    public void setOnMyItemClickedListener(OnMyItemClickedListener onMyItemClickedListener) {
        this.onMyItemClickedListener = onMyItemClickedListener;
    }

}
