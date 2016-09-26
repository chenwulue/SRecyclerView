# SRecyclerView
一个提供上拉刷新，下拉加载更多，显示状态页（错误、空、加载中）的RecyclerView，且加载更多样式、状态页样式均可自定义，可直接在xml配置线性布局还是网格布局，以及网格布局的列个数
该项目参考了此项目 https://github.com/YougaKing/DragRecyclerView 与其说参考，不如说是再封装了一次，以及改了部分内容


使用方法：



    compile 'com.chn.srecyclerview:SRecyclerView:0.1'


xml里面直接引用：

默认为LinearLayoutManager VERTICAL 的样式
<com.chn.srecyclerview.SRecyclerView xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/ac_sample_srv"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />

设置网格样式
<com.chn.srecyclerview.SRecyclerView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/ac_sample_srv"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:srv_layoutManager="GridLayout"
    app:srv_spanCount="3" />
    
设置网格，且加载完成后有“已经到底了”的字样
<com.chn.srecyclerview.SRecyclerView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/ac_sample_srv"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:srv_layoutManager="GridLayout"
    app:srv_showOver="true"
    app:srv_spanCount="3" />
    
    
具体的自定义属性
        <!--是否显示“已经到底了”字样，默认为false-->
        <attr name="srv_showOver" format="boolean" />
        <!--自定义底部布局样式，里面分三块，id分别为srv_foot_over、srv_foot_loading、srv_foot_fail-->
        <attr name="srv_footLayoutId" format="reference" />
        <!--自定义满屏布局样式，里面分三块，id分别为srv_fill_progress、srv_fill_empty、srv_fill_fail-->
        <attr name="srv_fillLayoutId" format="reference" />
        <!--布局样式-->
        <attr name="srv_layoutManager" format="integer">
            <enum name="LinearLayout" value="1" />
            <enum name="GridLayout" value="2" />
            <enum name="StaggeredGridLayout" value="3" />
        </attr>
        <!--方向-->
        <attr name="srv_orientation" format="integer">
            <enum name="horizontal" value="0" />
            <enum name="vertical" value="1" />
        </attr>
        <!--列个数 当srv_layoutManager为GridLayout或者StaggeredGridLayout时有效-->
        <attr name="srv_spanCount" format="integer" />
        
代码中调用
设置Adapter： sRecyclerView.setAdapter(myAdapter);
设置监听：sRecyclerView.setOnRequestListener(new SRecyclerView.OnRequestListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
          

            }

            @Override
            public void onStateClick() {
           

            }
        });
网络访问结束后，如果没有网络原因，更新自己的adapter，再调用sRecyclerView.setHadNextPage(true); true表示有下一页
如果有网络原因，可以直接调用 sRecyclerView.requestFail();内部会判断最近的一次请求是下拉刷新还是加载更多还是点击错误页面发出的
也可以自己调用   refreshFail(); loadMoreFail(); stateFail();
