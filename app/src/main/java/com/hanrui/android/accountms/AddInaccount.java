package com.hanrui.android.accountms;


import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.InputType;
import android.text.format.DateFormat;
import android.text.method.NumberKeyListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.List;

import model.Inaccount;
import model.User;


public class AddInaccount extends AppCompatActivity {
    private static final String DIALOG_DATE="DialogDate";
    
    private EditText txtInMoney,txtInHandler,txtInMark;//创建4个EditText常量
    private Button btnTime,btnInSave,btnInCancel;//创建两个Button对象
    private TextView mTextView;

    private String[]popContents={"工资","兼职","股票","基金","分红","利息","奖金","补贴",
          "礼金","租金","应收款","销售款","报销款","自定义"};

    private Date mDate=new Date();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inaccount);

        //获取用户名
        Intent intent=getIntent();
        final String userName=intent.getStringExtra("UserName");
        
        txtInMoney=(EditText)findViewById(R.id.txtInMoney);
        btnTime=(Button)findViewById(R.id.btn_Time);
        txtInHandler=(EditText)findViewById(R.id.txtInHandler);
        txtInMark=(EditText)findViewById(R.id.txtInMark);
        mTextView=(TextView)findViewById(R.id.txtInType); 
        btnInSave=(Button)findViewById(R.id.btn_Save);
        btnInCancel=(Button)findViewById(R.id.btn_Cancel);

        String mdate=(String) DateFormat.format("EEEE,MM/dd/yyyy",mDate);
        btnTime.setText(mdate.toString());
        //为时间文本框设置监听事件
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FragmentManager系统自动创建并提交事物
                FragmentManager manager = getFragmentManager();
                DatePickerFragment1 dialog = DatePickerFragment1.newInstance(mDate);

                dialog.show(manager, DIALOG_DATE);
            }
        });

        // 初始化显示
        mTextView.setText(popContents[0]);
        //type点击事件
        typeClick();
        
        //为保存按钮设置监听事件
        btnInSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strInMoney=txtInMoney.getText().toString(); //获取金额文本框中数值
                boolean isNum = strInMoney.matches("^[+]?\\d+(\\.\\d+)?$");
                if(!strInMoney.isEmpty()){        //判断金额不为空
                    if(isNum) {
                        Inaccount inaccount = new Inaccount();
                        inaccount.setMoney(Double.parseDouble(strInMoney));
                        inaccount.setTime(btnTime.getText().toString());
                        inaccount.setType(mTextView.getText().toString());
                        inaccount.setHandler(txtInHandler.getText().toString());
                        inaccount.setMark(txtInMark.getText().toString());
                        inaccount.save();

                        //找到用户
                        List<User> mUser= DataSupport.findAll(User.class);
                        for(int i=0;i<mUser.size();i++){
                            if(userName.equals(mUser.get(i).getUser_name())){
                                int id=mUser.get(i).get_id();
                                //查询用户
                                User users=DataSupport.find(User.class,id,true);
                                List<Inaccount>inaccounts=users.getInaccountList();
                                inaccounts.add(inaccount);
                                users.save();
                            }
                        }
                        
                        Toast.makeText(AddInaccount.this, "数据添加成功！", Toast.LENGTH_SHORT).show();

                        finish();
                    }else{
                        Toast.makeText(AddInaccount.this, "请输入正确金额！", Toast.LENGTH_SHORT).show();
                        txtInMoney.setText("");
                    }
                }else{
                    Toast.makeText(AddInaccount.this,"请输入收入金额！",Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        //为取消按钮设置监听事件
        btnInCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void getDate(Date date){
        String mdate=(String) DateFormat.format("EEEE,MM/dd/yyyy",date);
        btnTime.setText(mdate);
    }

    private void typeClick(){

        //获取屏幕宽度
        final DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);


        //找到需要填充到pop的布局
        View view= LayoutInflater.from(this).inflate(R.layout.pop_list,null);
        //根据此布局建立pop
        final PopupWindow popupWindow=new PopupWindow(view);
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        //这样设置pop才能缩回去
        Drawable transpent=new ColorDrawable(Color.TRANSPARENT);
        popupWindow.setBackgroundDrawable(transpent);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        //填充此布局上的列表
        ListView listView=(ListView)view.findViewById(R.id.pop_lv);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,popContents);
        listView.setAdapter(adapter);
        // popupWindow.setContentView(listView);
        //listView受到点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==popContents.length-1){
                    Intent intent=new Intent(AddInaccount.this,OutTypeDIY.class);
                    startActivityForResult(intent,1);
                    popupWindow.dismiss();
                }else{
                    mTextView.setText(popContents[position]);
                    popupWindow.dismiss();
                }
            }
        });
        // 当mTextView受到点击时显示pop
        mTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popupWindow.showAsDropDown(v);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    String type=data.getStringExtra("strType");
                    mTextView.setText(type);
                }
        }
    }
}
