package tan.myapplication.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import tan.myapplication.Adapter.MyAdapter;
import tan.myapplication.Adapter.MyAdapter2;
import tan.myapplication.Data.Boring_info;
import tan.myapplication.Data.Busy_info;
import tan.myapplication.R;

/**
 * 创建者：TAN
 * 创建时间： 2016/5/12.
 */
public class fragment2 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    public Context context;
    Button btn_publish_busy;
    ListView listView_busy;
    SwipeRefreshLayout SF;

    List<Busy_info> mlist;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_2, container, false);
        context = this.getActivity();
        Bmob.initialize(this.getActivity(), "2d3b9cf00c834afce97de57a3485e074");
        btn_publish_busy = (Button) view.findViewById(R.id.btn_publish_busy);
        listView_busy = (ListView) view.findViewById(R.id.listView_busy);
        SF = (SwipeRefreshLayout) view.findViewById(R.id.F2_Swipe);

        SF.setOnRefreshListener(this);
        listView_busy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Busy_info info = mlist.get(position);
                String pubid = info.getObjectId();
                Intent intent = new Intent(context, Aty_Busy_info.class);
                intent.putExtra("pubid", pubid);
                startActivity(intent);
                Toast.makeText(context, pubid, Toast.LENGTH_SHORT).show();
            }
        });


        btn_publish_busy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "BU";
                Intent intent = new Intent(context, Aty_addinfo.class);
                intent.putExtra("PUBLISH_TYPE", data);
                startActivity(intent);
            }
        });

        showinfo();
        return view;
    }

    public void showinfo() {
        BmobQuery<Busy_info> query = new BmobQuery<>();
        query.order("-createdAt");
        query.findObjects(context, new FindListener<Busy_info>() {
            @Override
            public void onSuccess(List<Busy_info> list) {
                mlist = list;
                listView_busy.setAdapter(new MyAdapter2(context, list));
                SF.setRefreshing(false);
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        showinfo();
    }

    @Override
    public void onRefresh() {
        showinfo();
    }
}
