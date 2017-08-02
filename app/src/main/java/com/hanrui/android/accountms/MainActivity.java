package com.hanrui.android.accountms;


import android.content.DialogInterface;
import android.content.Intent;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.security.keystore.UserNotAuthenticatedException;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


import model.User;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private List<Picture>mPictureList=new ArrayList<>();
    private static final int UPDATE_MAIL=1;
    
    private DrawerLayout mDrawerLayout;
    private de.hdodenhof.circleimageview.CircleImageView imageView;
    
    private TextView username,mail;
    
    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_MAIL:
                    Bundle bundle = msg.getData();
                    mail.setText(bundle.getString("mail"));
            }
        }
    };
    
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case UPDATE_IMAGE:
                    Bundle bundle=msg.getData();
                    String path=bundle.getString("imagePath");
                    Bitmap image= BitmapFactory.decodeFile(path);
                    imageView.setImageBitmap(image);
                    break;
                default:
                    break;
            }
        }
    };
    
    private static final int UPDATE_IMAGE=2;

    //返回码，本地图库
    private static final int RESULT_IMAGE=100;
    //返回码，相机
    private static final int RESULT_CAMERA=200;
    //IMAGE类型
    private static final String IMAGE_TYPE="image/*";
    private String[]mCustomItems=new String[]{"本地相册","相机拍照"};
    //Temp照片路径
    public static String TEMP_IMAGE_PATH= Environment.getExternalStorageDirectory().getPath()+
            "/temp.png";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBar actionBar=getSupportActionBar();//获取ActionBar实例
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);//让导航按钮显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);//设置图标
        }

        NavigationView navView=(NavigationView)findViewById(R.id.nav_view);
        
        View headerLayout=navView.getHeaderView(0);
        username=(TextView)headerLayout.findViewById(R.id.user_name);
        mail=(TextView)headerLayout.findViewById(R.id.mail);

        imageView=(de.hdodenhof.circleimageview.CircleImageView)headerLayout
                .findViewById(R.id.icon_image);

        //获取用户名
        Intent intent=getIntent();
        final String userName=intent.getStringExtra("UserName");
        
        List<User>users=DataSupport.findAll(User.class);
        for(int i=0;i<users.size();i++){
            if(userName.equals(users.get(i).getUser_name())){
                byte[]images=users.get(i).getHeadshot();
                if(images!=null){
                    Bitmap headshot=BitmapFactory.decodeByteArray(images,0,images.length);
                    imageView.setImageBitmap(headshot);
                }else{
                    imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.nav_icon));
                }
            }      
        }
        imageView.setOnClickListener(this);
        
        //把用户名和邮箱显示出来
        username.setText(userName);
        List<User>mUser= DataSupport.findAll(User.class);
        for(int i=0;i<mUser.size();i++){
            if(userName.equals(mUser.get(i).getUser_name())){
                String mMail=mUser.get(i).getMail();
                if(mMail.equals("")){
                    mail.setText("(您没有设置邮箱)");
                }else {
                    mail.setText(mMail);
                }
            }
        }
        
        
        //设置menu点击事件
        navView.setCheckedItem(R.id.nav_mail);//默认选择第一个
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_mail : 
                        Intent intent1=new Intent(MainActivity.this,MailSet.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent1.putExtra("UserName",userName);
                        startActivityForResult(intent1,1);
                        break;
                    
                    case R.id.nav_setting:
                        Intent intent2=new Intent(MainActivity.this,Sysset.class);
                        startActivity(intent2);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        
        
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
                        intent.putExtra("UserName",userName);
                        startActivity(intent);
                        break;
                    case "新增收入":
                        intent=new Intent(MainActivity.this,AddInaccount.class);
                        intent.putExtra("UserName",userName);
                        startActivity(intent);
                        break;
                    case "我的支出":
                        intent=new Intent(MainActivity.this,Outaccountinfo.class);
                        intent.putExtra("UserName",userName);
                        startActivity(intent);
                        break;
                    case "我的收入":
                        intent=new Intent(MainActivity.this,Inaccountinfo.class);
                        intent.putExtra("UserName",userName);
                        startActivity(intent);
                        break;
                    case "数据管理":
                        intent=new Intent(MainActivity.this,Showinfo.class);
                        intent.putExtra("UserName",userName);
                        startActivity(intent);
                        break;
                    case "系统设置":
                        intent=new Intent(MainActivity.this,Sysset.class);
                        intent.putExtra("UserName",userName);
                        startActivity(intent);
                        break;
                    case "收支便签":
                        intent=new Intent(MainActivity.this,AddFlag.class);
                        intent.putExtra("UserName",userName);
                        startActivity(intent);
                        break;
                    case "收支金额":
                        intent=new Intent(MainActivity.this,InfoMoney.class);
                        intent.putExtra("UserName",userName);
                        startActivity(intent);
                        break;
                    
                    case "退出登录":
                       finish();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.icon_image:
                showDialogCustom();
                break;
            default:
                break;
        }
    }
    
    //显示选择相机，图库对话框
    private void showDialogCustom(){
        //创建对话框
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("请选择：");
        builder.setItems(mCustomItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0) {
                    //相册
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    //content://media/external/images/media
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_TYPE);
                    startActivityForResult(intent, RESULT_IMAGE);
                }else if(which==1){
                    //照相机
                    Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri photoUri=Uri.fromFile(new File(TEMP_IMAGE_PATH));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                    startActivityForResult(intent,RESULT_CAMERA);
                }
            }
        });
        builder.create().show();
    }
    

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    String mail=data.getStringExtra("mail");
                    Message message=new Message();
                    message.what=UPDATE_MAIL;
                    Bundle bundle = new Bundle();
                    bundle.putString("mail",mail);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
                break;
            case RESULT_IMAGE:
                if(resultCode==RESULT_OK&&data!=null){
                    String[]proj={MediaStore.Images.Media.DATA};
                    //相册
                    Cursor cursor=this.getContentResolver().query(data.getData(), proj,null,null,null);
                    if(cursor!=null&&cursor.moveToFirst()){
                        String imagePath=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                        Message message=new Message();
                        message.what=UPDATE_IMAGE;
                        Bundle bundle = new Bundle();
                        bundle.putString("imagePath",imagePath);
                        message.setData(bundle);
                        mHandler.sendMessage(message);

                        //获取到图片
                        Bitmap headShot=BitmapFactory.decodeFile(imagePath);
                        //把图片转换字节流
                        byte[]images=img(headShot);
                        //获取用户名
                        Intent intent=getIntent();
                        final String userName=intent.getStringExtra("UserName");
                        //把头像存储到数据库
                        List<User>mUser=DataSupport.findAll(User.class);
                        for(int i=0;i<mUser.size();i++){
                            if(userName.equals(mUser.get(i).getUser_name())){
                                User user=mUser.get(i);
                                user.setHeadshot(images);
                                user.save();
                            }
                        }
                    }else{
                        String imagePath=data.getData().getPath();
                        Message message=new Message();
                        message.what=UPDATE_IMAGE;
                        Bundle bundle = new Bundle();
                        bundle.putString("imagePath",imagePath);
                        message.setData(bundle);
                        mHandler.sendMessage(message);

                        //获取到图片
                        Bitmap headShot=BitmapFactory.decodeFile(imagePath);
                        //把图片转换字节流
                        byte[]images=img(headShot);
                        //获取用户名
                        Intent intent=getIntent();
                        final String userName=intent.getStringExtra("UserName");
                        //把头像存储到数据库
                        List<User>mUser=DataSupport.findAll(User.class);
                        for(int i=0;i<mUser.size();i++){
                            if(userName.equals(mUser.get(i).getUser_name())){
                                User user=mUser.get(i);
                                user.setHeadshot(images);
                                user.save();
                            }
                        }
                    }
                }
                break;
                
            case RESULT_CAMERA:
                if(resultCode==RESULT_OK){
                    //相机
                    String imagePath=TEMP_IMAGE_PATH;
                    Message message=new Message();
                    message.what=UPDATE_IMAGE;
                    Bundle bundle = new Bundle();
                    bundle.putString("imagePath",imagePath);
                    message.setData(bundle);
                    mHandler.sendMessage(message);

                    //获取到图片
                    Bitmap headShot=BitmapFactory.decodeFile(imagePath);
                    //把图片转换字节流
                    byte[]images=img(headShot);
                    //获取用户名
                    Intent intent=getIntent();
                    final String userName=intent.getStringExtra("UserName");
                    //把头像存储到数据库
                    List<User>mUser=DataSupport.findAll(User.class);
                    for(int i=0;i<mUser.size();i++){
                        if(userName.equals(mUser.get(i).getUser_name())){
                            User user=mUser.get(i);
                            user.setHeadshot(images);
                            user.save();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
    
    //把图片转换为字节
    private byte[]img(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
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
