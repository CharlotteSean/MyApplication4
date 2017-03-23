package tan.myapplication.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import tan.myapplication.Data.Boring_info;
import tan.myapplication.R;

/**
 * 创建者：TAN
 * 创建时间： 2016/6/19.
 */
public class Aty_Boring_info extends Activity implements View.OnClickListener {

    TextView Boring_Show_Title, Boring_Show_Content, Boring_Show_Location;
    ImageView Boring_Show_Pic;
    Button Boring_btn_PL;

    String pubid;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boring_info);

        Boring_Show_Title = (TextView) findViewById(R.id.Boring_Show_Title);
        Boring_Show_Content = (TextView) findViewById(R.id.Boring_Show_Content);
        Boring_Show_Location = (TextView) findViewById(R.id.Boring_Show_Location);
        Boring_Show_Pic = (ImageView) findViewById(R.id.Boring_Show_Pic);
        Boring_btn_PL = (Button) findViewById(R.id.Boring_btn_PL);

        Boring_btn_PL.setOnClickListener(this);

        Intent i = this.getIntent();
        pubid = i.getStringExtra("pubid");

        BmobQuery<Boring_info> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", pubid);
        query.setLimit(1);
        query.findObjects(this, new FindListener<Boring_info>() {
            @Override
            public void onSuccess(List<Boring_info> list) {
                Boring_info info = list.get(0);
                name=info.getUsername();
                Boring_Show_Title.setText(info.getTitle());
                Boring_Show_Content.setText(info.getContent());
                Boring_Show_Location.setText(info.getLocation());
                BmobFile icon = info.getPic();
                icon.loadImage(getBaseContext(), Boring_Show_Pic);
            }

            @Override
            public void onError(int i, String s) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getBaseContext(),MainActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Boring_btn_PL:
                Intent intent = new Intent(getBaseContext(), Aty_comment.class);
                intent.putExtra("comment", name);
                startActivity(intent);
                break;
        }
    }

}
