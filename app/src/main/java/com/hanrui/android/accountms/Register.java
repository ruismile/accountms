package com.hanrui.android.accountms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

import model.User;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private EditText txt_username,txt_password,txt_makesure,txt_mail;
    private Button btn_register,btn_Cancel;
    private boolean flag;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        txt_username=(EditText)findViewById(R.id.user_name);
        txt_password=(EditText)findViewById(R.id.txt_password);
        txt_makesure=(EditText)findViewById(R.id.make_sure);
        btn_Cancel=(Button)findViewById(R.id.btn_Cancel);
        btn_register=(Button)findViewById(R.id.btn_register);
        txt_mail=(EditText)findViewById(R.id.mail); 
        
        btn_register.setOnClickListener(this);
        btn_Cancel.setOnClickListener(this);
    }
    
    //处理点击事件

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                isFlag();
                if(flag){
                    Log.d("Register.class","======onstart");
                    //如果flag为true，即不存在相同用户，两次密码又输入一样，注册成功
                    User user=new User();
                    user.setUser_name(txt_username.getText().toString());
                    user.setPassword(txt_password.getText().toString());
                    user.setMail(txt_mail.getText().toString());
                    user.save();
                    Toast.makeText(Register.this,"注册成功",Toast.LENGTH_SHORT).show();
                    finish();
                    flag=false;
                }
                break;
            case R.id.btn_Cancel:
                finish();
                break;
            default:
                break;
        }
    }
    
    private boolean isFlag(){
        //账户和密码不能为空
        if(txt_username.getText().toString().equals("")||
                txt_password.getText().toString().equals("")||
                txt_makesure.getText().toString().equals("")){
            Toast.makeText(Register.this,"用户名密码和确定密码不能为空",Toast.LENGTH_SHORT).show();
            flag=false;
        }else {
            //判断两次密码输入是否一样
            if (!txt_password.getText().toString().equals(txt_makesure.getText().toString())) {
                Toast.makeText(Register.this, "两次密码输入不同，请重新输入", Toast.LENGTH_SHORT).show();
                flag = false;
            } else {
                //判断是否有相同用户存在
                List<User> mUsers = DataSupport.findAll(User.class);
                if(mUsers.size()!=0){
                    for (int i = 0; i < mUsers.size(); i++) {
                        if (txt_username.getText().toString().equals(mUsers.get(i).getUser_name())) {
                            Toast.makeText(Register.this, "该用户已存在", Toast.LENGTH_SHORT).show();
                            flag = false;
                            break;
                        } else {
                            flag = true;
                        }
                    }
                }else{
                    return flag=true;
                }
                
            }
        }
        return flag;
    }
}
