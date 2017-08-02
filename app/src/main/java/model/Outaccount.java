package model;

import org.litepal.crud.DataSupport;

/**
 * @author
 * @version 1.0
 * @date 2017/6/27 0027
 */

public class Outaccount extends DataSupport {
    private int _id;    //支出编号
    private double money;   //金额
    private String time;    //时间 
    private String type;     //类别
    private String address;   //地点
    private String mark;    //备注
    
    private User mUser;

    public Outaccount(){
        super();
    }

    public Outaccount(int _id,double money,String time,String type,
                      String address,String mark, User mUser){
        super();
        this._id=_id;
        this.money=money;
        this.time=time;
        this.type=type;
        this.address=address;
        this.mark=mark;
        this.mUser=mUser;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }


}
