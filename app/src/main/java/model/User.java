package model;

import android.graphics.Bitmap;
import android.os.TokenWatcher;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2017/6/27 0027
 */

public class User extends DataSupport {
    private String user_name;//用户名
    private String password;//表示用户密码
    private String mail;//邮箱
    private byte[] headshot;//头像
    private int _id;
    
    private List<Outaccount>mOutaccountList=new ArrayList<>();
    private List<Inaccount>mInaccountList= new ArrayList<>();
    private List<Flag>mFlagList=new ArrayList<>();
    
    public User(){
        super();
    }

    public User(String name,String password,String mail,byte[] headshot,int _id,List<Outaccount>mOutaccountList,
                List<Inaccount>mInaccountList,List<Flag>mFlagList){
        super();
        this.user_name=name;
        this.password =password;
        this.mail=mail;
        this.headshot=headshot;
        this._id=_id;
        this.mOutaccountList=mOutaccountList;
        this.mInaccountList=mInaccountList;
        this.mFlagList=mFlagList;
    }

    public byte[] getHeadshot() {
        return headshot;
    }

    public void setHeadshot(byte[] headshot) {
        this.headshot = headshot;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public List<Outaccount> getOutaccountList() {
        return mOutaccountList;
    }

    public void setOutaccountList(List<Outaccount> outaccountList) {
        mOutaccountList = outaccountList;
    }

    public List<Inaccount> getInaccountList() {
        return mInaccountList;
    }

    public void setInaccountList(List<Inaccount> inaccountList) {
        mInaccountList = inaccountList;
    }

    public List<Flag> getFlagList() {
        return mFlagList;
    }

    public void setFlagList(List<Flag> flagList) {
        mFlagList = flagList;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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
