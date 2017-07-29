package com.hanrui.android.accountms;

/**
 * @author
 * @version 1.0
 * @date 2017/6/29 0029
 */

public class Picture {
    private String title;  //标题
    private int imageId;  //图片id
    
    public Picture(String title,int imageId){
        this.title=title;
        this.imageId=imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
