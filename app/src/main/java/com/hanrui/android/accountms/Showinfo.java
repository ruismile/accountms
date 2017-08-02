package com.hanrui.android.accountms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.security.PrivateKey;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.InfoAdapter;
import model.Flag;
import model.Inaccount;
import model.Info;
import model.Outaccount;
import model.User;

public class Showinfo extends AppCompatActivity {

    public static final String FLAG="id";
    ListView lvinfo;
    Button btnininfo,btnoutinfo,btnflaginfo;
    String strType="";//用来记录管理信息

    private List<Info>mInInfoList=new ArrayList<>();
    private List<Info>mOutInfoList=new ArrayList<>();
    private List<Info>mFlagInfoList=new ArrayList<>();

    private Map<Integer,Integer> mMap1=new HashMap<>();
    private Map<Integer,Integer> mMap2=new HashMap<>();
    private Map<Integer,Integer> mMap3=new HashMap<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showinfo);
        
        lvinfo=(ListView)findViewById(R.id.lvinfo);
        btnininfo=(Button)findViewById(R.id.btnininfo);
        btnoutinfo=(Button)findViewById(R.id.btnoutinfo);
        btnflaginfo=(Button)findViewById(R.id.btnflaginfo);

        //获取用户名
        Intent intent=getIntent();
        final String userName=intent.getStringExtra("UserName");
        
        ShowInfo(R.id.btnoutinfo,userName);
        
        btnoutinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowInfo(R.id.btnoutinfo,userName);
            }
        });
        btnininfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowInfo(R.id.btnininfo,userName);
            }
        });
        btnflaginfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowInfo(R.id.btnflaginfo,userName);
            }
        });
        
       lvinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               
               if(strType.equals("btnininfo")){
                   send(position,userName,mMap2);
               }else if(strType.equals("btnoutinfo")){
                   send(position,userName,mMap1);
               }else{
                   Intent intent=null;
                   String strid=String.valueOf(position);
                   String accountId=String.valueOf(mMap3.get(position+1));
                   intent=new Intent(Showinfo.this,FlagManage.class);
                   Bundle bundle=new Bundle();
                   bundle.putStringArray(FLAG,new String[]{strid,userName,accountId});
                   intent.putExtras(bundle);
                   startActivityForResult(intent,2);
               }
           }
       });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //获取用户名
        Intent intent=getIntent();
        final String userName=intent.getStringExtra("UserName");
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    String strType=data.getStringExtra("strType");
                    if(strType.equals("btnininfo")){
                        ShowInfo(R.id.btnininfo,userName);
                    }else if(strType.equals("btnoutinfo")){
                        ShowInfo(R.id.btnoutinfo,userName);
                    }
                }
                break;
            case 2:
                if(resultCode==RESULT_OK){
                    ShowInfo(R.id.btnflaginfo,userName);
                }
                break;
            default:
                break;
        }
    }

    private void send(int position,String userName,Map mMap){
        Intent intent=null;
        String strid=String.valueOf(position);
        String accountId=String.valueOf(mMap.get(position+1));
        intent=new Intent(Showinfo.this,InfoManage.class);
        Bundle bundle=new Bundle();
        bundle.putStringArray(FLAG,new String[]{strid,strType,userName,accountId});
        intent.putExtras(bundle);
        startActivityForResult(intent,1);
    }
    
    private void ShowInfo(int intType,String userName){
        switch(intType){
            case R.id.btnoutinfo:
                strType="btnoutinfo";
                mOutInfoList.clear();
                mFlagInfoList.clear();
                mInInfoList.clear();

                //找到用户
                List<User>mUser= DataSupport.findAll(User.class);
                for(int i=0;i<mUser.size();i++){
                    if(userName.equals(mUser.get(i).getUser_name())){
                        int id=mUser.get(i).get_id();
                        //查询用户
                        User users=DataSupport.find(User.class,id,true);
                        List<Outaccount>outaccounts=users.getOutaccountList();
                        int j=1;
                        for(Outaccount outaccount:outaccounts){
                            Double money=outaccount.getMoney();
                            DecimalFormat format = new DecimalFormat("#0.000");
                            String strMoney=format.format(money);
                            Info info=new Info(j+"|"+outaccount.getType()+" "+
                                    strMoney+"元   "+
                                    outaccount.getTime(),R.drawable.right);
                            mOutInfoList.add(info);
                            mMap1.put(j,outaccount.get_id());
                            j++;
                        }
                    }
                }
                InfoAdapter adapter=new InfoAdapter(this,R.layout.inout_item,mOutInfoList);
                lvinfo.setAdapter(adapter);
                break;
            case R.id.btnininfo:
                strType="btnininfo";
                mFlagInfoList.clear();
                mOutInfoList.clear();
                mInInfoList.clear();
                //找到用户
                List<User>mUser1= DataSupport.findAll(User.class);
                for(int i=0;i<mUser1.size();i++){
                    if(userName.equals(mUser1.get(i).getUser_name())){
                        int id=mUser1.get(i).get_id();
                        //查询用户
                        User users=DataSupport.find(User.class,id,true);
                        List<Inaccount>inaccounts=users.getInaccountList();
                        int j=1;
                        for(Inaccount inaccount:inaccounts){
                            Double money=inaccount.getMoney();
                            DecimalFormat format = new DecimalFormat("#0.000");
                            String strMoney=format.format(money);
                            Info info=new Info(j+"|"+inaccount.getType()+" "+
                                    strMoney+"元   "+
                                    inaccount.getTime(),R.drawable.right);
                            mInInfoList.add(info);
                            mMap2.put(j,inaccount.get_id());
                            j++;
                        }
                    }
                }
                InfoAdapter adapter1=new InfoAdapter(this,R.layout.inout_item,mInInfoList);
                lvinfo.setAdapter(adapter1);
                break;
            case R.id.btnflaginfo:
                strType="btnflaginfo";
                mInInfoList.clear();
                mOutInfoList.clear();
                mFlagInfoList.clear();
                //找到用户
                List<User>mUser2= DataSupport.findAll(User.class);
                for(int i=0;i<mUser2.size();i++){
                    if(userName.equals(mUser2.get(i).getUser_name())){
                        int id=mUser2.get(i).get_id();
                        //查询用户
                        User users=DataSupport.find(User.class,id,true);
                        List<Flag>flags=users.getFlagList();
                        int j=1;
                        for(Flag flag:flags){
                            Info info=new Info(j+"|"+flag.getFlag(),R.drawable.right);
                            mFlagInfoList.add(info);
                            mMap3.put(j,flag.get_id());
                            j++;
                        }
                    }
                }
                InfoAdapter adapter2=new InfoAdapter(this,R.layout.inout_item,mFlagInfoList);
                lvinfo.setAdapter(adapter2);
                break;
        }
        
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
    
}
