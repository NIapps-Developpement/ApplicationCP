package be.cerclepolytechnique.applicationcp;

public class Item {
    private String name;
    private String date;
    private String message;
    private int photonbr;

    public Item(String name, String date, String message, int photonbr) {
        this.name = name;
        this.date = date;
        this.message = message;
        this.photonbr = photonbr;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public int getPhotonbr() {
        return photonbr;
    }

    public void setPhotonbr(int photonbr) {
        this.photonbr = photonbr;
    }
}
