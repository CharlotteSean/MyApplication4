package tan.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import tan.myapplication.Data.Boring_info;
import tan.myapplication.Data.Comment;
import tan.myapplication.R;

public class CommentAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Comment> comments;


    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
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
            convertView = View.inflate(context, R.layout.comment_item, null);
            holder.content = (TextView) convertView.findViewById(R.id.tv_comment_content);
            holder.commentmen = (TextView) convertView.findViewById(R.id.tv_comment_name);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        Comment c = comments.get(position);
        holder.content.setText(c.getContent());
        holder.commentmen.setText("评论者："+c.getCommentmen());
        return convertView;


    }

    class Holder {
        public TextView content;
        public TextView commentmen;
    }

}
