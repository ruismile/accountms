package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanrui.android.accountms.R;

import java.util.List;

import model.Info;

/**
 * @author
 * @version 1.0
 * @date 2017/7/21 0021
 */

public class InfoAdapter extends ArrayAdapter<Info>{
    
    private int resourceId;
    
    public InfoAdapter(Context context, int textViewResourceId, List<Info>objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    
    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        Info info=getItem(position);//获取当前项的Info实例
        //为子项加载我们传入的布局
        View view;
        ViewHolder viewHolder;
        //convertView用于将之前的View进行缓存
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.infoImage=(ImageView)view.findViewById(R.id.info_image);
            viewHolder.infomessage=(TextView)view.findViewById(R.id.info_message);
            //将ViewHolder存储到View中
            view.setTag(viewHolder);
        }else{
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();//重新获取ViewHolder
        }
        viewHolder.infoImage.setImageResource(info.getImageId());
        viewHolder.infomessage.setText(info.getInfo_message());
        return view;
    }

    public final class ViewHolder{
        public ImageView infoImage;
        public TextView infomessage;
    }
}


