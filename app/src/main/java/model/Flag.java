package model;

import org.litepal.crud.DataSupport;

/**
 * @author
 * @version 1.0
 * @date 2017/6/27 0027
 */

public class Flag extends DataSupport{
    private int _id;  //便签编号
    private String flag; //存储便签信息
    
    private User mUser;

    public Flag(){
        super();
    }

    public Flag(int _id,String flag,User mUser){
        super();
        this._id=_id;
        this.flag=flag;
        this.mUser=mUser;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
