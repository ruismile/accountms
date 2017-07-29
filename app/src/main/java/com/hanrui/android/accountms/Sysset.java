package com.hanrui.android.accountms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import model.Pwd;

public class Sysset extends AppCompatActivity {

    EditText txtPwd;
    Button btnSet,btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sysset);
        
        txtPwd=(EditText)findViewById(R.id.txtPwd);
        btnSet=(Button)findViewById(R.id.btn_Set);
        btnCancel=(Button)findViewById(R.id.btn_Cancel);
        
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str=txtPwd.getText().toString();
                int num= DataSupport.count(Pwd.class);
                if(num==0){
                    if(str.isEmpty()){
                        // 弹出信息提示
                        Toast.makeText(Sysset.this, "请输入密码！", Toast.LENGTH_SHORT).show();
                    }else {
                        Pwd pwd = new Pwd();
                        pwd.setPassword(str);
                        pwd.save();
                        // 弹出信息提示
                        Toast.makeText(Sysset.this, "〖密码〗设置成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else{
                    if(str.isEmpty()){
                        // 弹出信息提示
                        Toast.makeText(Sysset.this, "请输入密码！", Toast.LENGTH_SHORT).show();
                    }else {
                        Pwd pwd = DataSupport.findFirst(Pwd.class);
                        pwd.setPassword(str);
                        pwd.save();
                        // 弹出信息提示
                        Toast.makeText(Sysset.this, "〖密码〗设置成功！", Toast.LENGTH_SHORT).show();
                        finish();
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
