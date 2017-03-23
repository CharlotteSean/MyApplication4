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
import tan.myapplication.Data.Boring_info;
import tan.myapplication.R;

public class fragment1 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public Context context;
    Button btn_publish_boring;
    ListView listView_boring;
    SwipeRefreshLayout Sf;

    List<Boring_info> mlist;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false);

        context = this.getActivity();
        Bmob.initialize(this.getActivity(), "2d3b9cf00c834afce97de57a3485e074");
        btn_publish_boring = (Button) view.findViewById(R.id.btn_publish_boring);
        listView_boring = (ListView) view.findViewById(R.id.listView_boring);
        Sf = (SwipeRefreshLayout) view.findViewById(R.id.F1_Swipe);

        Sf.setOnRefreshListener(this);


        listView_boring.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Boring_info info = mlist.get(position);
                String pubid = info.getObjectId();

                Intent intent = new Intent(context, Aty_Boring_info.class);
                intent.putExtra("pubid", pubid);
                startActivity(intent);

                Toast.makeText(context, pubid, Toast.LENGTH_SHORT).show();
            }
        });

        btn_publish_boring.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String data = "BO";
                Intent intent = new Intent(context, Aty_addinfo.class);
                intent.putExtra("PUBLISH_TYPE", data);
                startActivity(intent);
            }
        });

        showinfo();
        return view;
    }


    public void showinfo() {
        BmobQuery<Boring_info> query = new BmobQuery<>();
        query.order("-createdAt");
        query.findObjects(context, new FindListener<Boring_info>() {

            @Override
            public void onSuccess(List<Boring_info> list) {
                mlist = list;
                listView_boring.setAdapter(new MyAdapter(context, list));
                Sf.setRefreshing(false);
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
