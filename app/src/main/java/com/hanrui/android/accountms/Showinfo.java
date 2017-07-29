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
import java.util.ArrayList;
import java.util.List;

import adapter.InfoAdapter;
import model.Flag;
import model.Inaccount;
import model.Info;
import model.Outaccount;

public class Showinfo extends AppCompatActivity {

    public static final String FLAG="id";
    ListView lvinfo;
    Button btnininfo,btnoutinfo,btnflaginfo;
    String strType="";//用来记录管理信息

    private List<Info>mInInfoList=new ArrayList<>();
    private List<Info>mOutInfoList=new ArrayList<>();
    private List<Info>mFlagInfoList=new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showinfo);
        
        lvinfo=(ListView)findViewById(R.id.lvinfo);
        btnininfo=(Button)findViewById(R.id.btnininfo);
        btnoutinfo=(Button)findViewById(R.id.btnoutinfo);
        btnflaginfo=(Button)findViewById(R.id.btnflaginfo);
        
        ShowInfo(R.id.btnoutinfo);
        
        btnoutinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowInfo(R.id.btnoutinfo);
            }
        });
        btnininfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowInfo(R.id.btnininfo);
            }
        });
        btnflaginfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowInfo(R.id.btnflaginfo);
            }
        });
        
       lvinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               
               if(strType.equals("btnininfo")){
                   Info info=mInInfoList.get(position);
                   send(info);
               }else if(strType.equals("btnoutinfo")){
                   Info info=mOutInfoList.get(position);
                   send(info);
               }else{
                   Intent intent=null;
                   Info info=mFlagInfoList.get(position);
                   String strInfo=info.getInfo_message();
                   String strid=strInfo.substring(0,strInfo.indexOf('|'));
                   intent=new Intent(Showinfo.this,FlagManage.class);
                   intent.putExtra(FLAG,strid);
                   startActivityForResult(intent,2);
               }
           }
       });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    String strType=data.getStringExtra("strType");
                    if(strType.equals("btnininfo")){
                        ShowInfo(R.id.btnininfo);
                    }else if(strType.equals("btnoutinfo")){
                        ShowInfo(R.id.btnoutinfo);
                    }
                }
                break;
            case 2:
                if(resultCode==RESULT_OK){
                    ShowInfo(R.id.btnflaginfo);
                }
                break;
            default:
                break;
        }
    }

    private void send(Info info){
        Intent intent=null;
        String strInfo=info.getInfo_message();
        String strid=strInfo.substring(0,strInfo.indexOf('|'));
        intent=new Intent(Showinfo.this,InfoManage.class);
        Bundle bundle=new Bundle();
        bundle.putStringArray(FLAG,new String[]{strid,strType});
        intent.putExtras(bundle);
        startActivityForResult(intent,1);
    }
    
    private void ShowInfo(int intType){
        switch(intType){
            case R.id.btnoutinfo:
                strType="btnoutinfo";
                mOutInfoList.clear();
                mFlagInfoList.clear();
                mInInfoList.clear();
                List<Outaccount>outaccounts= DataSupport.findAll(Outaccount.class);
                for(Outaccount outaccount:outaccounts){
                    Info info=new Info(outaccount.get_id()+"|"+outaccount.getType()+" "+
                            String.valueOf(outaccount.getMoney())+"元   "+
                            outaccount.getTime(),R.drawable.right);
                    mOutInfoList.add(info);
                }
                InfoAdapter adapter=new InfoAdapter(this,R.layout.inout_item,mOutInfoList);
                lvinfo.setAdapter(adapter);
                break;
            case R.id.btnininfo:
                strType="btnininfo";
                mFlagInfoList.clear();
                mOutInfoList.clear();
                mInInfoList.clear();
                List<Inaccount>inaccounts=DataSupport.findAll(Inaccount.class);
                for(Inaccount inaccount:inaccounts){
                    Info info=new Info(inaccount.get_id()+"|"+inaccount.getType()+" "+
                            String.valueOf(inaccount.getMoney())+"元   "+
                            inaccount.getTime(),R.drawable.right);
                    mInInfoList.add(info);
                }
                InfoAdapter adapter1=new InfoAdapter(this,R.layout.inout_item,mInInfoList);
                lvinfo.setAdapter(adapter1);
                break;
            case R.id.btnflaginfo:
                strType="btnflaginfo";
                mInInfoList.clear();
                mOutInfoList.clear();
                mFlagInfoList.clear();
                List<Flag>flags=DataSupport.findAll(Flag.class);
                for(Flag flag:flags){
                    Info info=new Info(flag.get_id()+"|"+flag.getFlag(),R.drawable.right);
                    mFlagInfoList.add(info);
                }
                InfoAdapter adapter2=new InfoAdapter(this,R.layout.inout_item,mFlagInfoList);
                lvinfo.setAdapter(adapter2);
                break;
        }
        
    }
    
}
