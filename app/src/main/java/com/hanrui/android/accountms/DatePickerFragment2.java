package com.hanrui.android.accountms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author
 * @version 1.0
 * @date 2017/7/7 0007
 */

public class DatePickerFragment2 extends DialogFragment {
    private static final String ARG_DATE="date";
    private DatePicker mDatePicker;

    //传递按钮记录日期给DatePickerFragment
    public static DatePickerFragment2 newInstance(Date date){
        Bundle args=new Bundle();
        args.putSerializable(ARG_DATE,date);

        DatePickerFragment2 fragment = new DatePickerFragment2();
        fragment.setArguments(args);
        return fragment;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){

        final InfoManage infoManage=(InfoManage) getActivity();

        Date date=(Date)getArguments().getSerializable(ARG_DATE);//获取日期

        //创建一个Calendar对象
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);

        View mView = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date,null);

        mDatePicker=(DatePicker)mView.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year,month,day,null);

        return new AlertDialog.Builder(getActivity())
                .setView(mView)
                .setTitle("选择时间：")
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int year=mDatePicker.getYear();
                                int month=mDatePicker.getMonth();
                                int day=mDatePicker.getDayOfMonth();
                                Date date1 = new GregorianCalendar(year,month,day).getTime();
                                infoManage.getDate(date1);
                            }
                        })
                .create();
    }
}
