package tan.myapplication.View;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.listener.SaveListener;
import tan.myapplication.Data.Comment;
import tan.myapplication.R;

/**
 * 创建者：TAN
 * 创建时间： 2016/6/20.
 */
public class Aty_comment extends Activity implements View.OnClickListener {


    EditText edit_comment;
    Button btn_PubComment, btn_back;
    String name;

    String content = "未填写任何评论信息";
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment);


        sp = this.getSharedPreferences("login_info", this.MODE_PRIVATE);
        editor = sp.edit();
        username = sp.getString("username", "");


        edit_comment = (EditText) findViewById(R.id.edit_comment);
        btn_PubComment = (Button) findViewById(R.id.btn_pubComment);
        btn_back = (Button) findViewById(R.id.btn_comment_back);

//        content = edit_comment.getText().toString();


        Intent i = this.getIntent();
        name = i.getStringExtra("comment");

        btn_PubComment.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pubComment:
                DoComment();
                startActivity(new Intent(getBaseContext(), Aty_Boring_info.class));
                break;

            case R.id.btn_comment_back:
                startActivity(new Intent(getBaseContext(), Aty_Boring_info.class));
                break;
        }
    }


    public void DoComment() {
        Comment c = new Comment();
        content = edit_comment.getText().toString();
        c.setContent(content);
        c.setName(name);
        c.setCommentmen(username);
        c.save(getBaseContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(Aty_comment.this, "评论成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });


    }


}
