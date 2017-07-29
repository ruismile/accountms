package model;

import org.litepal.crud.DataSupport;

/**
 * @author
 * @version 1.0
 * @date 2017/6/27 0027
 */

public class Inaccount extends DataSupport {
    private int _id;    //收入编号
    private double money;  //金额
    private String time;    //时间
    private String type;    //类别
    private String handler; // 付款方
    private String mark;    //备注

    public Inaccount(){
        super();
    }

    public Inaccount(int _id,double money,String time,String type,String handler,String mark){
        super();
        this._id=_id;
        this.money=money;
        this.time=time;
        this.type=type;
        this.handler=handler;
        this.mark=mark;
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

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

}
