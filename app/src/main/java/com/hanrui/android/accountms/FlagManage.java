package com.hanrui.android.accountms;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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

public class FlagManage extends AppCompatActivity {

    EditText txtFlag;
    Button btnEdit,btnDel;
    String strid,accountId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flag_manage);
        txtFlag=(EditText)findViewById(R.id.txtFlag);
        btnEdit=(Button)findViewById(R.id.btnEdit);
        btnDel=(Button)findViewById(R.id.btnDel);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        
        String []strInfos=bundle.getStringArray("id");
        strid = strInfos[0];
        accountId=strInfos[2];
        final String userName=strInfos[1];
        
        final int num=Integer.parseInt(strid);
        final int accountid=Integer.valueOf(accountId);
        
        final Flag flag= DataSupport.find(Flag.class,accountid);
        txtFlag.setText(flag.getFlag());
        
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(FlagManage.this);
                dialog.setTitle("修改：");
                dialog.setMessage("是否确认修改?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        flag.setFlag(txtFlag.getText().toString());
                        flag.save();

                        //查询用户
                        List<User> mUser = DataSupport.findAll(User.class);
                        for (int i = 0; i < mUser.size(); i++) {
                            if (userName.equals(mUser.get(i).getUser_name())) {
                                int id = mUser.get(i).get_id();
                                User user = DataSupport.find(User.class, id, true);
                                List<Flag> list = user.getFlagList();
                                list.add(num, flag);
                                user.save();
                            }
                        }
                        // 弹出信息提示
                        Toast.makeText(FlagManage.this, "〖便签数据〗修改成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                
                
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            
            }
        });
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(FlagManage.this);
                dialog.setTitle("修改：");
                dialog.setMessage("是否确认修改?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataSupport.delete(Flag.class, accountid);
                        Toast.makeText(FlagManage.this, "〖便签数据〗删除成功！", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent();
                        intent.putExtra("strType","btnflaginfo");
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                });

                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent=new Intent();
        intent.putExtra("strType","btnflaginfo");
        setResult(RESULT_OK,intent);
    }
}
