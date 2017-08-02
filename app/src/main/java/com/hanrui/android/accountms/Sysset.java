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

import model.User;

public class Sysset extends AppCompatActivity {

    EditText oldPassword,newPassword;
    Button btnSet,btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sysset);

        //获取用户名
        Intent intent=getIntent();
        final String userName=intent.getStringExtra("UserName");
        
        oldPassword=(EditText)findViewById(R.id.old_password);
        newPassword=(EditText)findViewById(R.id.new_password); 
        btnSet=(Button)findViewById(R.id.btn_Set);
        btnCancel=(Button)findViewById(R.id.btn_Cancel);
        
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //找到用户
                List<User>mUser=DataSupport.findAll(User.class);
                for(int i=0;i<mUser.size();i++){
                    if(userName.equals(mUser.get(i).getUser_name())){
                        String password = mUser.get(i).getPassword();
                        String old=oldPassword.getText().toString();
                        String news=newPassword.getText().toString();
                        if(!old.equals(password)){
                            Toast.makeText(Sysset.this,"密码输入错误",Toast.LENGTH_SHORT).show();
                            break;
                        }else {
                            if(news.equals("")){
                                Toast.makeText(Sysset.this,"密码设置不能为空",Toast.LENGTH_SHORT).show();
                                break;
                            }else{
                                User user = mUser.get(i);
                                user.setPassword(news);
                                user.save();
                                Toast.makeText(Sysset.this,"密码设置成功",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }
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
