package model;

import org.litepal.crud.DataSupport;

/**
 * @author
 * @version 1.0
 * @date 2017/6/27 0027
 */

public class Pwd extends DataSupport {
    private String password;//表示用户密码
    private int _id;
    
    public Pwd(){
        super();
    }

    public Pwd(String password,int _id){
        super();
        this.password =password;
        this._id=_id;
    }

    public int get_id() {
        return _id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
