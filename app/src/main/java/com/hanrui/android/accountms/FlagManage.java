package com.hanrui.android.accountms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import model.Flag;

public class FlagManage extends AppCompatActivity {

    EditText txtFlag;
    Button btnEdit,btnDel;
    String strid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flag_manage);
        txtFlag=(EditText)findViewById(R.id.txtFlag);
        btnEdit=(Button)findViewById(R.id.btnEdit);
        btnDel=(Button)findViewById(R.id.btnDel);

        Intent intent=getIntent();
        String strid=intent.getStringExtra("id");
        final int num=Integer.parseInt(strid);
        final Flag flag= DataSupport.find(Flag.class,num);
        txtFlag.setText(flag.getFlag());
        
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag.setFlag(txtFlag.getText().toString());
                flag.save();
                // 弹出信息提示
                Toast.makeText(FlagManage.this, "〖便签数据〗修改成功！",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSupport.delete(Flag.class,num);
                Toast.makeText(FlagManage.this, "〖便签数据〗删除成功！",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent=new Intent();
        setResult(RESULT_OK,intent);
    }
}
