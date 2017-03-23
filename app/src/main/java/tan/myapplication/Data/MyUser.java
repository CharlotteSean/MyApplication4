package tan.myapplication.Data;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 创建者：TAN
 * 创建时间： 2016/5/11.
 */
public class MyUser extends BmobUser {
    private BmobFile icon;

    public BmobFile getIcon() {
        return icon;
    }

    public void setIcon(BmobFile icon) {
        this.icon = icon;
    }
}
