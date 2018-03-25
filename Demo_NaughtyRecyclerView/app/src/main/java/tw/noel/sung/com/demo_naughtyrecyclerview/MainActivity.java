package tw.noel.sung.com.demo_naughtyrecyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.noel.sung.com.demo_naughtyrecyclerview.naughty_recyclerview.NaughtyRecyclerView;
import tw.noel.sung.com.demo_naughtyrecyclerview.naughty_recyclerview.implement.OnReLoadingListener;
import tw.noel.sung.com.demo_naughtyrecyclerview.test.adapter.PeopleAdapter;
import tw.noel.sung.com.demo_naughtyrecyclerview.test.model.People;

public class MainActivity extends AppCompatActivity implements OnReLoadingListener {

    @BindView(R.id.naughty_recycler_view)
    NaughtyRecyclerView naughtyRecyclerView;

    private PeopleAdapter adapter;
    private ArrayList<People> peoples;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        test();
        naughtyRecyclerView.setOnReLoadingListener(this);

    }

    private void test() {
        adapter = new PeopleAdapter(this);
        peoples = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            People people = new People();
            people.setName("Noel");
            people.setWeight(65);
            people.setHeight(176);
            people.setAge(25);
            peoples.add(people);
        }
        adapter.setData(peoples);
        naughtyRecyclerView.setAdapter(adapter);
    }

    /***
     *  當Loading
     */
    @Override
    public void onStartReLoading() {
        Log.e("a", "start");
        peoples.clear();
        for (int i = 0; i < 25; i++) {
            People people = new People();
            people.setName("健身去~目標3公里");
            people.setWeight(66);
            people.setHeight(666);
            people.setAge(66);
            peoples.add(people);
        }
    }

    /***
     * 當結束Reloading
     */
    @Override
    public void onStopReLoading() {
        Log.e("a", "stop");
        adapter.setData(peoples);
    }
}
