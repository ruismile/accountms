package com.hanrui.android.accountms;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
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

import java.text.DecimalFormat;
import java.util.Date;

import model.Inaccount;
import model.Outaccount;

public class InfoManage extends AppCompatActivity {

    private static final String DIALOG_DATE = "DialogDate";

    TextView tvtitle, mTextView, mTextView1;
    EditText txtMoney, txtHA, txtMark;
    Button btnTime, btnEdit, btnDel;
    String[] strInfos;
    String strid, strType;

    private Date mDate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_manage);

        tvtitle = (TextView) findViewById(R.id.inouttitle);
        mTextView1 = (TextView) findViewById(R.id.tvInOut);
        txtMoney = (EditText) findViewById(R.id.txtInMoney);
        //txtMoney.setInputType(InputType.TYPE_CLASS_PHONE);//设置输入类型
        btnTime = (Button) findViewById(R.id.btn_Time);
        mTextView = (TextView) findViewById(R.id.txtType);
        txtHA = (EditText) findViewById(R.id.txtInOut);
        txtMark = (EditText) findViewById(R.id.txtInMark);
        btnEdit = (Button) findViewById(R.id.btnInOutEdit);
        btnDel = (Button) findViewById(R.id.btnInOutDelete);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        strInfos = bundle.getStringArray("id");
        strid = strInfos[0];
        final int num = Integer.parseInt(strid);
        strType = strInfos[1];
        if (strType.equals("btnininfo")) {
            tvtitle.setText("收入管理");
            mTextView1.setText("付款方:");
            String[] popContents = {"工资", "兼职", "股票", "基金", "分红", "利息", 
                    "奖金", "补贴", "礼金", "租金", "应收款", "销售款", "报销款", "自定义"};
            //type点击事件
            typeClick(popContents);
            
            Inaccount inaccount = DataSupport.find(Inaccount.class, num);

            Double money=inaccount.getMoney();
            DecimalFormat format = new DecimalFormat("#0.00");
            String strMoney=format.format(money);
            
            txtMoney.setText(strMoney);
            btnTime.setText(inaccount.getTime().toString());
            mTextView.setText(inaccount.getType());
            txtHA.setText(inaccount.getHandler());
            txtMark.setText(inaccount.getMark());
        } else if (strType.equals("btnoutinfo")) {
            tvtitle.setText("支出管理");
            mTextView1.setText("地  点:");
            String[]popContents={"早餐","午餐","晚餐","夜宵",
                    "买菜","随礼","应酬","打的", "自定义"};
            //type点击事件
            typeClick(popContents);
            
            Outaccount outaccount = DataSupport.find(Outaccount.class, num);

            Double money=outaccount.getMoney();
            DecimalFormat format = new DecimalFormat("#0.00");
            String strMoney=format.format(money);

            txtMoney.setText(strMoney);
            btnTime.setText(outaccount.getTime().toString());
            mTextView.setText(outaccount.getType());
            txtHA.setText(outaccount.getAddress());
            txtMark.setText(outaccount.getMark());
        }

        //为时间按钮添加监听事件
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment2 dialog = DatePickerFragment2.newInstance(mDate);
                dialog.show(manager, DIALOG_DATE);
            }
        });


        //为修改按钮设置监听
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(InfoManage.this);
                dialog.setTitle("修改：");
                dialog.setMessage("是否确认修改?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strMoney = txtMoney.getText().toString();
                        boolean isNum = strMoney.matches("^[+]?\\d+(\\.\\d+)?$");
                        if (strType.equals("btnininfo")) {
                            if (isNum) {
                                Inaccount inaccounts = DataSupport.find(Inaccount.class, num);
                                inaccounts.setMoney(Double.parseDouble(strMoney));
                                inaccounts.setTime(btnTime.getText().toString());
                                inaccounts.setType(mTextView.getText().toString());
                                inaccounts.setHandler(txtHA.getText().toString());
                                inaccounts.setMark(txtMark.getText().toString());
                                inaccounts.save();
                                Toast.makeText(InfoManage.this, "修改成功！", Toast.LENGTH_SHORT).show();

                                Intent intent=new Intent();
                                intent.putExtra("strType",strType);
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                        } else if (strType.equals("btnoutinfo")) {
                            if (isNum) {
                                Outaccount outaccount = DataSupport.find(Outaccount.class, num);
                                outaccount.setMoney(Double.parseDouble(txtMoney.getText().toString()));
                                outaccount.setTime(btnTime.getText().toString());
                                //outaccount.setType(spType.getSelectedItem().toString());
                                outaccount.setType(mTextView.getText().toString());
                                outaccount.setAddress(txtHA.getText().toString());
                                outaccount.setMark(txtMark.getText().toString());
                                outaccount.save();
                                Toast.makeText(InfoManage.this, "修改成功！", Toast.LENGTH_SHORT).show();

                                Intent intent=new Intent();
                                intent.putExtra("strType",strType);
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                        }
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

        //为删除按钮设置监听
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(InfoManage.this);
                dialog.setTitle("删除：");
                dialog.setMessage("是否确认删除?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (strType.equals("btnininfo")) {
                            DataSupport.delete(Inaccount.class, num);
                            Toast.makeText(InfoManage.this, "删除成功！", Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent();
                            intent.putExtra("strType",strType);
                            setResult(RESULT_OK,intent);
                            finish();
                        } else if (strType.equals("btnoutinfo")) {
                            DataSupport.delete(Outaccount.class, num);
                            Toast.makeText(InfoManage.this, "删除成功！", Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent();
                            intent.putExtra("strType",strType);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
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

    //按返回键处理
    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("strType",strType);
        setResult(RESULT_OK,intent);
        
        finish();
    }

    public void getDate(Date date) {
        String mdate = (String) DateFormat.format("EEEE,MM/dd/yyyy", date);
        btnTime.setText(mdate);
    }

    //点击类别按钮
    private void typeClick(final String[]popContents){

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
                    Intent intent=new Intent(InfoManage.this,OutTypeDIY.class);
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
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String type = data.getStringExtra("strType");
                    mTextView.setText(type);
                }
        }

    }
}
