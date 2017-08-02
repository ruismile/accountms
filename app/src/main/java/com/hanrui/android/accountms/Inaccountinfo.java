package com.hanrui.android.accountms;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuWrapperFactory;
import android.text.AndroidCharacter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import adapter.InfoAdapter;
import model.Inaccount;
import model.Info;
import model.Outaccount;
import model.User;

public class Inaccountinfo extends AppCompatActivity {

    private static final String FLAG="id";//定义一个常量，用来作为请求码
    private ListView lvinfo;     //创建ListView对象
    private String strType=""; //创建字符串，记录管理类型
    
    private List<Info>mInfoList=new ArrayList<>();
    
    private Map<Integer,Integer>mMap=new HashMap<>();
    
    private TextView emptyView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inaccountinfo);

        //获取用户名
        Intent intent=getIntent();
        final String userName=intent.getStringExtra("UserName");
        
        lvinfo=(ListView)findViewById(R.id.lvinaccountinfo);  //获取布局文件中的ListView组件
        emptyView=(TextView)findViewById(R.id.empty_view);
        lvinfo.setEmptyView(emptyView);
        
        ShowInfo(userName);   //调用自定义方法显示收入信息
        
        //为ListView添加事件监听
        lvinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //取数据的id
                String inaccountId=String.valueOf(mMap.get(position+1));
                String strid=String.valueOf(position);
                Intent intent=new Intent(Inaccountinfo.this,InfoManage.class);
                Bundle bundle=new Bundle();
                bundle.putStringArray(FLAG,new String[]{strid,strType,userName,inaccountId});
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    
    //用来根据管理类型显示相应信息
    private void ShowInfo(String userName){
        strType="btnininfo"; //为strType变量赋值
        mInfoList.clear();
        //找到用户
        List<User>mUser= DataSupport.findAll(User.class);
        for(int i=0;i<mUser.size();i++){
            if(userName.equals(mUser.get(i).getUser_name())){
                int id=mUser.get(i).get_id();
                //查询用户
                User users=DataSupport.find(User.class,id,true);
                List<Inaccount>inaccounts=users.getInaccountList();
                if(inaccounts.size()!=0){
                    int j=1;
                    for(Inaccount inaccount:inaccounts){
                        Double money=inaccount.getMoney();
                        DecimalFormat format = new DecimalFormat("#0.000");
                        String strMoney=format.format(money);
                        Info info=new Info(j+"|"+inaccount.getType()+" "+
                                strMoney+"元   "+
                                inaccount.getTime(),R.drawable.right);
                        mInfoList.add(info);
                        mMap.put(j,inaccount.get_id());
                        j++;
                    }
                }
            }
        }
        //使用字符串数组初始化ArrayAdapter对象
        InfoAdapter adapter=new InfoAdapter(Inaccountinfo.this,R.layout.inout_item,mInfoList);
        lvinfo.setAdapter(adapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //获取用户名
        Intent intent=getIntent();
        String userName=intent.getStringExtra("UserName");
        ShowInfo(userName);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
}
