package sdk.chat.ui;

import androidx.annotation.Keep;


@Keep
public class Friends {

    public  String date;

    public Friends(){

    }

    public Friends(String date) {
        this.date = date;
    }

    public  String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
