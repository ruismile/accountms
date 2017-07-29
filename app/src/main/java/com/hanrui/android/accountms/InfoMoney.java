package com.hanrui.android.accountms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.List;

import model.Inaccount;
import model.Outaccount;

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
        
        ShowInfo(R.id.btnoutmoney);
        
        inMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowInfo(R.id.btninmoney);
            }
        });
        outMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowInfo(R.id.btnoutmoney);
            }
        });
        
        money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowInfo(R.id.btnmoney);
            }
        });
        
    }

    private void ShowInfo(int intType) {

        double getMoney=0;
        List<Inaccount>inaccounts= DataSupport.findAll(Inaccount.class);
        for(Inaccount inaccount:inaccounts){
            getMoney+=inaccount.getMoney();
        }
        DecimalFormat format = new DecimalFormat("#0.000");
        String strGetMoney=format.format(getMoney);
        final String strget="收入："+strGetMoney+"元";

        double putMoney=0;
        List<Outaccount>outaccounts=DataSupport.findAll(Outaccount.class);
        for(Outaccount outaccount:outaccounts){
            putMoney+=outaccount.getMoney();
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
