package tan.myapplication.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import tan.myapplication.Data.MyUser;
import tan.myapplication.R;

public class fragment4 extends Fragment {

    private Button btn_login_now, btn_reLogin, btn_mycomm, btn_mypub;
    private TextView textView_noLogined, textView_UserName;
    private ImageView imageView_icon;

    public Context context;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view1 = inflater.inflate(R.layout.fragment_4, container, false);

        context = this.getActivity().getBaseContext();
        Bmob.initialize(this.getActivity(), "2d3b9cf00c834afce97de57a3485e074");

        btn_login_now = (Button) view1.findViewById(R.id.btn_login_now);
        textView_noLogined = (TextView) view1.findViewById(R.id.textView_noLogined);
        textView_UserName = (TextView) view1.findViewById(R.id.textView_name);
        imageView_icon = (ImageView) view1.findViewById(R.id.imageView_icon);
        btn_reLogin = (Button) view1.findViewById(R.id.btn_reLogin);
        btn_mycomm = (Button) view1.findViewById(R.id.btn_MyComment);


        sp = context.getSharedPreferences("login_info", context.MODE_PRIVATE);
        editor = sp.edit();
        final String username = sp.getString("username", "");

        BmobQuery<MyUser> query = new BmobQuery<>();
        query.addWhereEqualTo("username", username);
        query.findObjects(context, new FindListener<MyUser>() {
            public void onSuccess(List<MyUser> list) {
                MyUser user = list.get(0);
                BmobFile pic = user.getIcon();
                pic.loadImage(context, imageView_icon);
                btn_login_now.setVisibility(View.GONE);
                textView_noLogined.setVisibility(View.GONE);
                textView_UserName.setText(username);
                imageView_icon.setVisibility(View.VISIBLE);
                textView_UserName.setVisibility(View.VISIBLE);
            }

            public void onError(int i, String s) {
                Toast.makeText(context, "查找用户名失败：" + s, Toast.LENGTH_SHORT).show();
            }
        });


        btn_login_now.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(context, Aty_login.class));
            }
        });

        btn_reLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editor.clear();
                editor.commit();
                startActivity(new Intent(context, Aty_login.class));
            }
        });
        btn_mycomm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(context, Aty_mycomm.class));
            }
        });


        return view1;

    }


}
