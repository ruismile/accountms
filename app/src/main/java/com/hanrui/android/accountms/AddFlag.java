package com.hanrui.android.accountms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

import model.Flag;
import model.User;

public class AddFlag extends AppCompatActivity {

    EditText txtFlag;
    Button btnSave,btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountflag);

        //获取用户名
        Intent intent=getIntent();
        final String userName=intent.getStringExtra("UserName");
        
        txtFlag=(EditText)findViewById(R.id.txtFlag);
        btnSave=(Button)findViewById(R.id.btn_Save);
        btnCancel=(Button)findViewById(R.id.btn_Cancel);
        
        //为保存按钮设置监听
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strFlag=txtFlag.getText().toString();
                if(!strFlag.isEmpty()){
                    Flag flag=new Flag();
                    flag.setFlag(strFlag);
                    flag.save();

                    //找到用户
                    List<User> mUser= DataSupport.findAll(User.class);
                    for(int i=0;i<mUser.size();i++){
                        if(userName.equals(mUser.get(i).getUser_name())){
                            User user=DataSupport.find(User.class,mUser.get(i).get_id(),true);
                            List<Flag>flags=user.getFlagList();
                            
                            flags.add(flag);
                            user.save();
                        }
                    }
                    
                    Toast.makeText(AddFlag.this, "〖新增便签〗数据添加成功！",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(AddFlag.this, "请输入便签！",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
    }
}
