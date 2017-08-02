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
import model.User;

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

        //获取用户名
        Intent intent=getIntent();
        final String userName=intent.getStringExtra("UserName");
        
        Showinfo(userName);
        
        //为ListView设置点击事件
        lvinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //取数据的id
                String inaccountId=String.valueOf(mMap.get(position+1));
                String strid=String.valueOf(position);
                Intent intent=new Intent(Outaccountinfo.this,InfoManage.class);
                Bundle bundle=new Bundle();
                bundle.putStringArray(FLAG,new String[]{strid,strType,userName,inaccountId});
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    private void Showinfo(String userName){
        strType="btnoutinfo";
        mInfoList.clear();

        //找到用户
        List<User>mUser= DataSupport.findAll(User.class);
        for(int i=0;i<mUser.size();i++){
            if(userName.equals(mUser.get(i).getUser_name())){
               int id=mUser.get(i).get_id();
                //查询用户
                User users=DataSupport.find(User.class,id,true);
                List<Outaccount>outaccounts=users.getOutaccountList();
                if(outaccounts.size()!=0) {
                    int j = 1;
                    for (Outaccount outaccount : outaccounts) {
                        Double money = outaccount.getMoney();
                        DecimalFormat format = new DecimalFormat("#0.000");
                        String strMoney = format.format(money);
                        Info info = new Info(j + "|" + outaccount.getType() + " " + strMoney + "元   " + outaccount.getTime(), R.drawable.right);
                        mInfoList.add(info);
                        mMap.put(j, outaccount.get_id());
                        j++;
                    }
                }
                
            }
        }
        //使用字符串数组初始化ArrayAdapter对象
        InfoAdapter adapter=new InfoAdapter(Outaccountinfo.this,R.layout.inout_item,mInfoList);
        lvinfo.setAdapter(adapter);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        //获取用户名
        Intent intent=getIntent();
        String userName=intent.getStringExtra("UserName");
        Showinfo(userName);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
}
