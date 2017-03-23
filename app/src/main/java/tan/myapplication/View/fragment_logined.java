package tan.myapplication.View;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tan.myapplication.R;

/**
 * 个人信息界面
 */
public class fragment_logined extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logined, container, false);

        return view;

    }

}
