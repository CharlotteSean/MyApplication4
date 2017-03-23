package tan.myapplication.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import tan.myapplication.Data.MyUser;
import tan.myapplication.R;

public class Aty_login extends Activity {
    EditText editText_username, editText_psw;
    Button btn_login, btn_register;

    CheckBox checkBox_remember;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bmob.initialize(this, "2d3b9cf00c834afce97de57a3485e074");

        sp = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        editor = sp.edit();
        String username = sp.getString("username", null);
        String psw = sp.getString("psw", null);


        editText_username = (EditText) findViewById(R.id.editText_username);
        editText_psw = (EditText) findViewById(R.id.editText_psw);

        if (username == null || psw == null) {

        } else {
            editText_username.setText(username);
            editText_psw.setText(psw);
        }

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_login_register);

        checkBox_remember = (CheckBox) findViewById(R.id.checkBox_remember);

        checkBox_remember.isChecked();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Aty_register.class));
            }
        });

        /**
         * 登录按钮的点击事件
         */
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editText_username.getText().toString().trim();
                String psw = editText_psw.getText().toString().trim();
                DoLogin(username, psw);

            }
        });

    }


    public void DoLogin(final String username, final String psw) {
        BmobUser bu = new BmobUser();
        bu.setUsername(username);
        bu.setPassword(psw);
        bu.login(Aty_login.this, new SaveListener() {
            public void onSuccess() {
                if (checkBox_remember.isChecked()) {
                    editor.putString("username", username);
                    editor.putString("psw", psw);
                    editor.commit();
                } else {
                    editor.clear();
                    editor.commit();
                }
                Toast.makeText(Aty_login.this, "OK", Toast.LENGTH_SHORT).show();
                String code = username;
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("code", code);
                startActivity(intent);
            }

            public void onFailure(int i, String s) {
                Toast.makeText(Aty_login.this, s, Toast.LENGTH_LONG).show();
            }
        });
    }


}
