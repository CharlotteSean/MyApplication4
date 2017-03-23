package tan.myapplication.Data;

import cn.bmob.v3.BmobObject;

public class Comment extends BmobObject{

    String content;
    String name;
    String pubid;
    String commentmen;

    public String getCommentmen() {
        return commentmen;
    }

    public void setCommentmen(String commentmen) {
        this.commentmen = commentmen;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPubid() {
        return pubid;
    }

    public void setPubid(String pubid) {
        this.pubid = pubid;
    }
}
