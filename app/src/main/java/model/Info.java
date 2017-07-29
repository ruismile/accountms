package model;

/**
 * @author
 * @version 1.0
 * @date 2017/7/21 0021
 */

public class Info {
    private String info_message;
    private int imageId;
    
    public Info(String info_message, int imageId){
        this.info_message=info_message;
        this.imageId=imageId;
    }

    public String getInfo_message() {
        return info_message;
    }

    public int getImageId() {
        return imageId;
    }
}
