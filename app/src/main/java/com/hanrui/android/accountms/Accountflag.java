package com.hanrui.android.accountms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import model.Flag;

public class Accountflag extends AppCompatActivity {

    EditText txtFlag;
    Button btnSave,btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountflag);
        
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
                    Toast.makeText(Accountflag.this, "〖新增便签〗数据添加成功！",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(Accountflag.this, "请输入便签！",
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
