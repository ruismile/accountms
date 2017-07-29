package com.hanrui.android.accountms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.InfoAdapter;
import model.Flag;
import model.Info;
import model.Outaccount;

public class Outaccountinfo extends AppCompatActivity {
    private static final String FLAG="id";//定义一个常量，用来作为请求码
    ListView lvinfo;
    String strType="";

    private List<Info>mInfoList=new ArrayList<>();

    private Map<Integer,Integer> mMap=new HashMap<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outaccountinfo);
        lvinfo=(ListView)findViewById(R.id.lvoutaccountinfo);
        Showinfo();
        
        //为ListView设置点击事件
        lvinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //取数据的id
                String strid=String.valueOf(mMap.get(position+1));
                
                Intent intent=new Intent(Outaccountinfo.this,InfoManage.class);
                Bundle bundle=new Bundle();
                bundle.putStringArray(FLAG,new String[]{strid,strType});
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    private void Showinfo(){
        strType="btnoutinfo";
        mInfoList.clear();
        List<Outaccount>outaccounts= DataSupport.findAll(Outaccount.class);//查询Outaccount表中数据
        int i=1;
        for(Outaccount outaccount:outaccounts){
            Double money=outaccount.getMoney();
            DecimalFormat format = new DecimalFormat("#0.000");
            String strMoney=format.format(money);
            Info info=new Info(i+"|"+outaccount.getType()+" "+
                    strMoney+"元   "+
                    outaccount.getTime(),R.drawable.right);
            mInfoList.add(info);
            mMap.put(i,outaccount.get_id());
            i++;
        }
        //使用字符串数组初始化ArrayAdapter对象
        InfoAdapter adapter=new InfoAdapter(Outaccountinfo.this,R.layout.inout_item,mInfoList);
        lvinfo.setAdapter(adapter);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Showinfo();
    }
}
