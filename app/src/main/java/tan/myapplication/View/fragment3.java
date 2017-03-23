package tan.myapplication.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import tan.myapplication.Adapter.CommentAdapter;
import tan.myapplication.Data.Comment;
import tan.myapplication.R;

public class fragment3 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public Context context;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    ListView lv_news;
    SwipeRefreshLayout SF;
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_3, container, false);
        context = this.getActivity().getBaseContext();
        Bmob.initialize(this.getActivity(), "2d3b9cf00c834afce97de57a3485e074");

        SF = (SwipeRefreshLayout) view.findViewById(R.id.F3_Swipe);
        SF.setOnRefreshListener(this);

        lv_news = (ListView) view.findViewById(R.id.listView_news);

        sp = context.getSharedPreferences("login_info", context.MODE_PRIVATE);
        editor = sp.edit();
        doquery();
        return view;
    }

    public void doquery() {
        String username = sp.getString("username", "");

        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("name", username);
        query.findObjects(context, new FindListener<Comment>() {

            public void onSuccess(List<Comment> list) {
                lv_news.setAdapter(new CommentAdapter(context, list));
                SF.setRefreshing(false);
            }

            public void onError(int i, String s) {
                Toast.makeText(context, "获取数据失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        doquery();
    }

    @Override
    public void onRefresh() {
        doquery();
    }
}
