package tan.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import tan.myapplication.Data.Busy_info;
import tan.myapplication.R;

public class MyAdapter2 extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Busy_info> boringinfos;

    public MyAdapter2(Context context, List<Busy_info> boringinfos) {
        this.context = context;
        this.boringinfos = boringinfos;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return boringinfos.size();
    }

    @Override
    public Object getItem(int position) {
        return boringinfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(context, R.layout.list_item, null);
            holder.iv_image = (ImageView) convertView.findViewById(R.id.listview_pic);
            holder.title = (TextView) convertView.findViewById(R.id.tv_username);
            holder.content = (TextView) convertView.findViewById(R.id.tv_psw);
            holder.location = (TextView) convertView.findViewById(R.id.textview_location);
            holder.name = (TextView) convertView.findViewById(R.id.tv_user);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        Busy_info busyInfo = boringinfos.get(position);

        String locationstr = busyInfo.getLocation();
        if (locationstr == null) {
            locationstr = "hello";
        } else {
            locationstr = "当前位置：" + locationstr + "附近";
        }

        holder.title.setText(busyInfo.getTitle());
        holder.content.setText(busyInfo.getContent());
        holder.location.setText(locationstr);
        holder.name.setText("发布者："+busyInfo.getUsername());
        BmobFile icon = busyInfo.getPic();
        icon.loadImage(context, holder.iv_image);

        return convertView;
    }


    class Holder {
        public ImageView iv_image;
        public TextView title;
        public TextView content;
        public TextView location;
        public TextView name;
    }


}
