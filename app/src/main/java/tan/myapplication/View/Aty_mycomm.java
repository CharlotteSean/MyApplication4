package tan.myapplication.View;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import tan.myapplication.Adapter.CommentAdapter;
import tan.myapplication.Data.Comment;
import tan.myapplication.R;


public class Aty_mycomm extends Activity {
    SharedPreferences sp;
    ListView lv_mycomm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycomm);
        lv_mycomm = (ListView) findViewById(R.id.listview_mycomm);

        sp = this.getSharedPreferences("login_info", this.MODE_PRIVATE);
        String username = sp.getString("username", "");
        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("username", username);

        doquery();

    }


    public void doquery() {
        String username = sp.getString("username", "");

        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("name", username);
        query.findObjects(getBaseContext(), new FindListener<Comment>() {

            public void onSuccess(List<Comment> list) {
                lv_mycomm.setAdapter(new CommentAdapter(getBaseContext(), list));
//                SF.setRefreshing(false);
            }

            public void onError(int i, String s) {
                Toast.makeText(getBaseContext(), "获取数据失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
