package com.chn.srecyclerviewsample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.chn.srecyclerview.SRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SampleActivity extends AppCompatActivity {


    SRecyclerView sRecyclerView;
    MyAdapter myAdapter;
    List<String> datas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        int layoutId = getIntent().getIntExtra("layoutId",R.layout.activity_sample);

        setContentView(layoutId);
        datas = new ArrayList<>();
        myAdapter = new MyAdapter(this, datas);
        sRecyclerView = (SRecyclerView) findViewById(R.id.ac_sample_srv);
        sRecyclerView.setAdapter(myAdapter);
        sRecyclerView.setOnRequestListener(new SRecyclerView.OnRequestListener() {
            @Override
            public void onRefresh() {
                loadData(true);
            }

            @Override
            public void onLoadMore() {
                loadData(false);

            }

            @Override
            public void onStateClick() {
                loadData(true);

            }
        });
        loadData(true);

    }

    int count = 0;

    private void loadData(final boolean isRefresh) {
        sRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                int position = count % 6;
                Log.e("run", position + "");
                List<String> list = getNewList(myAdapter.getItemCount());
                switch (position) {
                    case 0:
                        if (isRefresh) {
                            datas.clear();
                            datas.addAll(list);
                            myAdapter.notifyDataSetChanged();
                        } else {
                            datas.addAll(list);
                            myAdapter.notifyItemRangeChanged(myAdapter.getItemCount() - list.size(), list.size());
                        }
                        sRecyclerView.setHadNextPage(true);
                        break;
                    case 1:
                        sRecyclerView.requestFail();
                        break;
                    case 2:
                        sRecyclerView.requestFail();
                        break;

                    case 3:
                        datas.clear();
                        myAdapter.notifyDataSetChanged();
                        sRecyclerView.setHadNextPage(false);

                        break;
                    case 4:
                        if (isRefresh) {
                            datas.clear();
                            datas.addAll(list);
                            myAdapter.notifyDataSetChanged();
                        } else {
                            datas.addAll(list);
                            myAdapter.notifyItemRangeChanged(myAdapter.getItemCount() - list.size(), list.size());
                        }
                        sRecyclerView.setHadNextPage(false);
                        break;
                    case 5:
                        if (isRefresh) {
                            datas.clear();
                            datas.addAll(list);
                            myAdapter.notifyDataSetChanged();
                        } else {
                            datas.addAll(list);
                            myAdapter.notifyItemRangeChanged(myAdapter.getItemCount() - list.size(), list.size());
                        }
                        sRecyclerView.setHadNextPage(true);

                        break;
                }
                count++;
            }
        }, 3000);

    }

    private List<String> getNewList(int start) {
        List<String> dataList = new ArrayList<>();
        for (int i = start; i < (10 + start); i++) {
            dataList.add(String.valueOf(i));
        }
        return dataList;
    }

}
