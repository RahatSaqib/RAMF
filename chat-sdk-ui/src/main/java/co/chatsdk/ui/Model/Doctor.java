package co.chatsdk.ui.Model;

/**
 * Created by AkshayeJH on 19/06/17.
 */

public class Doctor {

    private String name;
    private String image;
    private String status;
    private String thumb_image;
    private String phone;
    private String email;
    private String password;


    public Doctor(){

    }


    public Doctor(String name, String image, String status, String thumb_image, String phone, String email, String password) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.thumb_image = thumb_image;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    public  String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public  String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.name = email;
    }

    public  String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public  String getPhone() {
        return phone;
    }

    public void setPhone(String name) {
        this.phone = phone;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public  String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public  String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

}
