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

public class MailSet extends AppCompatActivity {

    private Button btn_Set,btn_Cancel;
    private EditText txt_mail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mail_set);

        //获取用户名
        final Intent intent=getIntent();
        final String userName=intent.getStringExtra("UserName");
        
        btn_Set=(Button)findViewById(R.id.btn_Set);
        btn_Cancel=(Button)findViewById(R.id.btn_Cancel);
        txt_mail=(EditText)findViewById(R.id.mail);
        
        btn_Set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<User>mUser= DataSupport.findAll(User.class);
                for(int i=0;i<mUser.size();i++){
                    if(userName.equals(mUser.get(i).getUser_name())){
                        User user=mUser.get(i);
                        user.setMail(txt_mail.getText().toString());
                        user.save();
                        Toast.makeText(MailSet.this,"邮箱修改成功",Toast.LENGTH_SHORT).show();
                        Intent intent1=new Intent();
                        intent1.putExtra("mail",txt_mail.getText().toString());
                        setResult(RESULT_OK,intent1);
                        finish();
                    }
                }
            }
        });
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
