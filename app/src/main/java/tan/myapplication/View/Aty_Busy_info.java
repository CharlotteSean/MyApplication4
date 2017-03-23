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
import tan.myapplication.Data.Busy_info;
import tan.myapplication.R;

/**
 * 创建者：TAN
 * 创建时间： 2016/6/20.
 */
public class Aty_Busy_info extends Activity implements View.OnClickListener {
    
    TextView Busy_Show_Title, Busy_Show_Content, Busy_Show_Location;
    ImageView Busy_Show_Pic;
    Button Busy_btn_PL;
    
    String name;
    String pubid;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.busy_info);
        Busy_Show_Title = (TextView) findViewById(R.id.Busy_Show_Title);
        Busy_Show_Content = (TextView) findViewById(R.id.Busy_Show_Content);
        Busy_Show_Location = (TextView) findViewById(R.id.Busy_Show_Location);
        Busy_Show_Pic = (ImageView) findViewById(R.id.Busy_Show_Pic);
        Busy_btn_PL = (Button) findViewById(R.id.Busy_btn_PL);
        
        
        Busy_btn_PL.setOnClickListener(this);
        Intent i = this.getIntent();
        pubid = i.getStringExtra("pubid");
        
        BmobQuery<Busy_info> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", pubid);
        query.setLimit(1);
        query.findObjects(this, new FindListener<Busy_info>() {
            @Override
            public void onSuccess(List<Busy_info> list) {
                Busy_info info = list.get(0);
                name = info.getUsername();
                Busy_Show_Title.setText(info.getTitle());
                Busy_Show_Content.setText(info.getContent());
                Busy_Show_Location.setText(info.getLocation());
                BmobFile icon = info.getPic();
                icon.loadImage(getBaseContext(), Busy_Show_Pic);
            }
            
            @Override
            public void onError(int i, String s) {
                
            }
        });
        
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getBaseContext(), MainActivity.class));
    }
    
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Busy_btn_PL:
                Intent intent = new Intent(getBaseContext(), Aty_comment.class);
                intent.putExtra("comment", name);
                startActivity(intent);
                break;
        }
    }
}
