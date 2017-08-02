package com.hanrui.android.accountms;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.Outaccount;
import model.User;

public class AddOutaccount extends AppCompatActivity {

    
    private static final String DIALOG_DATE="DialogDate";
   
    
    private EditText txtMoney,txtAddress,txtMark;
    private Button btnTime,btnSave,btnCancel;
   private Date mDate=new Date();
    
    private TextView mTextView;
    private String[]popContents={"早餐","午餐","晚餐","夜宵","买菜",
            "随礼","应酬","打的", "自定义"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_outaccount);
        
        //获取用户名
        Intent intent=getIntent();
        final String userName=intent.getStringExtra("UserName");
        
        txtMoney=(EditText)findViewById(R.id.txtOutMoney);
        //txtMoney.setInputType(InputType.TYPE_CLASS_PHONE);
        btnTime=(Button)findViewById(R.id.btn_Time);
        txtAddress=(EditText)findViewById(R.id.txtOutAddress);
        txtMark=(EditText)findViewById(R.id.txtOutMark);
        mTextView=(TextView)findViewById(R.id.txtOutType);
        btnSave=(Button)findViewById(R.id.btn_Save);
        btnCancel=(Button)findViewById(R.id.btn_Cancel);

        // 初始化显示
        mTextView.setText(popContents[0]);

        String mdate=(String) DateFormat.format("EEEE,MM/dd/yyyy",mDate);
        btnTime.setText(mdate.toString());
        
        btnTime.setOnClickListener(new View.OnClickListener() {// 为时间文本框设置单击监听事件
            @Override
            public void onClick(View arg0) {
                //FragmentManager系统自动创建并提交事物
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mDate);
                
                dialog.show(manager, DIALOG_DATE);
            }
        });
        
        
        //type点击事件
       typeClick();
        
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strMoney=txtMoney.getText().toString();
                boolean isNum = strMoney.matches("^[+]?\\d+(\\.\\d+)?$");
                if(!strMoney.isEmpty()) {        //判断金额不为空
                    if(isNum) {
                        Outaccount outaccount = new Outaccount();
                        outaccount.setMoney(Double.parseDouble(strMoney));
                        outaccount.setTime(btnTime.getText().toString());
                        outaccount.setType(mTextView.getText().toString());
                        outaccount.setAddress(txtAddress.getText().toString());
                        outaccount.setMark(txtMark.getText().toString());
                        outaccount.save();
                        
                        //找到用户
                        List<User>mUser= DataSupport.findAll(User.class);
                        for(int i=0;i<mUser.size();i++){
                            if(userName.equals(mUser.get(i).getUser_name())){
                                int id=mUser.get(i).get_id();
                                //查询用户
                                User users=DataSupport.find(User.class,id,true);
                                List<Outaccount>outaccounts=users.getOutaccountList();
                                
                                outaccounts.add(outaccount);
                                users.save();
                            }
                        }
                        
                        Toast.makeText(AddOutaccount.this, "数据添加成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(AddOutaccount.this, "请输入正确金额！", Toast.LENGTH_SHORT).show();
                        txtMoney.setText("");
                    }
                }else {
                    Toast.makeText(AddOutaccount.this,"请输入收入金额！",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //为取消按钮设置监听事件
        btnCancel.setOnClickListener(new View.OnClickListener() {
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
        ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,popContents);
        listView.setAdapter(adapter);
       // popupWindow.setContentView(listView);
        //listView受到点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==popContents.length-1){
                    Intent intent=new Intent(AddOutaccount.this,OutTypeDIY.class);
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
