package com.hanrui.android.accountms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.List;

import model.Inaccount;
import model.Info;
import model.Outaccount;
import model.User;

public class InfoMoney extends AppCompatActivity {

    private Button inMoney,outMoney,money;
    private TextView showMoney;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_money);
        
        inMoney=(Button)findViewById(R.id.btninmoney);
        outMoney=(Button)findViewById(R.id.btnoutmoney);
        money=(Button)findViewById(R.id.btnmoney);
        showMoney=(TextView)findViewById(R.id.showmoney);

        //获取用户名
        Intent intent=getIntent();
        final String userName=intent.getStringExtra("UserName");
        
        ShowInfo(R.id.btnoutmoney,userName);
        
        inMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowInfo(R.id.btninmoney,userName);
            }
        });
        outMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowInfo(R.id.btnoutmoney,userName);
            }
        });
        
        money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowInfo(R.id.btnmoney,userName);
            }
        });
        
    }

    private void ShowInfo(int intType,String userName) {

        double getMoney=0;

        //找到用户
        List<User>mUser= DataSupport.findAll(User.class);
        for(int i=0;i<mUser.size();i++){
            if(userName.equals(mUser.get(i).getUser_name())){
                int id=mUser.get(i).get_id();
                //查询用户
                User users=DataSupport.find(User.class,id,true);
                List<Inaccount>inaccounts=users.getInaccountList();
                for(Inaccount inaccount:inaccounts){
                    getMoney+=inaccount.getMoney();
                }
            }
        }
        
        DecimalFormat format = new DecimalFormat("#0.000");
        String strGetMoney=format.format(getMoney);
        final String strget="收入："+strGetMoney+"元";

        double putMoney=0;

        //找到用户
        List<User>mUser1= DataSupport.findAll(User.class);
        for(int i=0;i<mUser1.size();i++){
            if(userName.equals(mUser1.get(i).getUser_name())){
                int id=mUser1.get(i).get_id();
                //查询用户
                User users=DataSupport.find(User.class,id,true);
                List<Outaccount>outaccounts=users.getOutaccountList();
                for(Outaccount outaccount:outaccounts){
                    putMoney+=outaccount.getMoney();
                }
            }
        }
        
        DecimalFormat format1 = new DecimalFormat("#0.000");
        String strPutMoney=format1.format(putMoney);
        final String strput="支出："+strPutMoney+"元";

        double allmoney=getMoney-putMoney;
        DecimalFormat format2 = new DecimalFormat("#0.000");
        String strAllMoney=format2.format(allmoney);
        final String all="剩余："+strAllMoney+"元";
        
        switch (intType){
            case R.id.btninmoney:
                showMoney.setText(strget);
                break;
            case R.id.btnoutmoney:
                showMoney.setText(strput);
                break;
            case R.id.btnmoney:
                showMoney.setText(all);
                break;
            default:
                break;
        }
        
    }
    
}
