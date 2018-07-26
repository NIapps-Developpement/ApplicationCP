package be.cerclepolytechnique.applicationcp;

public class ItemInfo {
    private String name;
    private String role;
    private String facebook;
    private String mail;
    private String phone;
    private String photonbr;

    public ItemInfo(String name, String role, String facebook, String mail, String phone, String photonbr) {
        this.name = name;
        this.role = role;
        this.facebook = facebook;
        this.mail = mail;
        this.phone = phone;
        this.photonbr = photonbr;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPhotonbr() {
        return photonbr;
    }

    public void setPhotonbr(String photonbr) {
        this.photonbr = photonbr;
    }
}
