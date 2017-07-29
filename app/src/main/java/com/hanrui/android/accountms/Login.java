package com.hanrui.android.accountms;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

import model.Pwd;


/**
 * @author
 * @version 1.0
 * @date 2017/6/29 0029
 */

public class Login extends AppCompatActivity {
    private EditText txt_login;
    private Button btn_close;
    private Button btn_login;
    private Button btn_clear;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        LitePal.getDatabase();//创建AccountMS数据库
        
        txt_login=(EditText) findViewById(R.id.tv_login);
        btn_close=(Button)findViewById(R.id.btn_close);
        btn_login=(Button)findViewById(R.id.btn_login);
        btn_clear=(Button)findViewById(R.id.btn_clear);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num=DataSupport.count(Pwd.class);
                Intent intent=new Intent(Login.this,MainActivity.class);
                //判断密码表是否有密码，若没有，则初始密码为空，直接进去
                if(num==0){
                    startActivity(intent);
                }else{
                    Pwd pwds=DataSupport.findFirst(Pwd.class);
                    //判断输入密码与数据库中的密码是否一致
                    if(pwds.getPassword().equals(txt_login.getText().toString())){
                        startActivity(intent);
                    }else{
                        Toast.makeText(Login.this,"请输入正确密码！",Toast.LENGTH_SHORT).show();
                    }
                }
                txt_login.setText(""); 
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_login.setText("");
            }
        });
    }
}
