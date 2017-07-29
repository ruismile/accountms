package com.hanrui.android.accountms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OutTypeDIY extends AppCompatActivity {

    private EditText txt_Intype;
    private Button btn_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.out_type_diy);
        
        txt_Intype=(EditText)findViewById(R.id.tv_Intype);
        btn_button=(Button)findViewById(R.id.button);
        
        
        btn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str=txt_Intype.getText().toString();
                if(str.equals("")){
                    Toast.makeText(OutTypeDIY.this,"请输入类别！",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent();
                    intent.putExtra("strType",str);
                    setResult(RESULT_OK,intent);
                    finish();
                }
                
            }
        });
    }
}
