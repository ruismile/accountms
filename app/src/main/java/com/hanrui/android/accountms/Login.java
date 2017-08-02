package com.hanrui.android.accountms;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import model.User;


/**
 * @author
 * @version 1.0
 * @date 2017/6/29 0029
 */

public class Login extends AppCompatActivity {
    private EditText txt_username,txt_password;
    private Button btn_close,btn_login,btn_register;
    private boolean flag=true;
    
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        LitePal.getDatabase();//创建AccountMS数据库
        
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        rememberPass=(CheckBox)findViewById(R.id.remember_pass);
        boolean isRemember=pref.getBoolean("remember_password",false);
        
        txt_username=(EditText)findViewById(R.id.user_name); 
        txt_password=(EditText) findViewById(R.id.tv_password);
        btn_close=(Button)findViewById(R.id.btn_close);
        btn_login=(Button)findViewById(R.id.btn_login);
        btn_register=(Button)findViewById(R.id.btn_register);
        
        if(isRemember){
            //将账户和密码设置文本框中
            String account=pref.getString("account","");
            String password=pref.getString("password","");
            txt_username.setText(account);
            txt_password.setText(password);
            rememberPass.setChecked(true);
        }
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //判断是否有此用户
                List<User>mUser=DataSupport.findAll(User.class);
                if(mUser.size()==0){
                    Toast.makeText(Login.this,"该用户名不存在，请重新输入或注册",Toast.LENGTH_SHORT).show();
                }else{
                    for(int i=0;i<mUser.size();i++){
                        if(txt_username.getText().toString().equals(mUser.get(i).getUser_name())){
                            //判断密码是否正确
                            if(!txt_password.getText().toString().equals(mUser.get(i).getPassword())){
                                Toast.makeText(Login.this,"密码错误，请重新检查用户名和密码",Toast.LENGTH_SHORT).show();
                                flag=true;
                                break;
                            }else{
                                editor=pref.edit();
                                if(rememberPass.isChecked()){
                                    editor.putBoolean("remember_password",true);
                                    editor.putString("account",txt_username.getText().toString());
                                    editor.putString("password",txt_password.getText().toString());
                                }else{
                                    editor.clear();
                                }
                                editor.apply();
                                
                                Intent intent=new Intent(Login.this,MainActivity.class);
                                intent.putExtra("UserName",txt_username.getText().toString());
                                startActivity(intent);
                                Toast.makeText(Login.this,"登录成功",Toast.LENGTH_SHORT).show();
                                flag=true;
                                break;
                            }
                        }
                        flag=false;
                    }
                }
                if(!flag){
                    Toast.makeText(Login.this,"用户名不存在，请检查账户",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });
    }
}
