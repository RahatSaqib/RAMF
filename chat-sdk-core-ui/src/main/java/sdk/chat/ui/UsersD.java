package sdk.chat.ui;



public class UsersD {

    private String name;
    private String image;
    private String status;
    private String type;
    private String pid;
    private String thumb_image;
    private String phone;
    private String email;
    private String password;


public UsersD(){

}


    public UsersD(String name, String image, String status, String type, String pid, String thumb_image, String phone, String email, String password) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.type = type;
        this.pid = pid;
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
        this.email = email;
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

    public void setPhone(String phone) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
