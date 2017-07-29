package com.hanrui.android.accountms;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static org.litepal.LitePalApplication.getContext;


public class MainActivity extends AppCompatActivity {
    private List<Picture>mPictureList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mPictureList.add(new Picture("新增支出",R.drawable.addoutaccount));
        mPictureList.add(new Picture("新增收入",R.drawable.addinaccount));
        mPictureList.add(new Picture("我的支出",R.drawable.outaccountinfo));
        mPictureList.add(new Picture("我的收入",R.drawable.inaccountinfo));
        mPictureList.add(new Picture("数据管理",R.drawable.showinfo));
        mPictureList.add(new Picture("系统设置",R.drawable.sysset));
        mPictureList.add(new Picture("收支便签",R.drawable.accountflag));
        mPictureList.add(new Picture("收支金额",R.drawable.account));
        mPictureList.add(new Picture("退出登录",R.drawable.exit));
        
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycleview);
        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);   //加载布局
        PictureAdapter adapter=new PictureAdapter(mPictureList);
        recyclerView.setAdapter(adapter);//完成RecycleView与数据关联
        adapter.setOnItemClickListener(new PictureAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Picture picture=mPictureList.get(position);
                String pictureTitle=picture.getTitle();
                Intent intent=null;
                switch(pictureTitle){
                    case "新增支出":
                        intent=new Intent(MainActivity.this,AddOutaccount.class);
                        startActivity(intent);
                        break;
                    case "新增收入":
                        intent=new Intent(MainActivity.this,AddInaccount.class);
                        startActivity(intent);
                        break;
                    case "我的支出":
                        intent=new Intent(MainActivity.this,Outaccountinfo.class);
                        startActivity(intent);
                        break;
                    case "我的收入":
                        intent=new Intent(MainActivity.this,Inaccountinfo.class);
                        startActivity(intent);
                        break;
                    case "数据管理":
                        intent=new Intent(MainActivity.this,Showinfo.class);
                        startActivity(intent);
                        break;
                    case "系统设置":
                        intent=new Intent(MainActivity.this,Sysset.class);
                        startActivity(intent);
                        break;
                    case "收支便签":
                        intent=new Intent(MainActivity.this,Accountflag.class);
                        startActivity(intent);
                        break;
                    case "收支金额":
                        intent=new Intent(MainActivity.this,InfoMoney.class);
                        startActivity(intent);
                        break;
                    
                    case "退出登录":
                       finish();
                }
            }
        });
        
    }
    
    
}

//创建一个PictureAdapter
class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder>implements View.OnClickListener{
    private List<Picture>mPictureList;

    
    
    private OnItemClickListener mOnItemClickListener = null;

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }
    
    //创建一个ViewHolder,获取子项布局中的实例
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView pictureImage;
        TextView pictureTitle;
        
        public ViewHolder(View view){
            super(view);
            pictureImage=(ImageView)view.findViewById(R.id.picture_image);
            pictureTitle=(TextView)view.findViewById(R.id.picture_title);
            
        }
    }
    
    //把要展示的数据源传入进来
    public PictureAdapter(List<Picture>picturesList){
        mPictureList=picturesList;
    }

    //把布局加载进来并把它传入ViewHolder中
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext())
               .inflate(R.layout.picture_item,parent,false);
        ViewHolder holder=new ViewHolder(view);

        //将创建的View注册点击事件
        view.setOnClickListener(this);
        
        return holder;
    }

    //对RecycleView子项数据进行赋值
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picture picture=mPictureList.get(position);
        holder.pictureImage.setImageResource(picture.getImageId());
        holder.pictureTitle.setText(picture.getTitle());
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return mPictureList.size();
    }
}
